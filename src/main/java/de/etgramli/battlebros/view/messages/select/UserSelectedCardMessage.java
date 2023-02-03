package de.etgramli.battlebros.view.messages.select;

/**
 * Message expected as a response to SelectCardMessage.
 */
public record UserSelectedCardMessage(int playerIndex,
                                      SelectCardType type,
                                      int selectedCardIndex,
                                      boolean opponentCard  // True if the card is one of the opponent's played cards
) {}
