package io.github.edwinvanrooij.domain.mexican;

import io.github.edwinvanrooij.domain.Player;

import java.util.List;

/**
 * Created by eddy
 * on 8/24/17.
 */
public class MexicanGameResults {
    private int stake;
    private Player loser;
    private List<PlayerResultItem> playerResultItems;

    public int getStake() {
        return stake;
    }

    public void setStake(int stake) {
        this.stake = stake;
    }

    public Player getLoser() {
        return loser;
    }

    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public List<PlayerResultItem> getPlayerResultItems() {
        return playerResultItems;
    }

    public void setPlayerResultItems(List<PlayerResultItem> playerResultItems) {
        this.playerResultItems = playerResultItems;
    }

    public MexicanGameResults(int stake, Player loser, List<PlayerResultItem> playerResultItems) {
        this.stake = stake;
        this.loser = loser;
        this.playerResultItems = playerResultItems;
    }
}
