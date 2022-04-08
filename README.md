# BattleBros
This is an implementation of the game logic of the awesome game BattleBros.

## Rules
This is a game for two players, each having a deck of 12 cards.

One game consists of up to 3 rounds and one who won 2 of the 3 rounds is the final winner.

### Cards
Each card has a value and an element attached, some have effects.

Effects may change values, prohibit effect or flip cards.
An effect can affect the card itself, neighbor cards (left, right), the facing card or cards of a specific element.

Cards can only be placed at empty posisions, that directly connects to an already played card (but not digonally).

### Specific rulings
