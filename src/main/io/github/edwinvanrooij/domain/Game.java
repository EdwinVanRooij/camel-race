package io.github.edwinvanrooij.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.edwinvanrooij.domain.engine.*;
import io.github.edwinvanrooij.domain.events.GameStart;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Game {

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

    private static AtomicInteger nextId = new AtomicInteger();

    private HashMap<Integer, Bid> bids;
    private String id;
    private List<Player> players;

    private List<SideCard> sideCardList;
    private List<Camel> camelList;
    private List<Card> deck;

    private Card lastPickedCard;
    private Camel winner;
    private boolean gameEnded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Game(String id) {
        this.id = id;
        players = new ArrayList<>();
        bids = new HashMap<>();

        sideCardList = new ArrayList<>();
        camelList = new ArrayList<>();
        deck = new ArrayList<>();
        init();
    }

    public Game() {
        players = new ArrayList<>();
        bids = new HashMap<>();

        sideCardList = new ArrayList<>();
        camelList = new ArrayList<>();
        deck = new ArrayList<>();
        init();
    }

    public void newBid(Player player, Bid bid) {
        bids.put(player.getId(), bid);
    }

    private String funName(String string) {
        if (funMap.get(string) != null) {
            return funMap.get(string);
        }
        return string;
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

    @Override
    public String toString() {
        return "Game{" +
                "bids=" + bids +
                ", id='" + id + '\'' +
                ", players=" + players +
                '}';
    }

    public void restart() {
        sideCardList.clear();
        camelList.clear();
        deck.clear();
        lastPickedCard = null;
        winner = null;
        gameEnded = false;

        init();
    }

    private void init() {
        // Create and add camels to list
        Camel clubsCamel = new Camel(CardType.CLUBS, 0);
        Camel diamondsCamel = new Camel(CardType.DIAMONDS, 1);
        Camel heartsCamel = new Camel(CardType.HEARTS, 2);
        Camel spadesCamel = new Camel(CardType.SPADES, 3);
        camelList.add(clubsCamel);
        camelList.add(diamondsCamel);
        camelList.add(heartsCamel);
        camelList.add(spadesCamel);

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

        // Take 4 random cards from the deck, fill in side cards
        for (int i = 1; i < 5; i++) {
            Card randomCard = takeRandomCardFromDeck(deck);
            SideCard card = new SideCard(randomCard.getCardType(), randomCard.getCardValue(), i);
            sideCardList.add(card);
        }
    }

    public void nextRound() throws Exception {
        lastPickedCard = takeRandomCardFromDeck(deck);

        moveCamelsAccordingToCard(lastPickedCard, true);
    }

    private void moveCamelsAccordingToCard(Card card, boolean forward) throws Exception {
        // Get the camel that matches this card
        CardType cardType = card.getCardType();
        Camel camel = getCamelByCardType(cardType);

        moveCamel(camel, forward);
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

        handleSideCard(position);
    }

    private void handleSideCard(int position) throws Exception {

        // Find card at this position
        for (SideCard card : sideCardList) {
            if (card.getPosition() == position) {

                // Check if card was turned around yet
                if (card.isVisible()) {
                    // Card is visible, do nothing to it
                    return;
                }

                // Card is invisible
                for (Camel c : camelList) {
                    if (c.getPosition() < position) {
                        // There's a card below this position, don't turn around
                        return;
                    }
                }

                // All camels have passed this position or are on it, move card around
                card.setVisible(true);

                moveCamelsAccordingToCard(card, false);

                // Don't iterate over other positions
                return;
            }
        }
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

        System.out.println(String.format("There's %s players in this game, game id %s, map now contains", players.size(), id ));

        return results;
    }

    private Card takeRandomCardFromDeck(List<Card> deck) {
        int randomNumber = new Random().nextInt(deck.size());

        // Get a non-empty card
        Card pickedOutCard;
        do {
            pickedOutCard = deck.get(randomNumber);
        } while (pickedOutCard == null);

        deck.remove(pickedOutCard);
        return pickedOutCard;
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
