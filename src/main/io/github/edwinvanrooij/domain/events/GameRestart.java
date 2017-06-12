package io.github.edwinvanrooij.domain.events;

/**
 * Created by eddy
 * on 6/12/17.
 */
public class GameRestart {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameRestart(String gameId) {
        this.id = gameId;
    }
}
