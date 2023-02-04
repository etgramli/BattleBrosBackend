# Battle Bros
This is an implementation of the game logic of the awesome game Battle Bros.

[![Java build with Maven](https://github.com/etgramli/BattleBrosBackend/actions/workflows/maven.yml/badge.svg)](https://github.com/etgramli/BattleBrosBackend/actions/workflows/maven.yml)

## ToDo
- Logo
- Select pre-built decks
- Deck builder
- In-Game turn clock (~1 minute per action), game loss when it runs out
- User accounts
  - Unlock cards by playing games; Count progression/number of copies for each card (from 0 to 10)
  - unlockable versions of cards with visual effect and voice line when played
  - preferred color (for own name in chat)
  - Profile picture (upload a picture OR choose a battle bro artwork)
  - Resume-able, asynchronous games
  - maybe: Automatic email, sent when you can take an action in an ongoing asynchronous game, or when you get a message
  - friend list and friend-specific messaging outside of games
  - Viewable player profile with name, profile picture, number of wins/losses, won tournaments (little trophy icons), cards in collection, friend list
- Additional in-game features
  - chat in the game lobby, where everyone can talk to everyone
  - list of every player currently online, with options like "send friend request", "send a message", "invite to a game", "view profile"
  - list of every friend (even if they're not currently online)
  - when you write the name of a card in chat, it becomes highlighted text that can be hovered over in order to view the referenced card.
  - maybe: bi-weekly or monthly tournaments/leagues, where you play in a ladder and the winner gets a big prize (20 random cards, a unique flair or profile picture, or something)
  - maybe: tournament structure: each day there a scheduled tournament games, which can be played at any time. Games will be best-of-3 and single elminiation. If players don't play during the scheduled day, it counts as a loss.
  - chat with opponent
  - in-game log with all taken game actions and hoverable card names
  - large image of last hovered card, with written out name, value, element & ability text underneath
  - card specific rules FAQs, viewable by right-clicking a card
  - making display of these additional features optional/toggleable
  - maybe: save replays which can be viewed at any time (replay contains information of players, every taken game action, and every sent chat message)
  - maybe: toggleable soundtrack (unique songs for main page/lobby, deckbuilding/profile, game)
  - maybe: daily challenges (play with a deck containing at least 15 cards of this element, play with a deck containing these 3 cards, play a game and pass on your first turn)
- Single Player content
  - play against AI (maybe fighting game style story mode with dialogue between games)
  - solve puzzles
  - gain card rewards for completing single player content for the first time
- Additional links
  - page with rules explanation (same explanation as on the physical rules card)
  - maybe: playable rules tutorial
  - link to purchasable physical cards
  - main page with information about daily quests (play up to 5 games to get cards), card of the day, change log, and upcoming changes/maintenance
- Translation
  - translate texts to english and add function to change language on the website
  - translate card images
  - maybe: more languages, e.g. french
- Prettification
  - images of cards your opponent plays load slow -> preload all cards in background or something
  - automatically resize cards/gamefield etc. to browser size
  - show changed power values and negated abilities on cards in play (red number over printed power value / red X over ability box)
  - highlight zones on the gamefield, where cards can be played
  - highlight zones where it is forbidden to play cards
  - highlight cards in hand, that can/cannot be played (either or)
  - highlight cards that are allowed to be selected (when you're resolving an ability which requires you to choose a card)
  - make it possible to sort your hand
  - highlight field and other information of the player, who currently has to take a game action
  - use icons for showing amount of cards/viewing cards in: discard, deck, life zone, opponent's hand
  - use animations for: playing cards, discarding cards, flipping cards face up/down, drawing cards, sorting hand, passing, ending the battle, ending the game
  - use sfx for the actions listed above as well as receiving chat messages
  - add a light and a dark theme for the whole website

## Card Unlocking System
- Each day, an account gets one card as a reward for each of the first 5 games they played that day.
- When you lose a game, you get one random card, when you win you get to choose one of three random cards to add to your collection.
- You can have multiple (up to 10) copies of cards you already own.
- You only need a single copy of a card to use in as many decks as you want.
- Excess copies unlock cosmetic uppgrades: 5 copies unlock a profile picture of the card art; 10 copies unlock a special version of that card with visual fx and a voice line
- You can NOT trade cards with other users
- Standard profile picutures that are automatically unlock might include: symbols of the elements, the battle bros logo in different colors, etc.
- Each random card has these probabilities determining what card it is:
  - 1/3: this card is a random card you don't have any copies of (when you already have a card of each type, this is replaced with the next category)
  - 1/3: this card is one of the cards you or your opponent had in their decks this game
  - 1/3: this card is a completely random card
  - cards that you already have 10 copies of will never show up again

## Rules
This is a game for two players, each having their own deck of 20 cards.

This game consists of multiple rounds - or battles - in which you try to amass a higher total value on your side of the field than the opponent.
The first player to win 3 battles against their opponent wins the game.

### Cards
Each card has a power value and an element, most cards have an ability.

Abilities may change power values, prohibit effects, flip cards face down or do other things.
An ability may affect the card itself, neighboring cards (left, right), the opposite facing card, diagonally facing cards, cards of a specific element or just any card the player chooses.

Cards can only be placed to empty positions, that directly connect to an already played card (but not digonally).

### Game modes
- Constructed decks (Each player plays with a deck they built with cards from their own collection)
- Draft (4 cards are reveiled, player A chooses one of them for their deck, then player B chooses two of the remaining cards, then player A gets the last card. Repeat until both players have a full deck.)

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

| Incoming URL | Description                             |
|--------------|-----------------------------------------|
| /hostgame    | Create new game                         |
| /joingame    | Join an existing game                   |
| /placecard   | Place card and triggers board update    |
| /pass        | Pass round - next player's turn         |
| /showgames   | Manually reload games list              |
| /selectcard  | Select a card in hand/in play/discarded |

### User-Topics
All the following urls must be subscribed in the client with a preceding "/user" to get automatic updates.

| URL                 | Purpose                                                                         |
|---------------------|---------------------------------------------------------------------------------|
| /topic/hand         | The player's hand cards                                                         |
| /topic/board        | Board state                                                                     |
| /topic/strength     | Current strength of each player's cards                                         |
| /topic/lifecards    | Amount of life cards                                                            |
| /topic/names        | Names of the players of the current game                                        |
| /topic/activeplayer | Index of the active player (which player's turn)                                |
| /topic/showgames    | List of open games (which you can join)                                         |
| /topic/joingame     | Returns the index of the players after the 2nd player joined                    |
| /topic/passed       | Returns a list of Booleans that says which players passed                       |
| /topic/selectcard   | Send request from the backend to the frontend to ask the user  to select a card |