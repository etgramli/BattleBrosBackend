package de.etgramli.battlebros.view.message;

public class PlaceCardMessage {
    private int handIndex;
    private int boardIndex;

    public PlaceCardMessage() {
        this.handIndex = -1;
        this.boardIndex = -1;
    }

    public PlaceCardMessage(final int handIndex, final int boardIndex) {
        this.handIndex = handIndex;
        this.boardIndex = boardIndex;
    }

    public int getHandIndex() {
        return handIndex;
    }

    public void setHandIndex(int handIndex) {
        this.handIndex = handIndex;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    public boolean isValid() {
        return handIndex >= 0;
    }

    @Override
    public String toString() {
        return "PlaceCardMessage{" + "handIndex=" + handIndex + ", boardIndex=" + boardIndex + '}';
    }
}
