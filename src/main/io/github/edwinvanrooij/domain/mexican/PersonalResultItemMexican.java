package io.github.edwinvanrooij.domain.mexican;

/**
 * Created by eddy
 * on 8/26/17.
 */
public class PersonalResultItemMexican {
    private boolean loser;
    private int stake;

    public boolean isLoser() {
        return loser;
    }

    public void setLoser(boolean loser) {
        this.loser = loser;
    }

    public int getStake() {
        return stake;
    }

    public void setStake(int stake) {
        this.stake = stake;
    }

    public PersonalResultItemMexican(boolean loser, int stake) {
        this.loser = loser;
        this.stake = stake;
    }
}
