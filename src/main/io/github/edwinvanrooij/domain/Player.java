package io.github.edwinvanrooij.domain;

import javax.websocket.Session;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Player {
    private String name;
    private Session session;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Player(String name, Session session) {
        this.name = name;
        this.session = session;
    }
}
