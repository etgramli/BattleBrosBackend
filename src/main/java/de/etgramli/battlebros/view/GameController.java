package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import de.etgramli.battlebros.model.Deck;
import de.etgramli.battlebros.model.Game;
import de.etgramli.battlebros.model.GameInterface;
import de.etgramli.battlebros.model.Player;
import de.etgramli.battlebros.util.IObserver;
import de.etgramli.battlebros.view.dto.BoardDTO;
import de.etgramli.battlebros.view.message.JoinGameMessage;
import de.etgramli.battlebros.view.message.PlaceCardMessage;
import de.etgramli.battlebros.view.message.select.MessageWithId;
import de.etgramli.battlebros.view.message.select.SelectCardMessage;
import de.etgramli.battlebros.view.message.select.SelectCardType;
import de.etgramli.battlebros.view.message.select.UserSelectedCardMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static de.etgramli.battlebros.view.message.select.SelectCardType.*;


@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public static final String URL_PLAYER_NAMES = "/topic/names";
    public static final String URL_PLAYER_HANDS = "/topic/hand";
    public static final String URL_GAME_BOARD = "/topic/board";
    public static final String URL_PLAYER_STRENGTH = "/topic/strength";
    public static final String URL_LIFE_CARDS = "/topic/lifecards";
    public static final String URL_ACTIVE_PLAYER = "/topic/activeplayer";
    public static final String URL_SHOW_GAMES_LIST = "/topic/showgames";
    public static final String URL_JOIN_GAME = "/topic/joingame";
    public static final String URL_PLAYERS_PASSED = "/topic/passed";
    public static final String URL_SELECT_CARD = "/topic/selectcard";

    @Autowired
    private SimpMessagingTemplate template;

    private final List<GameInstance> openGames = new ArrayList<>();
    private final Map<String, GameInstance> playerUuidToGame = new HashMap<>();

    @MessageMapping("/showgames")
    @SendTo(URL_SHOW_GAMES_LIST)
    public List<String> showOpenGames() {
        return openGames.stream()
                .map(GameInstance::getName)
                .toList();
    }

    @MessageMapping("/hostgame")
    @SendToUser
    public int hostGame(@NonNull final SimpMessageHeaderAccessor sha, @NonNull final String playerName) {
        final Principal callingPlayer = sha.getUser();
        if (callingPlayer == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        final GameInstance newGame = new GameInstance(playerName, callingPlayer);
        openGames.add(newGame);
        playerUuidToGame.put(callingPlayer.getName(), newGame);
        logger.info("Player with name \"%s\" and UUID \"%s\" hosts a new game. Got index: 1"
                .formatted(playerName, callingPlayer.getName()));
        template.convertAndSend(URL_SHOW_GAMES_LIST, showOpenGames());
        return 0;
    }

    @MessageMapping("/joingame")
    public int joinGame(@NonNull final SimpMessageHeaderAccessor sha, @NonNull final JoinGameMessage message) {
        final Principal callingPlayer = sha.getUser();
        if (callingPlayer == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        final GameInstance joinedGame = openGames.remove(message.getGameIndex());
        joinedGame.addPlayer(message.getPlayerName(), callingPlayer);
        playerUuidToGame.put(callingPlayer.getName(), joinedGame);
        return 1;
    }

    @MessageMapping("/placecard")
    public void placeCard(@NonNull final SimpMessageHeaderAccessor sha, @NonNull final PlaceCardMessage message) {
        final Principal callingPlayer = sha.getUser();
        if (callingPlayer == null) {    // User must have Principal with UUID
            logger.error("No username provided!");
            return;
        }
        if (!message.isValid()) {
            logger.error("Received indices were not valid: " + message);
        }
        final String userUuid = callingPlayer.getName();
        playerUuidToGame.get(userUuid).placeCard(callingPlayer, message.getHandIndex(), message.getBoardIndex());
    }

    @MessageMapping("/pass")
    public void pass(@NonNull final SimpMessageHeaderAccessor sha) {
        final Principal callingUser = sha.getUser();
        if (callingUser == null) {    // User must have Principal with UUID
            logger.error("No username provided!");
            return;
        }
        playerUuidToGame.get(callingUser.getName()).pass(callingUser);
    }

    @MessageMapping("/selectcard")
    public void selectCard(@NonNull final SimpMessageHeaderAccessor sha,
                           @NonNull final UserSelectedCardMessage message) {
        final Principal callingPlayer = sha.getUser();
        if (callingPlayer == null) {
            logger.error("No username provided!");
            return;
        }
        final String uuid = callingPlayer.getName();
        final boolean success = playerUuidToGame.get(uuid).selectCard(message);
        logger.info("User %s selected card: %s (success: %b)".formatted(uuid, message, success));
    }

    @MessageMapping("/chooseCancel")
    @SendToUser
    public boolean chooseCancel(@NonNull final SimpMessageHeaderAccessor sha) {
        final Principal callingPlayer = sha.getUser();
        if (callingPlayer == null) {
            logger.error("No username provided!");
            return false;
        }
        final String uuid = callingPlayer.getName();
        final boolean success = playerUuidToGame.get(uuid).chooseCancel(callingPlayer);
        logger.info("User %s canceled action: (success: %b)".formatted(uuid, success));
        return success;
    }

    /**
     * Wraps a single game for communication through STOMP.
     */
    class GameInstance implements IObserver {
        private GameInterface game;
        private final Principal[] playerPrincipals = new Principal[2];
        private final String playerOneName;

        // ToDo: Replace with Deque (/stack)
        private final Queue<MessageWithId> messagesToBeSent = new ConcurrentLinkedQueue<>();

        public GameInstance(@NonNull final String playerName, @NonNull final Principal principal) {
            if (StringUtils.isAnyBlank(playerName, principal.getName())) {
                throw new IllegalArgumentException("playerName and principal.getUser() must not be blank!");
            }
            playerOneName = playerName;
            playerPrincipals[0] = principal;
        }

        public void addPlayer(@NonNull final String playerName, @NonNull final Principal principal) {
            if (StringUtils.isAnyBlank(playerName, principal.getName())) {
                throw new IllegalArgumentException("playerName and principal.getUser() must not be blank!");
            }

            if (playerPrincipals[1] != null) {
                throw new RuntimeException("Second player expected to be null!");
            }
            playerPrincipals[1] = principal;

            game = new Game(new Player(playerOneName, Deck.DECKS.get("Feurio!")),
                    new Player(playerName, Deck.DECKS.get("The River is flowing")));
            logger.info("Game instance created: " + getName());
            game.addObserver(this);

            try {
                Thread.sleep(200);  // Give frontend time to subscribe to all URLS (when switching to game component)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            template.convertAndSend(URL_PLAYER_NAMES, List.of(game.getPlayerName(0), game.getPlayerName(1)));
            template.convertAndSendToUser(playerPrincipals[0].getName(), URL_JOIN_GAME, 0);
            template.convertAndSendToUser(playerPrincipals[1].getName(), URL_JOIN_GAME, 1);

            game.startGame();
        }

        public String getName() {
            if (game == null) {
                return playerOneName + "'s game";
            } else {
                return "%s's and %s's game".formatted(game.getPlayerName(0), game.getPlayerName(1));
            }
        }

        private void updateHands() {
            for (int i = 0; i < playerPrincipals.length; i++) {
                final String uuid = playerPrincipals[i].getName();
                final List<Integer> hand = game.getCardsInHand(i).stream().map(Card::getId).toList();
                template.convertAndSendToUser(uuid, URL_PLAYER_HANDS, hand);
            }
        }

        private void updateBoards() {
            final BoardDTO boardDto = BoardDTO.from(game);
            for (Principal principal : playerPrincipals) {
                template.convertAndSendToUser(principal.getName(), URL_GAME_BOARD, boardDto);
            }
        }

        private void updateStrength() {
            final List<Integer> strengths = List.of(game.getTotalValue(0), game.getTotalValue(1));
            for (Principal principal : playerPrincipals) {
                template.convertAndSendToUser(principal.getName(), URL_PLAYER_STRENGTH, strengths);
            }
        }

        private void updateLifeCards() {
            final List<Integer> numLifeCardsPerPlayer = List.of(game.getAmountOfLifeCards(0), game.getAmountOfLifeCards(1));
            for (Principal principal : playerPrincipals) {
                template.convertAndSendToUser(principal.getName(), URL_LIFE_CARDS, numLifeCardsPerPlayer);
            }
            // If a player won: remove game and detach observer to garbage-collect
            if (numLifeCardsPerPlayer.get(0) == 0 || numLifeCardsPerPlayer.get(1) == 0) {
                for (Principal principal : playerPrincipals) {
                    playerUuidToGame.remove(principal.getName());
                }
                game.removeObservers();
            }
        }

        private void updateActivePlayer() {
            for (Principal principal : playerPrincipals) {
                template.convertAndSendToUser(principal.getName(), URL_ACTIVE_PLAYER, game.getTurnPlayerIndex());
            }
        }

        private void updatePlayersPassed() {
            for (Principal principal : playerPrincipals) {
                template.convertAndSendToUser(principal.getName(), URL_PLAYERS_PASSED, game.hasPassed());
            }
        }

        @Override
        public void update() {
            updateHands();
            updateBoards();
            updateStrength();
            updateLifeCards();
            updateActivePlayer();
            updatePlayersPassed();
        }

        private void sendCardSelectMessage(final int playerIndex, @NonNull final SelectCardType type) {
            final SelectCardMessage selectCardMessage = new SelectCardMessage(type);
            final String playerUuid = playerPrincipals[playerIndex].getName();

            template.convertAndSendToUser(playerUuid, URL_SELECT_CARD, selectCardMessage);

            logger.info("Player %s has to select card of selectCardType %s".formatted(playerUuid, type));
            messagesToBeSent.add(selectCardMessage);
        }

        @Override
        public void selectMyHandCard(final int playerIndex) {
            sendCardSelectMessage(playerIndex, SelectCardType.SELECT_MY_HAND_CARD);
        }

        @Override
        public void selectMyPlayedCard(final int playerIndex) {
            sendCardSelectMessage(playerIndex, SelectCardType.SELECT_MY_PLAYED_CARD);
        }

        @Override
        public void selectOpponentPlayedCard(final int playerIndex) {
            sendCardSelectMessage(playerIndex, SelectCardType.SELECT_OPPONENT_PLAYED_CARD);
        }

        @Override
        public void selectAnyPlayedCard(int playerIndex) {
            sendCardSelectMessage(playerIndex, SelectCardType.SELECT_ANY_PLAYED_CARD);
        }

        @Override
        public void selectAnyPlayedCards(int playerIndex) {
            sendCardSelectMessage(playerIndex, SELECT_ANY_PLAYED_CARDS);
        }

        @Override
        public void selectDiscardedCard(final int playerIndex) {
            sendCardSelectMessage(playerIndex, SELECT_DISCARDED_CARD);
        }

        @Override
        public void selectDiscardedCards(final int playerIndex) {
            sendCardSelectMessage(playerIndex, SELECT_DISCARDED_CARDS);
        }

        @Override
        public void selectNextAbilityToResolve(final int playerIndex) {
            // ToDo
        }

        @Override
        public void selectAcceptAbility(final int playerIndex) {
            // ToDo
        }

        public boolean chooseCancel(@NonNull final Principal principal) {
            final String callingPlayerUuid = principal.getName();

            final boolean success = game.chooseCancel(indexOfPlayer(callingPlayerUuid));

            logger.info("Player with UUID %s tried to cancel action (success %b)".formatted(callingPlayerUuid, success));
            return success;
        }

        public void pass(@NonNull final Principal principal) {
            final String callingPlayerUuid = principal.getName();

            final boolean success = game.pass(indexOfPlayer(callingPlayerUuid));

            logger.info("Player with UUID %s tried to pass (success: %b)".formatted(callingPlayerUuid, success));
        }

        public void placeCard(@NonNull final Principal principal, final int handIndex, final int boardIndex) {
            final String callingPlayerUuid = principal.getName();

            final boolean success = game.playCard(indexOfPlayer(callingPlayerUuid), handIndex, boardIndex);

            logger.info("Player with UUID %s tried to place hand card %s at board position %d (success: %b)"
                    .formatted(callingPlayerUuid, handIndex, boardIndex, success));
        }

        public boolean selectCard(@NonNull final UserSelectedCardMessage message) {
            // ToDo refactor with test for principal's UUID
            final int playerIndex = message.getPlayerIndex();
            final int otherPlayerIndex = game.getOtherPlayerNum(playerIndex);
            if (message.getIndices().isEmpty()) {
                logger.info("Resending card select message because no indices present!");
                sendCardSelectMessage(playerIndex, message.getSelectCardType());
            }
            final int cardIndex = message.getIndices().get(0).getValue();
            final boolean success = switch (message.getSelectCardType()) {
                case SELECT_MY_HAND_CARD -> game.chooseCardInHand(playerIndex, cardIndex);
                case SELECT_MY_PLAYED_CARD -> game.chooseCardInPlay(playerIndex, playerIndex, cardIndex);
                case SELECT_OPPONENT_PLAYED_CARD -> game.chooseCardInPlay(playerIndex, otherPlayerIndex, cardIndex);
                case SELECT_ANY_PLAYED_CARD ->
                        game.chooseCardInPlay(playerIndex, message.getFirstPlayerRow(), cardIndex);
                case SELECT_ANY_PLAYED_CARDS -> game.chooseCardsInPlay(playerIndex, message.getIndices());
                case SELECT_DISCARDED_CARD -> game.chooseCardInDiscard(playerIndex, cardIndex);
                case SELECT_DISCARDED_CARDS -> false; // ToDo
                case SELECT_SUCCESS ->
                        throw new IllegalArgumentException("Did not expect message of type: " + message.getSelectCardType());
            };
            logger.info("Player %d selected card (%s) with index %s (success: %b)"
                    .formatted(playerIndex, message.getSelectCardType(), cardIndex, success));
            if (success) {
                template.convertAndSendToUser(playerPrincipals[playerIndex].getName(), URL_SELECT_CARD, new SelectCardMessage(SELECT_SUCCESS));
                messagesToBeSent.removeIf(messageWithId -> messageWithId.getId().equals(message.getId()));
                if (!messagesToBeSent.isEmpty()) {
                    logger.error("Some messages were lost: " + messagesToBeSent.stream().map(MessageWithId::getId).collect(Collectors.joining(", ")));
                }
            } else {
                logger.info("Resending card select message due to unsuccessful action before!");
                sendCardSelectMessage(playerIndex, message.getSelectCardType());
            }
            return success;
        }

        private int indexOfPlayer(@NonNull final String principalUuid) {
            if (playerPrincipals[0].getName().equals(principalUuid)) {
                return 0;
            } else if (playerPrincipals[1].getName().equals(principalUuid)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
