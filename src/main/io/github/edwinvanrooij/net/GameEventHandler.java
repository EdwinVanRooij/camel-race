package io.github.edwinvanrooij.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.camelraceshared.domain.*;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.CamelRaceGame;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.PlayerNewBid;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.domain.GameManager;

import javax.websocket.Session;
import java.util.Map;

/**
 * Created by eddy
 * on 7/24/17.
 */
public abstract class GameEventHandler extends BaseEventHandler {

    protected static JsonParser parser = new JsonParser();
    protected static Gson gson = new Gson();

    protected GameManager gameManager = new GameManager();

    public GameEventHandler() {

    }

    @Override
    protected boolean handleClientEvent(String event, JsonObject json, Session session) throws Exception {
        switch (event) {
            case Event.KEY_PLAYER_JOIN: {
                PlayerJoinRequest playerJoinRequest = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerJoinRequest.class);
                String gameId = playerJoinRequest.getGameId();
                Player player = gameManager.playerJoin(gameId, playerJoinRequest.getPlayer(), session);
                sendEvent(Event.KEY_PLAYER_JOINED, player, session);

                Game game = gameManager.getGameById(gameId);
                sendEvent(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));
                return true;
            }

            case Event.KEY_PLAYER_NOT_READY: {
                PlayerNotReady playerNotReady = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNotReady.class);
                Boolean result = gameManager.playerNotReady(playerNotReady.getGameId(), playerNotReady.getPlayer());
                sendEvent(Event.KEY_PLAYER_NOT_READY_SUCCESS, result, session);

                Session gameSession = gameManager.getSessionByGameId(playerNotReady.getGameId());
                sendEvent(Event.KEY_PLAYER_NOT_READY, playerNotReady, gameSession);
                return true;
            }

            case Event.KEY_PLAYER_READY: {
                PlayerReady playerReady = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerReady.class);
                Boolean result = gameManager.playerReady(playerReady.getGameId(), playerReady.getPlayer());
                sendEvent(Event.KEY_PLAYER_READY_SUCCESS, result, session);

                Session gameSession = gameManager.getSessionByGameId(playerReady.getGameId());
                sendEvent(Event.KEY_PLAYER_READY, playerReady, gameSession);

                if (gameManager.isEveryoneReady(playerReady.getGameId())) {
                    sendEvent(Event.KEY_GAME_READY, "", gameSession);
                }
                return true;
            }


            case Event.KEY_PLAYER_ALIVE_CHECK: {
                PlayerAliveCheck playerAliveCheck = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerAliveCheck.class);
                gameManager.playerAliveCheck(playerAliveCheck);

                sendEvent(Event.KEY_PLAYER_ALIVE_CHECK_CONFIRMED, true, session);
                return true;
            }

            case Event.KEY_PLAY_AGAIN: {
                PlayAgainRequest playAgainRequest = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayAgainRequest.class);

                String gameId = playAgainRequest.getGameId();
                Game game = gameManager.getGameById(gameId);

                game.playAgain(playAgainRequest.getPlayer().getId(), true);

                sendEvent(Event.KEY_PLAY_AGAIN_SUCCESSFUL, true, session);

                if (game.allPlayAgain()) {
                    restartGame(game);
                }
                return true;
            }

            default:
                return false;
        }
    }

    @Override
    protected boolean handleHostEvent(String event, JsonObject json, Session session) throws Exception {
        switch (event) {

            case Event.KEY_GAME_RESTART: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                Game game = gameManager.getGameById(gameId);
                restartGame(game);
                return true;
            }

            default:
                return false;
        }
    }

    protected void restartGame(Game game) throws Exception {
        Session gameSession = gameManager.getSessionByGameId(game.getId());
        sendEvent(Event.KEY_GAME_RESTART, "", gameSession);

        game.restart();

        for (Player player : game.getPlayers()) {
            sendEvent(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));

            if (game instanceof CamelRaceGame) {
                CamelRaceGame game1 = (CamelRaceGame) game;
                Bid bid = game1.getBid(player.getId());
                if (bid == null) {
                    throw new Exception("Bid is null! This is not allowed when restarting a game.");
                }
                PlayerNewBid playerBid = new PlayerNewBid(game.getId(), player, bid);
                sendEvent(Event.KEY_PLAYER_NEW_BID, playerBid, gameManager.getSessionByGameId(game.getId()));
            }
        }
        for (Map.Entry<Player, Session> entry : gameManager.getPlayerSessionMapByGame(game).entrySet()) {
            sendEvent(Event.KEY_PLAYER_JOINED, entry.getKey(), entry.getValue());
        }
    }
}
