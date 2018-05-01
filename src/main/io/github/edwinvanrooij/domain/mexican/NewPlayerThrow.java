package io.github.edwinvanrooij.domain.mexican;

import io.github.edwinvanrooij.domain.Player;

/**
 * Created by eddy
 * on 8/23/17.
 */
public class NewPlayerThrow {
    private String gameId;
    private Player player;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public NewPlayerThrow(String gameId, Player player) {
        this.gameId = gameId;
        this.player = player;
    }
}
