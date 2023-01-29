# BattleBros
This is an implementation of the game logic of the awesome game BattleBros.

## ToDo
- Logo
- Select pre-built decks
- Deck builder
- In-Game turn clock (~1 minute per action), game loss when it runs out
- User accounts
  - Unlock cards after a period of time
  - Number of wins/losses
  - Profile picture (upload a picture OR choose a battle bro artwork)
  - Resume-able, asynchronous games
  - friend list and chat outside of games
  - unlockable versions of cards with visual effect and voice line when played
- Additional in-game features
  - chat with opponent
  - log with all taken game actions and hoverable card names
  - large image of last hovered card, with written out name, value, element & ability text underneath
  - card specific rules FAQs, viewable by right-clicking a card
  - making display of these additional features optional/toggleable
- Single Player content
  - play against AI
  - solve puzzles
- Additional links
  - page with rules explanation
  - maybe even playable rules tutorial
  - link to purchasable physical cards

## Rules
This is a game for two players, each having a deck of 12 cards.

One game consists of up to 3 rounds and one who won 2 of the 3 rounds is the final winner.

### Cards
Each card has a value and an element attached, some have effects.

Effects may change values, prohibit effect or flip cards.
An effect can affect the card itself, neighbor cards (left, right), the facing card or cards of a specific element.

Cards can only be placed at empty positions, that directly connects to an already played card (but not digonally).

### Game modes
- Pre-built decks
- Drafting (6 rounds à 4 cards -> 2 deck of size 12)

### Specific rulings
Here are specific rules that occur rarely, but then are very important.

#### May I play a card with an effect, that would create a contradicting loop of effects?
- ?

## WebSocket communication
An overview on the URLs used to send/receive messages to create and update game instances.

### Important STOMP URLs
| Purpose             | URL    |
|---------------------|--------|
| User-specific Queue | /user  |
| App prefix          | /app   |
| Broker URL          | /topic |

### Messages
The server listens to the following URLs for messages from the web client to the described actions. All of them trigger
at least one response on the URL(s) mentioned in "User-Topics".

| Incoming URL | Description                          |
|--------------|--------------------------------------|
| /hostgame    | Create new game                      |
| /joingame    | Join an existing game                |
| /placecard   | Place card and triggers board update |
| /pass        | Pass round - next player's turn      |
| /showgames   | Manually reload games list           |

### User-Topics
All the following urls must be subscribed in the client with a preceding "/user" to get automatic updates.

| URL                 | Purpose                                                      |
|---------------------|--------------------------------------------------------------|
| /topic/hand         | The player's hand cards                                      |
| /topic/board        | Board state                                                  |
| /topic/strength     | Current strength of each player's cards                      |
| /topic/lifecards    | Amount of life cards                                         |
| /topic/names        | Names of the players of the current game                     |
| /topic/activeplayer | Index of the active player (which player's turn)             |
| /topic/showgames    | List of open games (which you can join)                      |
| /topic/joingame     | Returns the index of the players after the 2nd player joined |
| /topic/passed       | Returns a list of Booleans that says which players passed    |
