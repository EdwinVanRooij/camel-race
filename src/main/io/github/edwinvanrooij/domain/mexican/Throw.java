package io.github.edwinvanrooij.domain.mexican;

/**
 * Created by eddy
 * on 8/17/17.
 */
public class Throw {
    private int firstScore;
    private int secondScore;
    private int score;

    public int getFirstScore() {
        return firstScore;
    }

    public void setFirstScore(int firstScore) {
        this.firstScore = firstScore;
    }

    public int getSecondScore() {
        return secondScore;
    }

    public void setSecondScore(int secondScore) {
        this.secondScore = secondScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Throw(int firstScore, int secondScore, int score) {
        this.firstScore = firstScore;
        this.secondScore = secondScore;
        this.score = score;
    }
}
