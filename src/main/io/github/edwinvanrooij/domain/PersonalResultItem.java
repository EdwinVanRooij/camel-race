package io.github.edwinvanrooij.domain;


import io.github.edwinvanrooij.domain.camelrace.Bid;

/**
 * Created by eddy
 * on 7/17/17.
 */
public class PersonalResultItem {
    private Bid bid;
    private boolean won;

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public PersonalResultItem(Bid bid, boolean won) {
        this.bid = bid;
        this.won = won;
    }
}
