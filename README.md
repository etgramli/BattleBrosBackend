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

### Game modes
- Pre-built decks
- Drafting (6 rounds à 4 cards -> 2 deck of size 12)

### Specific rulings
Here are specific rules that occur rarely, but then are very important.

#### May I play a card with an effect, that would create a contradicting loop of effects?
- ?

## WebSocket communication
The table shows the server's URLs for incoming messages and which URLs get updated due to the changed state (by that incoming messages).

| Action          | Incoming URL | Triggers update to subscribing URL | Description                             |
|-----------------|--------------|------------------------------------|-----------------------------------------|
| New Game        | /newgame     |                                    | Create new game                         |
| Join Game       | /joingame    |                                    | Join an existing game                   |
| Play card       | /placecard   | /topic/board                       | Place card and triggers board update    |
| Fold            | /fold        |                                    | Fold round - next player's turn         |

### Important STOMP URLs
| Purpose             | URL    |
|---------------------|--------|
| User-specific Queue | /user  |
| App prefix          | /app   |
| Broker URL          | /topic |

#### User-Topics
| Purpose      | URL                     |
|--------------|-------------------------|
| /topic/hand  | The player's hand cards |
| /topic/board | Board state             |
