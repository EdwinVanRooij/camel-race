package io.github.edwinvanrooij.domain.mexican;

import io.github.edwinvanrooij.domain.Player;

import java.util.List;

/**
 * Created by eddy
 * on 8/22/17.
 */
public class MexicanGameState {
    private int gameModeOrdinal;
    private int stake;
    private List<PlayerTableRowItem> players;

    public int getGameModeOrdinal() {
        return gameModeOrdinal;
    }

    public void setGameModeOrdinal(int gameModeOrdinal) {
        this.gameModeOrdinal = gameModeOrdinal;
    }

    public int getStake() {
        return stake;
    }

    public void setStake(int stake) {
        this.stake = stake;
    }

    public List<PlayerTableRowItem> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerTableRowItem> players) {
        this.players = players;
    }

    public MexicanGameState(int gameModeOrdinal, int stake, List<PlayerTableRowItem> players) {
        this.gameModeOrdinal = gameModeOrdinal;
        this.stake = stake;
        this.players = players;
    }
}
