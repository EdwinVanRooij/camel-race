package io.github.edwinvanrooij.domain;


import io.github.edwinvanrooij.Const;
import io.github.edwinvanrooij.Util;
import io.github.edwinvanrooij.camelraceshared.domain.Game;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerAliveCheck;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.CamelRaceGame;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.MexicanGame;

import javax.websocket.Session;
import java.util.*;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class GameManager {
    private List<Game> games; // list of all games
    private Map<String, Session> gameSessionMap; // all game sessions mapped by their unique game ID
    private Map<Integer, Session> playerSessionMap; // all player sessions mapped by their unique player ID

    public GameManager() {
        games = new ArrayList<>();
        gameSessionMap = new HashMap<>();
        playerSessionMap = new HashMap<>();
    }

//    public Game createGame(Session session) throws Exception {
//        String id = generateUniqueGameId();
//        Game game = new Game(id);
//        games.add(game);
//        gameSessionMap.put(game.getId(), session);
//        return game;
//    }

    public Game createCamelRaceGame(Session session) throws Exception {
        String id = generateUniqueGameId(Const.PREFIX_CAMEL_RACE);
        Game game = new CamelRaceGame(id);
        games.add(game);
        gameSessionMap.put(game.getId(), session);
        return game;
    }

    public Game createMexicanGame(Session session) throws Exception {
        String id = generateUniqueGameId(Const.PREFIX_MEXICAN);
        Game game = new MexicanGame(id);
        games.add(game);
        gameSessionMap.put(game.getId(), session);
        return game;
    }

    private String generateUniqueGameId(String prefix) {
        String id;
        do {
            id = Util.generateGameId(prefix);
        } while (gameWithIdExists(id));
        return id;
    }


    public Session getSessionByGameId(String id) {
        return gameSessionMap.get(id.toLowerCase());
    }

    public Session getSessionByPlayerId(int id) {
        return playerSessionMap.get(id);
    }

    public boolean isEveryoneReady(String gameId) throws Exception {
        Game game = getGameById(gameId);
        return game.everyoneIsReady();
    }

    public Game getGameById(String id) throws Exception {
        for (Game game : games) {
            if (Objects.equals(game.getId().toLowerCase(), id.toLowerCase())) {
                return game;
            }
        }
        throw new Exception("No game found by ID " + id);
    }

    private boolean gameWithIdExists(String id) {
        try {
            getGameById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Player playerJoin(String gameId, Player player, Session session) throws Exception {
        Game game = getGameById(gameId);

        Player playerWithId = game.addPlayer(player);
        playerSessionMap.put(playerWithId.getId(), session);
        return playerWithId;
    }

    public void playerAliveCheck(PlayerAliveCheck playerAliveCheck) throws Exception {
        Game game = getGameById(playerAliveCheck.getGameId());
        Player player = game.getPlayer(playerAliveCheck.getPlayer().getId());
        game.playerAliveCheck(player);
    }

    public boolean playerNotReady(String gameId, Player player) throws Exception {
        Game game = getGameById(gameId);
        game.ready(player, false);
        return true;
    }

    public boolean playerReady(String gameId, Player player) throws Exception {
        Game game = getGameById(gameId);
        game.ready(player, true);
        return true;
    }

    public boolean playerNewBid(String gameId, Player player, Bid bid) throws Exception {
        CamelRaceGame game = (CamelRaceGame) getGameById(gameId);
        game.newBid(player, bid);
        return true;
    }

    public List<Session> getPlayerSessionsByGame(Game game) {
        List<Session> sessions = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            Session session = getSessionByPlayerId(player.getId());
            sessions.add(session);
        }

        return sessions;
    }

    public Map<Player, Session> getPlayerSessionMapByGame(Game game) {
        Map<Player, Session> playerSessionMap = new HashMap<>();

        for (Player player : game.getPlayers()) {
            Session session = getSessionByPlayerId(player.getId());
            playerSessionMap.put(player, session);
        }

        return playerSessionMap;
    }
}
