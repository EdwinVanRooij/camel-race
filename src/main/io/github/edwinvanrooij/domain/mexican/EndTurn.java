package io.github.edwinvanrooij.domain.mexican;

/**
 * Created by eddy
 * on 8/24/17.
 */
public class EndTurn {
    private String gameId;
    private int playerId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public EndTurn(String gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
