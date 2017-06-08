package io.github.edwinvanrooij.domain;

import io.github.edwinvanrooij.domain.engine.Camel;

import javax.websocket.Session;
import java.util.*;

/**
 * Created by eddy on 6/8/17.
 */
public class GameManager {
    private List<Game> games;

    private Map<String, Session> gameSessionMap;
    private Map<Integer, Session> playerSessionMap;

    public GameManager() {
        games = new ArrayList<>();
        gameSessionMap = new HashMap<>();
    }

    public Game createGame(Session session) {
        String id = generateUniqueId();
        Game game = new Game(id, session);
        games.add(game);
        gameSessionMap.put(game.getId(), session);
        System.out.println(String.format("Created game: %s (in createGame in manager)", game));
        return game;
    }

    private String generateUniqueId() {
        String id;
        do {
            id = generateId();
        } while (gameWithIdExists(id));

        return id;
    }

    private String generateId() {
        char[] vowels = "aeiouy".toCharArray(); // klinkers
        char[] consonants = "bcdfghjklmnpqrstvwxz".toCharArray(); // medeklinkers

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char c;
            if (i % 2 == 1) {
                c = vowels[random.nextInt(vowels.length)];
            } else {
                c = consonants[random.nextInt(consonants.length)];
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public Session getSessionByGameId(String id) {
        return gameSessionMap.get(id);
    }
    public Session getSessionByPlayerId(int id) {
        return playerSessionMap.get(id);
    }
    public Game getGameById(String id) {
        for (Game game : games) {
            if (Objects.equals(game.getId(), id)) {
                System.out.println(String.format("getGameById returns %s", game));
                return game;
            }
        }
        return null;
    }

    private boolean gameWithIdExists(String id) {
        Game game = getGameById(id);
        return game != null;
    }

    public Player playerJoin(String gameId, Player player, Session session) {
        Game game = getGameById(gameId);

        if (game == null) {
            return null;
        }

        playerSessionMap.put(player.getId(), session);
        return game.addPlayer(player);
    }


    public boolean playerNewBid(String gameId, Player player, Camel camel, int value) {
        Game game = getGameById(gameId);

        if (game == null) {
            return false;
        }

        Player playerWithAttributes = game.getPlayer(player.getId());

        if (playerWithAttributes == null) {
            return false;
        }

        game.newBid(player, camel, value);
        return true;
    }
}
