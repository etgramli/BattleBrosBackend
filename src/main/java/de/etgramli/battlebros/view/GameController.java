package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Board;
import de.etgramli.battlebros.model.Game;
import de.etgramli.battlebros.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    //@Inject
    private Game game;
    private final List<String> names = new ArrayList<>(2);

    @MessageMapping("/joingame")
    public void joinGame(final String name) {
        names.add(name);
        logger.info("Player with name \"%s\" joined the game".formatted(name));

        if (names.size() == 2) {
            final List<Player> currentPlayers = List.of(new Player(names.get(0)), new Player(names.get(1)));
            names.clear();
            game = new Game(currentPlayers);
            logger.info("Game instance created");
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
