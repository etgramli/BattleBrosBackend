package de.etgramli.battlebros.view.messages;

public class JoinGameMessage {
    private String playerName;
    private int gameIndex;

    public JoinGameMessage() {
        playerName = "";
        gameIndex = -1;
    }

    public JoinGameMessage(String playerName, String gameIndex) {
        this.playerName = playerName;
        this.gameIndex = Integer.parseInt(gameIndex);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getGameIndex() {
        return gameIndex;
    }

    public void setGameIndex(String gameIndex) {
        this.gameIndex = Integer.parseInt(gameIndex);
    }
}
