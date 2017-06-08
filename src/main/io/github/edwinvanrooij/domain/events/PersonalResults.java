package io.github.edwinvanrooij.domain.events;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class PersonalResults {
    int rank;
    int bid;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public PersonalResults(int rank, int bid) {
        this.rank = rank;
        this.bid = bid;
    }
}
