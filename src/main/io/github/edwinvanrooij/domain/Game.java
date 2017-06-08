package io.github.edwinvanrooij.domain;

import io.github.edwinvanrooij.domain.engine.Camel;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Game {
    private static AtomicInteger nextId;

    private HashMap<Player, Bid> bids;
    private String id;
    private List<Player> players;
    private Session session;

    public Session getSession() {
        return session;
    }

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

    public Game(String id, Session session) {
        this.id = id;
        players = new ArrayList<>();
        bids = new HashMap<>();
    }

    public void newBid(Player player, Camel camel, int value) {
        bids.put(player, new Bid(camel, value));
    }

    public Player addPlayer(Player player, Session session) {
        int uniqueId = nextId.incrementAndGet();
        Player playerWithId = new Player(uniqueId, player.getName(), session);
        players.add(playerWithId);
        return playerWithId;
    }

    public Player getPlayer(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }
}
