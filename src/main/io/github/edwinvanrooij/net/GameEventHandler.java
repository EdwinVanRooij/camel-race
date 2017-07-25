package io.github.edwinvanrooij.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.camelraceshared.domain.Game;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.CamelRaceGame;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.PlayerNewBid;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.domain.GameManager;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.github.edwinvanrooij.Util.log;
import static io.github.edwinvanrooij.Util.logError;

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

    protected void restartGame(Game game) throws Exception {
        Session gameSession = gameManager.getSessionByGameId(game.getId());
        sendEvent(Event.KEY_GAME_RESTART, "", gameSession);

        game.restart();

        for (Player player : game.getPlayers()) {
            sendEvent(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));

            CamelRaceGame game1 = (CamelRaceGame) game;
            Bid bid = game1.getBid(player.getId());
            if (bid == null) {
                throw new Exception("Bid is null! This is not allowed when restarting a game.");
            }
            PlayerNewBid playerBid = new PlayerNewBid(game.getId(), player, bid);
            sendEvent(Event.KEY_PLAYER_NEW_BID, playerBid, gameManager.getSessionByGameId(game.getId()));
        }
        for (Map.Entry<Player, Session> entry : gameManager.getPlayerSessionMapByGame(game).entrySet()) {
            sendEvent(Event.KEY_PLAYER_JOINED, entry.getKey(), entry.getValue());
        }
    }
}
