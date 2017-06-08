package io.github.edwinvanrooij.domain;

import io.github.edwinvanrooij.domain.Player;

/**
 * Created by eddy
 * on 6/7/17.
 */
public class PlayerJoinRequest {
    private String gameId;
    private Player player;

    @Override
    public String toString() {
        return "PlayerJoinRequest{" +
                "gameId='" + gameId + '\'' +
                ", player=" + player +
                '}';
    }

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

    public PlayerJoinRequest(String gameId, Player player) {
        this.gameId = gameId;
        this.player = player;
    }
}
