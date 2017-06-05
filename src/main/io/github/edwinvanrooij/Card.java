package io.github.edwinvanrooij;

/**
 * Created by eddy
 * on 5/31/17.
 */
public class Card {
    protected CardType cardType;
    protected CardValue cardValue;

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public void setCardValue(CardValue cardValue) {
        this.cardValue = cardValue;
    }

    public Card(CardType cardType, CardValue cardValue) {
        this.cardType = cardType;
        this.cardValue = cardValue;
    }

    @Override
    public String toString() {
        return String.format("%s of %s", cardValue, cardType);
    }

    public enum CardValue {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
    }

    public enum CardType {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES,
    }
}
