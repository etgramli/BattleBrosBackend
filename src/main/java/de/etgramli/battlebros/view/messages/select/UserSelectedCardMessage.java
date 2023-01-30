package de.etgramli.battlebros.view.messages.select;

/**
 * Message expected as a response to SelectCardMessage.
 */
public record UserSelectedCardMessage(int playerIndex, SelectType type, int selectedCardIndex) {
}
