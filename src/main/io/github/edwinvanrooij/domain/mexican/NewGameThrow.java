package io.github.edwinvanrooij.domain.mexican;

/**
 * Created by eddy
 * on 8/24/17.
 */
public class NewGameThrow {
    private Throw newThrow;
    private MexicanGameState gameState;

    public Throw getNewThrow() {
        return newThrow;
    }

    public void setNewThrow(Throw newThrow) {
        this.newThrow = newThrow;
    }

    public MexicanGameState getGameState() {
        return gameState;
    }

    public void setGameState(MexicanGameState gameState) {
        this.gameState = gameState;
    }

    public NewGameThrow(Throw newThrow, MexicanGameState gameState) {
        this.newThrow = newThrow;
        this.gameState = gameState;
    }
}
