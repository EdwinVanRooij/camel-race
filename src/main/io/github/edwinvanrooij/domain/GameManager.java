package io.github.edwinvanrooij.domain;


import io.github.edwinvanrooij.camelraceshared.domain.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.Game;
import io.github.edwinvanrooij.camelraceshared.domain.Player;

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
        playerSessionMap = new HashMap<>();
    }

    public Game createGame(Session session) throws Exception {
        String id = generateUniqueId();
        Game game = new Game(id);
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
        return gameSessionMap.get(id.toLowerCase());
    }

    public Session getSessionByPlayerId(int id) {
        return playerSessionMap.get(id);
    }

    public Game getGameById(String id) {
        for (Game game : games) {
            if (Objects.equals(game.getId().toLowerCase(), id.toLowerCase())) {
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

        Player playerWithId = game.addPlayer(player);
        playerSessionMap.put(playerWithId.getId(), session);
        return playerWithId;
    }


    public boolean playerNewBid(String gameId, Player player, Bid bid) {
        Game game = getGameById(gameId);

        if (game == null) {
            return false;
        }

        Player playerWithAttributes = game.getPlayer(player.getId());

        if (playerWithAttributes == null) {
            return false;
        }

        game.newBid(player, bid);
        return true;
    }

    public List<Session> getPlayerSessionsByGame(Game game) {
        List<Session> sessions = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            System.out.println(String.format("player %s in get playersessionsbygame", player));
            Session session = getSessionByPlayerId(player.getId());
            sessions.add(session);
        }

        return sessions;
    }
}
