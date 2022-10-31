package de.etgramli.battlebros.model.card.effect;

public enum EffectType {
    FLIP_FACE_DOWN,             // Set card face down
    FLIP_FACE_UP,               // Set card face up
    PREVENT_FLIP,               // Prevent cards from being flipped face down
    SET_VALUE,                  // Set value of this/other card
    MODIFY_VALUE,               // Modify value
    FIXED_VALUE,                // Prevents value change of Bro
    REVIVE,                     // Place card from graveyard
    DRAW_CARD,                  // Draw card
    RESTRICT_CARD_PLACEMENT,    // Restrict card placement
    RETURN_CARD,                // Return card to player's hand
    DISABLE_EFFECT;             // Disable other effect
}
