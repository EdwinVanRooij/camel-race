package io.github.edwinvanrooij;

import java.util.List;

/**
 * Created by eddy
 * on 5/31/17.
 */
public class GameState {
    private List<SideCard> sideCardList;
    private List<Camel> camelList;
    private List<Card> deck;
    private Card lastPickedCard;

    public List<SideCard> getSideCardList() {
        return sideCardList;
    }

    public void setSideCardList(List<SideCard> sideCardList) {
        this.sideCardList = sideCardList;
    }

    public List<Camel> getCamelList() {
        return camelList;
    }

    public void setCamelList(List<Camel> camelList) {
        this.camelList = camelList;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public Card getLastPickedCard() {
        return lastPickedCard;
    }

    public void setLastPickedCard(Card lastPickedCard) {
        this.lastPickedCard = lastPickedCard;
    }

    public GameState(List<SideCard> sideCardList, List<Camel> camelList, List<Card> deck, Card lastPickedCard) {
        this.sideCardList = sideCardList;
        this.camelList = camelList;
        this.deck = deck;
        this.lastPickedCard = lastPickedCard;
    }
}
