package io.github.edwinvanrooij.domain.mexican;

import io.github.edwinvanrooij.domain.Player;

/**
 * Created by eddy
 * on 8/22/17.
 */
public class PlayerTableRowItem {
    private Player player;
    private int score;
    private int throwsLeft;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getThrowsLeft() {
        return throwsLeft;
    }

    public void setThrowsLeft(int throwsLeft) {
        this.throwsLeft = throwsLeft;
    }

    public PlayerTableRowItem(Player player, int score, int throwsLeft) {
        this.player = player;
        this.score = score;
        this.throwsLeft = throwsLeft;
    }
}
