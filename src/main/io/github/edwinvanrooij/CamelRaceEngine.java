package io.github.edwinvanrooij;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by eddy
 * on 5/31/17.
 */
public class CamelRaceEngine {

    private List<SideCard> sideCardList;
    private List<Camel> camelList;
    private List<Card> deck;

    private Card lastPickedCard;
    private Camel winner;
    private boolean gameEnded;

    public CamelRaceEngine() {
        sideCardList = new ArrayList<>();
        camelList = new ArrayList<>();
        deck = new ArrayList<>();
        init();
    }

    private void init() {
        // Create and add camels to list
        Camel clubsCamel = new Camel(Card.CardType.CLUBS, 0);
        Camel diamondsCamel = new Camel(Card.CardType.DIAMONDS, 1);
        Camel heartsCamel = new Camel(Card.CardType.HEARTS, 2);
        Camel spadesCamel = new Camel(Card.CardType.SPADES, 3);
        camelList.add(clubsCamel);
        camelList.add(diamondsCamel);
        camelList.add(heartsCamel);
        camelList.add(spadesCamel);

        // Create and fill the remaining deck
        List<Card> clubsDeck = new ArrayList<>();
        List<Card> diamondsDeck = new ArrayList<>();
        List<Card> heartsDeck = new ArrayList<>();
        List<Card> spadesDeck = new ArrayList<>();

        for (Card.CardValue v : Card.CardValue.values()) {
            // Don't add the ace to the (remaining) deck
            if (v == Card.CardValue.ACE)
                continue;

            clubsDeck.add(new Card(Card.CardType.CLUBS, v));
            diamondsDeck.add(new Card(Card.CardType.DIAMONDS, v));
            heartsDeck.add(new Card(Card.CardType.HEARTS, v));
            spadesDeck.add(new Card(Card.CardType.SPADES, v));
        }

        // Add all small decks to the total
        deck.addAll(clubsDeck);
        deck.addAll(diamondsDeck);
        deck.addAll(heartsDeck);
        deck.addAll(spadesDeck);

        // Take 4 random cards from the deck, fill in side cards
        for (int i = 0; i < 4; i++) {
            Card randomCard = takeRandomCardFromDeck(deck);
            SideCard card = new SideCard(randomCard.getCardType(), randomCard.getCardValue(), i);
            sideCardList.add(card);
        }
    }

    public void nextRound() throws Exception {
        lastPickedCard = takeRandomCardFromDeck(deck);

        // Get the camel that matches this card
        Card.CardType cardType = lastPickedCard.getCardType();
        Camel camel = getCamelByCardType(cardType);

        // Get camel position
        int position = camel.getPosition();
        // Put camel one position further
        position++;

        // If camel is now past the total size of side cards, it wins the game
        if (position > sideCardList.size()) {
            gameEnded = true;
            winner = camel;
        }

        // Set a new position for this camel
        camel.setPosition(++position);
    }

    private Camel getCamelByCardType(Card.CardType type) throws Exception {
        for (Camel c : camelList) {
            if (c.getCardType() == c.getCardType()){
                return c;
            }
        }
        throw new Exception(String.format("Camel with type %s not found!", type.toString()));
    }

    public GameState generateGameState() {
        return new GameState(sideCardList, camelList, deck, lastPickedCard);
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
