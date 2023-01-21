package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import de.etgramli.battlebros.model.Deck;
import de.etgramli.battlebros.model.Game;
import de.etgramli.battlebros.model.GameInterface;
import de.etgramli.battlebros.model.Player;
import de.etgramli.battlebros.util.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class GameController implements IObserver {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String URL_JOIN_GAME = "/topic/joingame";
    private static final String URL_PLAYER_NAMES = "/topic/names";
    private static final String URL_PLAYER_HANDS = "/topic/hand";
    private static final String URL_GAME_BOARD = "/topic/board";
    private static final String URL_PLAYER_STRENGTH = "/topic/strength";
    private static final String URL_LIFE_CARDS = "/topic/lifecards";
    private static final String URL_ACTIVE_PLAYER = "/topic/activeplayer";

    @Autowired
    private SimpMessagingTemplate template;

    // ToDo: Allow for multiple games
    private GameInterface game;
    private final LinkedHashMap<String, Principal> nameToPrincipal = new LinkedHashMap<>(2);


    @MessageMapping("/joingame")
    @SendToUser(URL_JOIN_GAME)
    public int joinGame(@NonNull final SimpMessageHeaderAccessor sha, final String playerName) {
        if (sha.getUser() == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        if (nameToPrincipal.size() < 2) {
            nameToPrincipal.put(playerName, sha.getUser());
            logger.info("Player with name \"%s\" and UUID \"%s\" joined the game. Got index: %s"
                    .formatted(playerName, sha.getUser().getName(), nameToPrincipal.size()));
        }

        if (nameToPrincipal.size() == 1) {
            return 0;
        } else if (nameToPrincipal.size() == 2) {
            final List<String> players = List.copyOf(nameToPrincipal.keySet());
            game = new Game(new Player(players.get(0), Deck.DECKS.get("Feurio!")),
                            new Player(players.get(1), Deck.DECKS.get("The River is flowing")));
            game.addObserver(this);
            logger.info("Game instance created");
            template.convertAndSend(URL_PLAYER_NAMES, List.of(game.getPlayerName(0), game.getPlayerName(1)));

            game.startGame();
            return 1;
        } else {
            // ToDo: Return 404 or Forbidden
            return -1;
        }
    }

    @MessageMapping("/placecard")
    public void placeCard(@NonNull final SimpMessageHeaderAccessor sha, @NonNull final String indices) {
        final int currentPlayerIndex = game.getTurnPlayerIndex();

        // Test if current player index is correct
        if (sha.getUser() == null) {    // User must have Principal with UUID
            logger.error("No username provided!");
            return;
        }

        final int commaIndex = indices.indexOf(',');
        final int handIndex = Integer.parseInt(indices.substring(0, commaIndex));
        final int boardIndex = Integer.parseInt(indices.substring(commaIndex + 1));

        if (indices.length() != 3) {
            logger.error("Indices array from frontend is wrong size: %d, was expecting: 2 (hand index, board index)"
                    .formatted(indices.length()));
        }
        logger.info("Player with UUID %s tried to place hand card %s at board position %d"
                .formatted(sha.getUser().getName(), handIndex, boardIndex));

        final String currentPlayerName = game.getPlayerName(currentPlayerIndex);
        final String callingPlayerUuid = sha.getUser().getName();
        final Optional<String> callingPlayerName = nameToPrincipal.entrySet().stream()
                .filter(entry -> entry.getValue().getName().equals(callingPlayerUuid))
                .map(Map.Entry::getKey)
                .findFirst();
        if (callingPlayerName.isEmpty()) {  // User must have one of the player's UUID
            logger.error("Player with UUID \"%s\" not found!".formatted(callingPlayerUuid));
            return;
        }
        if (!currentPlayerName.equals(callingPlayerName.get())) {   // Current player name must be calling player name
            logger.error("Wrong player (%s) made call to place card!".formatted(sha.getUser().getName()));
            return;
        }

        game.playCard(currentPlayerIndex, handIndex, boardIndex);
    }

    @MessageMapping("/pass")
    public void pass(@NonNull final SimpMessageHeaderAccessor sha) {
        final int currentPlayerIndex = game.getTurnPlayerIndex();

        // Test if current player index is correct
        if (sha.getUser() == null) {    // User must have Principal with UUID
            logger.error("No username provided!");
            return;
        }

        final String currentPlayerName = game.getPlayerName(currentPlayerIndex);
        final String callingPlayerUuid = sha.getUser().getName();
        final Optional<String> callingPlayerName = nameToPrincipal.entrySet().stream()
                .filter(entry -> entry.getValue().getName().equals(callingPlayerUuid))
                .map(Map.Entry::getKey)
                .findFirst();
        if (callingPlayerName.isEmpty()) {  // User must have one of the player's UUID
            logger.error("Player with UUID \"%s\" not found!".formatted(callingPlayerUuid));
            return;
        }
        if (!currentPlayerName.equals(callingPlayerName.get())) {   // Current player name must be calling player name
            logger.error("Wrong player (%s) made call to pass!".formatted(sha.getUser().getName()));
            return;
        }

        game.pass();
        logger.info("Player (%s) passed!".formatted(sha.getUser().getName()));
    }


    private void updateHands() {
        int counter = 0;
        for (Map.Entry<String, Principal> entry : nameToPrincipal.entrySet()) {
            final String principalName = entry.getValue().getName();
            final List<Integer> hand = game.getCardsInHand(counter++).stream().map(Card::getId).toList();
            template.convertAndSendToUser(principalName, URL_PLAYER_HANDS, hand);
            if (hand.isEmpty()) {
                logger.warn("Hand of player " + entry.getKey() + " is empty!");
            }
            logger.info("Sent hand to user \"%s\" with uuid \"%s\": %s"
                    .formatted(entry.getKey(), principalName, hand));
        }
    }

    private void updateBoards() {
        final BoardDTO boardDto = new BoardDTO(game.getCardsInPlay(0), game.getCardsInPlay(1));
        for (Principal principal : nameToPrincipal.values()) {
            template.convertAndSendToUser(principal.getName(), URL_GAME_BOARD, boardDto);
        }
    }

    private void updateStrength() {
        final List<Integer> strengths = List.of(game.getTotalValue(0), game.getTotalValue(1));
        for (Principal principal : nameToPrincipal.values()) {
            template.convertAndSendToUser(principal.getName(), URL_PLAYER_STRENGTH, strengths);
        }
        logger.info("Sent strengths: " + strengths);
    }

    private void updateLifeCards() {
        final List<Integer> numLifeCardsPerPlayer = List.of(game.getAmountOfLifeCards(0), game.getAmountOfLifeCards(1));
        for (Principal principal : nameToPrincipal.values()) {
            template.convertAndSendToUser(principal.getName(), URL_LIFE_CARDS, numLifeCardsPerPlayer);
        }
    }

    private void updateActivePlayer() {
        final int activePlayerIndex = game.getTurnPlayerIndex();
        for (Principal principal : nameToPrincipal.values()) {
            template.convertAndSendToUser(principal.getName(), URL_ACTIVE_PLAYER, activePlayerIndex);
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
}
