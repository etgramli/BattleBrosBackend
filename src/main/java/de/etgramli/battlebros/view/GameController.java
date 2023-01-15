package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Game;
import de.etgramli.battlebros.model.GameInterface;
import de.etgramli.battlebros.model.Player;
import de.etgramli.battlebros.util.IObserver;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController implements IObserver {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String URL_JOIN_GAME = "/topic/joingame";
    private static final String URL_PLAYER_NAMES = "/topic/names";
    private static final String URL_PLAYER_HANDS = "/topic/hand";
    private static final String URL_GAME_BOARD = "/topic/board";

    // ToDo: User Observer pattern to update players' hands and board

    @Autowired
    private SimpMessagingTemplate template;

    private GameInterface game;
    private final List<String> names = new ArrayList<>(2);
    private final LinkedHashMap<String, Principal> nameToPrincipal = new LinkedHashMap<>(2);


    @MessageMapping("/joingame")
    @SendToUser(URL_JOIN_GAME)
    public int joinGame(@NonNull final SimpMessageHeaderAccessor sha, final String playerName) {
        if (sha.getUser() == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        if (names.size() < 2) {
            names.add(playerName);
            nameToPrincipal.put(playerName, sha.getUser());
            logger.info(String.format("Player with name \"%s\" and UUID \"%s\" joined the game. Got index: %s", playerName, sha.getUser().getName(), names.size()));
        }

        if (names.size() == 1) {
            return 0;
        } else if (names.size() == 2) {
            game = new Game(new Player(names.get(0)), new Player(names.get(1)));
            game.addObserver(this);
            logger.info("Game instance created");
            names.clear();
            template.convertAndSend(URL_PLAYER_NAMES, List.of(game.getPlayerName(0), game.getPlayerName(1)));

            updateHands();
            return 1;
        } else {
            // ToDo: Return 404 or Forbidden
            return -1;
        }
    }

    /**
     * Return a copy of the state of the board using a DTO.
     * @return Unmodifiable nested list of CardDTOs.
     */
    @MessageMapping("/board")
    @SendTo("/topic/board")
    public List<List<CardDTO>> getBoard() {
        final List<List<CardDTO>> board = new ArrayList<>(2);
        //for (List<Board.CardTuple> playerRow : gameInterface.getPlayedCards()) {
        //    board.add(playerRow.stream().map(cardTuple -> CardDTO.of(cardTuple.card)).toList());
        //}
        logger.info("Sent board state");
        return Collections.unmodifiableList(board);
    }

    @MessageMapping("/placecard")
    @SendTo("/topic/board")
    public void placeCard(int handIndex, int boardIndex) {
        // ToDo
    }

    @MessageMapping("/pass")
    public void fold() {
        game.pass();
    }


    private void updateHandsAndBoards() {
        updateHands();
        updateBoards();
    }

    private void updateHands() {
        int counter = 0;
        for (Map.Entry<String, Principal> entry : nameToPrincipal.entrySet()) {
            final String principalName = entry.getValue().getName();
            final List<?> hand = game.getCardsInHand(counter++);
            template.convertAndSendToUser(principalName, URL_PLAYER_HANDS, hand);
            if (hand.isEmpty()) {
                logger.warn("Hand of player " + entry.getKey() + " is empty!");
            }
            logger.info(String.format("Sent hand to user \"%s\" with uuid \"%s\"",entry.getKey(), principalName));
        }
    }

    private void updateBoards() {
        for (Principal principal : nameToPrincipal.values()) {
            // ToDo: may convert to expected data type from frontend
            template.convertAndSendToUser(principal.getName(), URL_GAME_BOARD, game.getCardsInPlay());
        }
    }

    @Override
    public void update() {
        updateHandsAndBoards();
    }
}
