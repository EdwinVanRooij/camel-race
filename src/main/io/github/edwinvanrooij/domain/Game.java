package io.github.edwinvanrooij.domain;

import io.github.edwinvanrooij.domain.engine.Camel;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Game {
    private static AtomicInteger nextId = new AtomicInteger();

    private HashMap<Player, Bid> bids;
    private String id;
    private List<Player> players;

    private Map<String, String> funMap = new HashMap<>();

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

        funMap.put("rik", "LIMONCEEEELLLOO");
        funMap.put("tom", "ALLAHU AKBAR");
        funMap.put("tommeh", "ALLAHU AKBAR");
        funMap.put("yoeri", "Bob");
        funMap.put("bob", "Bobbeh");
        funMap.put("fons", "Der Foenzel");
        funMap.put("edwin", "Der Eddymeister");
        funMap.put("lars", "Jongens mag ik een emmer?");
        funMap.put("dennis", "Ja doe mij maar skere wodka");
    }

    public void newBid(Player player, Bid bid) {
        bids.put(player, bid);
    }

    private String funName(String string) {
        if (funMap.get(string) != null) {
            return funMap.get(string);
        }
        return string;
    }

    public Player addPlayer(Player player) {
        int uniqueId = nextId.incrementAndGet();

        Player playerWithId = new Player(uniqueId, funName(player.getName()));

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

    @Override
    public String toString() {
        return "Game{" +
                "bids=" + bids +
                ", id='" + id + '\'' +
                ", players=" + players +
                '}';
    }
}
