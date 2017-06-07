package io.github.edwinvanrooij.domain;

import javax.websocket.Session;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Player {
    private int id;
    private String name;
    private Session session;

    public int getId() {
        return id;
    }

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

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", session=" + session +
                '}';
    }

    public Player(int id, String name, Session session) {
        this.id = id;
        this.name = name;
        this.session = session;
    }
}
