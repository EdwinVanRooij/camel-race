package io.github.edwinvanrooij.camelraceshared.domain;

/**
 * Created by eddy
 * on 5/31/17.
 */
public class SideCard extends Card {
    private int position;
    private boolean isVisible;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public SideCard(CardType cardType, CardValue value, int position) {
        super(cardType, value);
        this.position = position;
        this.isVisible = false;
    }

    @Override
    public String toString() {
        if (isVisible) {
            return String.format("SideCard: %s of %s, turned around!", cardValue, cardType);
        }
        return String.format("SideCard: %s of %s, invisible.", cardValue, cardType);
    }
}
