package io.github.edwinvanrooij.domain.engine;

import io.github.edwinvanrooij.domain.Bid;
import io.github.edwinvanrooij.domain.Player;

/**
 * Created by eddy
 * on 6/11/17.
 */
public class ResultItem {
    private Player player;
    private Bid bid;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public ResultItem(Player player, Bid bid) {
        this.player = player;
        this.bid = bid;
    }
}
