package io.github.edwinvanrooij;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddy
 * on 5/31/17.
 */
public class CamelRaceEngine {

    private List<SideCard> sideCardList;
    private List<Camel> camelList;
    private List<Card> deck;

    public CamelRaceEngine() {
        sideCardList = new ArrayList<>();
        camelList = new ArrayList<>();
        deck = new ArrayList<>();
        init();
    }

    private void init() {
        Camel clubsCamel = new Camel(Card.CardType.CLUBS, 0);
        Camel diamondsCamel = new Camel(Card.CardType.DIAMONDS, 1);
        Camel heartsCamel = new Camel(Card.CardType.HEARTS, 2);
        Camel spadesCamel = new Camel(Card.CardType.SPADES, 3);

        camelList.add(clubsCamel);
        camelList.add(diamondsCamel);
        camelList.add(heartsCamel);
        camelList.add(spadesCamel);

        // todo; fill in sidecards
        // todo; fill in deck
    }

    public void newRound() {

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
