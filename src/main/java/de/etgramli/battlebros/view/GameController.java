package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import de.etgramli.battlebros.model.Deck;
import de.etgramli.battlebros.model.Game;
import de.etgramli.battlebros.model.GameInterface;
import de.etgramli.battlebros.model.Player;
import de.etgramli.battlebros.util.IObserver;
import de.etgramli.battlebros.view.messages.JoinGameMessage;
import de.etgramli.battlebros.view.messages.PlaceCardMessage;
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

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String URL_PLAYER_NAMES = "/topic/names";
    private static final String URL_PLAYER_HANDS = "/topic/hand";
    private static final String URL_GAME_BOARD = "/topic/board";
    private static final String URL_PLAYER_STRENGTH = "/topic/strength";
    private static final String URL_LIFE_CARDS = "/topic/lifecards";
    private static final String URL_ACTIVE_PLAYER = "/topic/activeplayer";
    private static final String URL_SHOW_GAMES_LIST = "/topic/showgames";
    public static final String URL_JOIN_GAME = "/topic/joingame";

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
    public int hostGame(@NonNull final SimpMessageHeaderAccessor sha, final String playerName) {
        if (sha.getUser() == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        openGames.add(new GameInstance(playerName, sha.getUser()));
        logger.info("Player with name \"%s\" and UUID \"%s\" hosts a new game. Got index: 1"
                .formatted(playerName, sha.getUser().getName()));
        template.convertAndSend(URL_SHOW_GAMES_LIST, showOpenGames());
        return 0;
    }

    @MessageMapping("/joingame")
    @SendToUser(URL_JOIN_GAME)
    public int joinGame(@NonNull final SimpMessageHeaderAccessor sha, @NonNull final JoinGameMessage message) {
        if (sha.getUser() == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        openGames.get(message.getGameIndex()).addPlayer(message.getPlayerName(), sha.getUser());
        return 1;
    }

    @MessageMapping("/placecard")
    public void placeCard(@NonNull final SimpMessageHeaderAccessor sha, @NonNull final PlaceCardMessage message) {
        if (sha.getUser() == null) {    // User must have Principal with UUID
            logger.error("No username provided!");
            return;
        }
        if (!message.isValid()) {
            logger.error("Received indices were not valid: " + message);
        }
        final Principal callingPlayer = sha.getUser();
        final String userUuid = callingPlayer.getName();
        playerUuidToGame.get(userUuid).placeCard(callingPlayer, message.getHandIndex(), message.getBoardIndex());
    }

    @MessageMapping("/pass")
    public void pass(@NonNull final SimpMessageHeaderAccessor sha) {
        if (sha.getUser() == null) {    // User must have Principal with UUID
            logger.error("No username provided!");
            return;
        }
        final Principal callingUser = sha.getUser();
        final String userUuid = callingUser.getName();
        playerUuidToGame.get(userUuid).pass(callingUser);
    }


    private class GameInstance implements IObserver {
        private GameInterface game;
        private final Principal[] playerPrincipals = new Principal[2];
        private final String playerOneName;

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

            openGames.remove(this);
            playerUuidToGame.put(playerPrincipals[0].getName(), this);
            playerUuidToGame.put(playerPrincipals[1].getName(), this);

            game = new Game(new Player(playerOneName, Deck.DECKS.get("Feurio!")),
                            new Player(playerName, Deck.DECKS.get("The River is flowing")));
            logger.info("Game instance created: " + getName());
            game.addObserver(this);

            try {
                Thread.sleep(100);  // Give frontend time to subscribe to all URLS (when switching to game component)
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
            final BoardDTO boardDto = new BoardDTO(game.getCardsInPlay(0), game.getCardsInPlay(1),
                    game.getPositionsOfFaceDownCards(0), game.getPositionsOfFaceDownCards(1));
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

        @Override
        public void update() {
            updateHands();
            updateBoards();
            updateStrength();
            updateLifeCards();
            updateActivePlayer();
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
