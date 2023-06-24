package de.etgramli.battlebros.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.etgramli.battlebros.model.GameInterface;
import de.etgramli.battlebros.util.CollectionUtil;
import de.etgramli.battlebros.view.messages.JoinGameMessage;
import de.etgramli.battlebros.view.messages.select.MessageWithId;
import de.etgramli.battlebros.view.messages.select.SelectCardMessage;
import de.etgramli.battlebros.view.messages.select.SelectCardType;
import de.etgramli.battlebros.view.messages.select.UserSelectedCardMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.etgramli.battlebros.view.GameController.URL_JOIN_GAME;
import static de.etgramli.battlebros.view.GameWebsocketController.APPLICATION_PREFIX;
import static de.etgramli.battlebros.view.GameWebsocketController.USER_PREFIX;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(GameControllerTest.class);

    @LocalServerPort
    private Integer port;

    @Autowired
    private GameController gameController;

    private static final String STOMP_URL = "http://localhost:%d/stomp";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final WebSocketStompClient stompClient = new WebSocketStompClient(
            new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));

    @BeforeAll
    public static void setUp() {
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    private static StompSession connect(@NonNull final String url)
            throws ExecutionException, InterruptedException, TimeoutException {
        return stompClient.connectAsync(url, new StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS);
    }

    private static void send(@NonNull final StompSession session,
                             @NonNull final String url,
                             @NonNull final Object payload) {
        session.send(APPLICATION_PREFIX + url, payload);
    }

    @NonNull
    private static <T> BlockingQueue<T> subscribeToUserTopic(@NonNull final StompSession session,
                                                             @NonNull final String topicUrl,
                                                             final int queueLength,
                                                             @NonNull final Class<T> clazz) {
        return subscribeToTopic(session, USER_PREFIX + topicUrl, queueLength, clazz);
    }

    @NonNull
    private static <T> BlockingQueue<T> subscribeToTopic(@NonNull final StompSession session,
                                                         @NonNull final String topicUrl,
                                                         final int queueLength,
                                                         @NonNull final Class<T> clazz) {
        final BlockingQueue<T> queue = new ArrayBlockingQueue<>(queueLength);
        final var subscription = session.subscribe(topicUrl, new StompFrameHandler() {
            @NonNull
            @Override
            public Type getPayloadType(@NonNull final StompHeaders headers) {
                return clazz;
            }
            @Override
            public void handleFrame(@NonNull final StompHeaders headers, @Nullable final Object payload) {
                queue.add((T) payload);
                logger.info("Added payload \"%s\" to queue (new size: %d)".formatted(payload, queue.size()));
            }
        });
        assertNotNull(subscription.getSubscriptionId());
        return queue;
    }

    @NonNull
    private static <T> BlockingQueue<T> subscribeToUserTopicJsonResult(@NonNull final StompSession session,
                                                                       @NonNull final String topicUrl,
                                                                       final int queueLength,
                                                                       @NonNull final Class<T> clazz) {
        return subscribeToTopicJsonResult(session, USER_PREFIX + topicUrl, queueLength, clazz);
    }

    @NonNull
    private static <T> BlockingQueue<T> subscribeToTopicJsonResult(@NonNull final StompSession session,
                                                                   @NonNull final String topicUrl,
                                                                   final int queueLength,
                                                                   @NonNull final Class<T> clazz) {
        final BlockingQueue<T> queue = new ArrayBlockingQueue<>(queueLength);
        final var subscription = session.subscribe(topicUrl, new StompFrameHandler() {
            @NonNull
            @Override
            public Type getPayloadType(@NonNull final StompHeaders headers) {
                return byte[].class;
            }
            @Override
            public void handleFrame(@NonNull final StompHeaders headers, @Nullable final Object payload) {
                if (payload == null) {
                    logger.error("Payload to queue \"%s\" was null!".formatted(topicUrl));
                    return;
                }
                try {
                    final String payloadString = new String((byte[]) payload);
                    final T payloadObject = new ObjectMapper().readValue(payloadString, clazz);
                    queue.add(payloadObject);
                    logger.info("Added payload \"%s\" to queue (new size: %d)".formatted(payloadObject, queue.size()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        assertNotNull(subscription.getSubscriptionId());
        return queue;
    }

    @Test
    public void test_joinGame() throws Exception {
        final String stompUrl = STOMP_URL.formatted(port);

        // Connect player 1
        final StompSession sessionOne = connect(stompUrl);
        final BlockingQueue<String> queueOne = subscribeToUserTopic(sessionOne, URL_JOIN_GAME, 1, String.class);

        // Connect player 2
        final StompSession sessionTwo = connect(stompUrl);
        final BlockingQueue<String> queueTwo = subscribeToUserTopic(sessionTwo, URL_JOIN_GAME, 1, String.class);

        // Logic
        send(sessionOne, "/hostgame", "JGT-P1");
        send(sessionTwo, "/joingame", new JoinGameMessage("JGT-P2", "0"));

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> assertEquals("0", queueOne.poll()));
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> assertEquals("1", queueTwo.poll()));
    }

    @Test
    public void test_selectOneCard_successfulResponse() throws Exception {
        final String stompUrl = STOMP_URL.formatted(port);

        // Connect player 1
        final StompSession sessionOne = connect(stompUrl);
        final BlockingQueue<Integer> joinGameQueue = subscribeToUserTopic(sessionOne, URL_JOIN_GAME, 1, Integer.class);

        // Connect player 2
        final StompSession sessionTwo = connect(stompUrl);
        final BlockingQueue<Integer> joinGameQueueTwo = subscribeToUserTopic(sessionTwo, URL_JOIN_GAME, 1, Integer.class);

        // Logic
        send(sessionOne, "/hostgame", "SHCT-P1");
        await().atMost(1, TimeUnit.SECONDS).until(() -> !gameController.showOpenGames().isEmpty());
        final int gameIndex = gameController.showOpenGames().size() - 1;
        final GameController.GameInstance gameInstance = getOpenGameInstance(gameController, gameIndex);
        assertNotNull(gameInstance);
        send(sessionTwo, "/joingame", new JoinGameMessage("SHCT-P2", String.valueOf(gameIndex)));

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertEquals(0, joinGameQueue.poll()));
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertEquals(1, joinGameQueueTwo.poll()));

        final BlockingQueue<SelectCardMessage> selectCardQueue =
                subscribeToUserTopicJsonResult(sessionOne, GameController.URL_SELECT_CARD, 1, SelectCardMessage.class);
        // Set up game behaviour
        await().atMost(1, TimeUnit.SECONDS).until(() -> isGameStarted(gameInstance));
        final GameInterface gameSpy = replaceGameInterfaceWithSpy(gameInstance);
        when(gameSpy.chooseCardInHand(eq(0), anyInt())).thenReturn(true);
        when(gameSpy.chooseCardInHand(not(eq(0)), anyInt())).thenReturn(false);

        // Game makes controller send message
        gameInstance.selectMyHandCard(0);
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> assertFalse(selectCardQueue.isEmpty()));

        SelectCardMessage selectCardMessage = selectCardQueue.poll();
        assertNotNull(selectCardMessage);
        assertEquals(SelectCardType.SELECT_MY_HAND_CARD, selectCardMessage.getSelectCardType());

        var selectedHandCardMessage = new UserSelectedCardMessage(
                selectCardMessage, 0, SelectCardType.SELECT_MY_HAND_CARD, CollectionUtil.listFromMap(Map.of(0, 0)));
        sessionOne.send(APPLICATION_PREFIX + "/selectcard", selectedHandCardMessage);
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> assertTrue(hasNoMessagesToBeSent(gameInstance)));
    }

    @Disabled
    @Test
    public void test_twoSelectCardMessages_successfulResponses() {
        // ToDo
    }

    @Disabled
    @Test
    public void test_twoSelectCardMessages_oneWithMultipleTries() {
        // ToDo
    }

    private static GameController.GameInstance getOpenGameInstance(@NonNull final GameController gameController,
                                                                    final int index) {
        try {
            final Field openGames = GameController.class.getDeclaredField("openGames");
            openGames.setAccessible(true);
            @SuppressWarnings("unchecked")
            final var openGamesList = (List<GameController.GameInstance>) openGames.get(gameController);
            return openGamesList.get(index);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isGameStarted(@NonNull final GameController.GameInstance gameInstance) {
        try {
            final Field gameInterface = GameController.GameInstance.class.getDeclaredField("game");
            gameInterface.setAccessible(true);
            return gameInterface.get(gameInstance) != null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static GameInterface replaceGameInterfaceWithSpy(@NonNull final GameController.GameInstance gameInstance) {
        try {
            final Field gameInterface = GameController.GameInstance.class.getDeclaredField("game");
            gameInterface.setAccessible(true);
            final GameInterface game = (GameInterface) gameInterface.get(gameInstance);

            final GameInterface spiedGame = spy(game);
            gameInterface.set(gameInstance, spiedGame);
            return spiedGame;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hasNoMessagesToBeSent(@NonNull final GameController.GameInstance gameInstance) {
        try {
            final Field messagesToBeSent = GameController.GameInstance.class.getDeclaredField("messagesToBeSent");
            messagesToBeSent.setAccessible(true);
            @SuppressWarnings("unchecked")
            final List<MessageWithId> unsentMessages = (List<MessageWithId>) messagesToBeSent.get(gameInstance);
            return unsentMessages.isEmpty();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
