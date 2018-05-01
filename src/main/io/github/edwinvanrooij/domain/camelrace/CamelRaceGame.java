package io.github.edwinvanrooij.domain.camelrace;

//import io.github.edwinvanrooij.camelraceshared.lib.domain.*;
import io.github.edwinvanrooij.domain.Game;
import io.github.edwinvanrooij.domain.Player;
import io.github.edwinvanrooij.domain.ResultItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by eddy
 * on 7/24/17.
 */
public class CamelRaceGame extends Game {
    private HashMap<Integer, Bid> bids; // player ID with bids

    private List<SideCard> sideCardList;
    private List<Camel> camelList;
    private List<Card> deck;

    private Card lastPickedCard;

    private Camel winner;
    private boolean gameEnded;

    private SideCard sideCardToTurn = null;

    public CamelRaceGame(String id) throws Exception {
        super(id);
    }

    public Camel getWinner() {
        return winner;
    }

    public List<SideCard> getSideCardList() {
        return sideCardList;
    }

    public List<Camel> getCamelList() {
        return camelList;
    }

    public void newBid(Player player, Bid bid) {
        bids.put(player.getId(), bid);
    }

    public Bid getBid(int playerId) {
        try {
            return bids.get(playerId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void restart() throws Exception {
        super.restart();

        sideCardList.clear();
        camelList.clear();
        deck.clear();
        lastPickedCard = null;
        winner = null;
        gameEnded = false;
        sideCardToTurn = null;
    }

    @Override
    protected void initVariables() {
        super.initVariables();

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

    @Override
    protected void setInitialState() throws Exception {
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

    @Override
    public CamelRaceGameState generateGameState() {
        return new CamelRaceGameState(sideCardList, camelList, deck, lastPickedCard, winner, gameEnded);
    }

    @Override
    public GameResults generateGameResults() {
        GameResults results = new GameResults(winner.getCardType());

        for (Player player : players) {
            ResultItem item = new ResultItem(player, bids.get(player.getId()));
            if (item.getBid() != null) {
                results.addResultItem(item);
            }
        }

        return results;
    }
}
