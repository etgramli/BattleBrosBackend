package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Board;
import de.etgramli.battlebros.model.Game;
import de.etgramli.battlebros.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String URL_PLAYER_HANDS = "/topic/hands";
    private static final String URL_GAME_BOARD = "/topic/board";

    @Autowired
    private SimpMessagingTemplate template;

    private Game game;
    private final List<String> names = new ArrayList<>(2);
    private final Map<String, Principal> nameToPrincipal = new HashMap<>(2);

    @MessageMapping("/joingame/{playername}")
    @SendTo("/topic/{playername}")
    public int joinGame(@NonNull final SimpMessageHeaderAccessor sha, final String name) {
        if (sha.getUser() == null) {
            throw new IllegalArgumentException("SimpleMessageHeaderAccessor must provide a User member!");
        }
        if (names.size() < 2) {
            names.add(name);
            nameToPrincipal.put(name, sha.getUser());
            logger.info("Player with name \"%s\" joined the game. Got index: %s".formatted(name, names.size()));
        }

        if (names.size() == 1) {
            return 0;
        } else if (names.size() == 2) {
            game = new Game(List.of(new Player(names.get(0)), new Player(names.get(1))));
            logger.info("Game instance created");
            names.clear();
            template.convertAndSend("/topic/names", List.of(game.getPlayerName(0), game.getPlayerName(1)));

            // ToDo: Send player names
            for (String playername : names) {
                final Principal principal = nameToPrincipal.get(playername);
                final int index = names.indexOf(playername);
                template.convertAndSendToUser(principal.getName(), URL_PLAYER_HANDS, game.getPlayerHand(index));
            }
            return 1;
        } else {
            // ToDo: Return 404 or Forbidden
            return -1;
        }
    }

    @MessageMapping("/hand/{playerIndex}")
    @SendTo("/topic/hand/{playerIndex}")
    public List<CardDTO> getPlayerHand(final int playerIndex) {
        if (game == null) {
            return Collections.emptyList(); // Avoid NPE when first player logged in
        }
        return game.getPlayerHand(playerIndex).stream().map(CardDTO::of).toList();
    }

    /**
     * Return a copy of the state of the board using a DTO.
     * @return Unmodifiable nested list of CardDTOs.
     */
    @MessageMapping("/board")
    @SendTo("/topic/board")
    public List<List<CardDTO>> getBoard() {
        final List<List<CardDTO>> board = new ArrayList<>(2);
        for (List<Board.CardTuple> playerRow : game.getPlayedCards()) {
            board.add(playerRow.stream().map(cardTuple -> CardDTO.of(cardTuple.card)).toList());
        }
        logger.info("Sent board state");
        return Collections.unmodifiableList(board);
    }

    @MessageMapping("/getvalidpositions")
    public Set<Board.BoardPosition> getValidPositions() {
        return game.getValidPositions();
    }

    @MessageMapping("/placecard")
    @SendTo("/topic/board")
    public void placeCard(int handIndex, int boardIndex) {
        final Board.BoardPosition position = new Board.BoardPosition(game.getCurrentPlayerIndex(), boardIndex);
        final boolean successfullyPlaced = game.playCard(handIndex, position);
        if (successfullyPlaced) {
            logger.info("Successfully played hand card with index %d at position (%d/%d)."
                    .formatted(handIndex, position.playerRow(), position.position()));
        } else {
            logger.warn("Card placement failed with hand card index %d and position (%d/%d)."
                    .formatted(handIndex, position.playerRow(), position.position()));
        }
        // ToDo: return apprpiate http code
    }

    public void fold() {
        game.fold();
        // ToDo: Add mapping and sendTo
    }
}
