package io.github.edwinvanrooij.camelraceshared.domain;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Game {

    // region Funmap
    private static transient Map<String, String> funMap = new HashMap<>();

    static {
        funMap.put("rik", "LIMONCEEEELLLOO");
        funMap.put("tom", "ALLAHU AKBAR");
        funMap.put("tommeh", "ALLAHU AKBAR");
        funMap.put("yoeri", "Bob");
        funMap.put("bob", "Bobbeh");
        funMap.put("fons", "Der Foenzel");
        funMap.put("edwin", "Der Eddymeister");
        funMap.put("lars", "Jongens mag ik een emmer?");
        funMap.put("dennis", "Ja doe mij maar skere wodka");
    }

    private String funName(String string) {
        if (funMap.get(string) != null) {
            return funMap.get(string);
        }
        return string;
    }
    // endregion

    private static AtomicInteger nextId = new AtomicInteger();

    private String id;
    private HashMap<Integer, Bid> bids; // player ID with bids
    private List<Player> players;

    private List<SideCard> sideCardList;
    private List<Camel> camelList;
    private List<Card> deck;

    private Card lastPickedCard;

    private boolean gameEnded;
    private Camel winner;

    private SideCard sideCardToTurn = null;

    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Game(String id) throws Exception {
        this.id = id;
        initVariables();
        setInitialState();
    }

    public void newBid(Player player, Bid bid) {
        bids.put(player.getId(), bid);
    }


    public Player addPlayer(Player player) {
        int uniqueId = nextId.incrementAndGet();

        Player playerWithId = new Player(uniqueId, funName(player.getName()));

        players.add(playerWithId);
        return playerWithId;
    }

    public Player getPlayer(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public void restart() throws Exception {
        sideCardList.clear();
        camelList.clear();
        deck.clear();
        lastPickedCard = null;
        winner = null;
        gameEnded = false;
        sideCardToTurn = null;

        setInitialState();
    }

    private void initVariables() {
        players = new ArrayList<>();
        bids = new HashMap<>();

        sideCardList = new ArrayList<>();
        camelList = new ArrayList<>();
        deck = new ArrayList<>();
    }

    private void addCamels() {
        // Create and add camels to list
        Camel clubsCamel = new Camel(CardType.CLUBS, 0);
        Camel diamondsCamel = new Camel(CardType.DIAMONDS, 1);
        Camel heartsCamel = new Camel(CardType.HEARTS, 2);
        Camel spadesCamel = new Camel(CardType.SPADES, 3);
        camelList.add(clubsCamel);
        camelList.add(diamondsCamel);
        camelList.add(heartsCamel);
        camelList.add(spadesCamel);
    }

    private void initDeck() {
        // Create and fill the remaining deck
        List<Card> clubsDeck = new ArrayList<>();
        List<Card> diamondsDeck = new ArrayList<>();
        List<Card> heartsDeck = new ArrayList<>();
        List<Card> spadesDeck = new ArrayList<>();

        for (CardValue v : CardValue.values()) {
            // Don't add the ace to the (remaining) deck
            if (v == CardValue.ACE)
                continue;

            clubsDeck.add(new Card(CardType.CLUBS, v));
            diamondsDeck.add(new Card(CardType.DIAMONDS, v));
            heartsDeck.add(new Card(CardType.HEARTS, v));
            spadesDeck.add(new Card(CardType.SPADES, v));
        }

        // Add all small decks to the total
        deck.addAll(clubsDeck);
        deck.addAll(diamondsDeck);
        deck.addAll(heartsDeck);
        deck.addAll(spadesDeck);
    }

    private void initSideCards() throws Exception {
        // Take 4 random cards from the deck, fill in side cards
        for (int i = 1; i < 5; i++) {
            Card randomCard = pickCard();
            SideCard card = new SideCard(randomCard.getCardType(), randomCard.getCardValue(), i);
            sideCardList.add(card);
        }
        lastPickedCard = null;
    }

    private void setInitialState() throws Exception {
        addCamels();
        initDeck();
        initSideCards();
    }

    public Card pickCard() throws Exception {
        int randomNumber = new Random().nextInt(deck.size());

        // Get a non-empty card
        lastPickedCard = null;
        do {
            lastPickedCard = deck.get(randomNumber);
        } while (lastPickedCard == null);

        deck.remove(lastPickedCard);

        return lastPickedCard;
    }

    public Camel didCamelWinYet() throws Exception {
        Camel camel = getCamelByCardType(lastPickedCard.getCardType());
        if (camel.getPosition() + 1 > sideCardList.size()) {
            winner = camel;
            gameEnded = true;
            return camel;
        }
        return null;
    }

    public void moveCamelAccordingToLastCard() throws Exception {
        // Get the camel that matches this card
        CardType cardType = lastPickedCard.getCardType();
        Camel camel = getCamelByCardType(cardType);
        moveCamel(camel, true);
    }

    private void setCamelPosition(Camel camel) {
        for (int i = 0; i < camelList.size(); i++) {
            if (camelList.get(i).getCardType() == camel.getCardType()) {
                camelList.set(i, camel);
            }
        }
    }

    public boolean shouldTurnSideCard() throws Exception {
        int position = getCamelByCardType(lastPickedCard.getCardType()).getPosition();

        // Find card at this position
        for (SideCard card : sideCardList) {
            if (card.getPosition() == position) {

                // Check if card was turned around yet
                if (card.isVisible()) {
                    // Card is visible, do nothing to it
                    return false;
                }

                // Card is invisible
                for (Camel c : camelList) {
                    if (c.getPosition() < position) {
                        // There's a card below this position, don't turn around
                        return false;
                    }
                }

                // All camels have passed this position or are on it, move card around
                card.setVisible(true);
                sideCardToTurn = card;
                return true;
            }
        }
        return false;
    }

    public List<Camel> newCamelList() throws Exception {
        // Get the camel that matches this card
        CardType cardType = sideCardToTurn.getCardType();
        Camel camel = getCamelByCardType(cardType);

        moveCamel(camel, false);
        return camelList;
    }

    private void moveCamel(Camel camel, boolean forward) throws Exception {
        // Get camel position
        int position = camel.getPosition();

        // Put camel one position according to forward or not
        if (forward) {
            position++;
        } else {
            position--;
        }

        // If camel is now past the total size of side cards, it wins the game
        if (position > sideCardList.size()) {
            gameEnded = true;
            winner = camel;
            return;
        }

        // Set a new position for this camel
        camel.setPosition(position);

        // Update real camelList
        setCamelPosition(camel);
    }

    private Camel getCamelByCardType(CardType type) throws Exception {
        for (Camel c : camelList) {
            if (c.getCardType() == type) {
                return c;
            }
        }
        throw new Exception(String.format("Camel with type %s not found!", type.toString()));
    }

    public GameState generateGameState() {
        return new GameState(sideCardList, camelList, deck, lastPickedCard, winner, gameEnded);
    }

    public GameResults generateGameResults() {
        GameResults results = new GameResults(winner.getCardType());
        System.out.println("Generating game results for winner " + winner.getCardType().toString());

        for (Player player : players) {
            ResultItem item = new ResultItem(player, bids.get(player.getId()));
            results.addResultItem(item);
        }

        System.out.println(String.format("There's %s players in this game, game id %s, map now contains", players.size(), id));

        return results;
    }

    public List<SideCard> getSideCardList() {
        return sideCardList;
    }

    public List<Camel> getCamelList() {
        return camelList;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public int getAmountOfCardsLeft() {
        return deck.size();
    }
}
