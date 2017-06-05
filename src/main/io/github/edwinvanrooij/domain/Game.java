package io.github.edwinvanrooij.domain;

import java.util.List;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Game {
    private String id;
    private List<Player> players;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Game(String id) {
        this.id = id;
        this.players = players;
    }
}
