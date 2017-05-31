package io.github.edwinvanrooij;

/**
 * Created by eddy
 * on 5/31/17.
 */
public class Camel extends Card {
    private int row;
    private int position;

    public Camel(CardType cardType, int row) {
        super(cardType, CardValue.ACE); // A camel is always an ace
        this.row = row;
        this.position = 0;
    }
}
