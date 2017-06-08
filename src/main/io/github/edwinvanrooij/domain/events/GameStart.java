package io.github.edwinvanrooij.domain.events;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class GameStart {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameStart(String id) {
        this.id = id;
    }
}
