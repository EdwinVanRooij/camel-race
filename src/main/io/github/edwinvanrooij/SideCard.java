package io.github.edwinvanrooij;

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

}
