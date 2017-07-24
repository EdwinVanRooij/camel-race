package io.github.edwinvanrooij.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.Const;
import io.github.edwinvanrooij.camelraceshared.domain.*;
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
public abstract class BaseEventHandler {

    protected static JsonParser parser = new JsonParser();
    protected static Gson gson = new Gson();

    protected GameManager gameManager = new GameManager();

    public BaseEventHandler() {

    }

    protected void restartGame(Game game) throws Exception {
        Session gameSession = gameManager.getSessionByGameId(game.getId());
        sendEvent(Event.KEY_GAME_RESTART, "", gameSession);

        game.restart();

        for (Player player : game.getPlayers()) {
            sendEvent(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));

            Bid bid = game.getBid(player.getId());
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

    public void handleEvent(EventType eventType, String message, Session session) throws Exception {
        JsonObject json = parser.parse(message).getAsJsonObject();
        String event = json.get(Event.KEY_TYPE).getAsString();

        switch (eventType) {
            case CLIENT:
                handleClientEvent(event, json, session);
                break;
            case HOST:
                handleHostEvent(event, json, session);
                break;
            default:
                throw new Exception("Could not determine whether this was a client or host sent event.");
        }
    }

    protected abstract void handleClientEvent(String event, JsonObject json, Session session) throws Exception;

    protected abstract void handleHostEvent(String event, JsonObject json, Session session) throws Exception;

    protected void sendEvent(String eventType, Object value, Session session) {
        try {
            Event event = new Event(eventType, value);
            String message = new Gson().toJson(event);
            log(String.format("Sending: %s", message));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logError(e);
        }
    }

    protected void sendEvents(String eventType, Object value, List<Session> sessionList) {
        Event event = new Event(eventType, value);
        String message = new Gson().toJson(event);
        log(String.format("Sending to multiple sessions: %s", message));

        for (Session session : sessionList) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException | NullPointerException e) {
                logError(e);
            }
        }
    }

    public enum EventType {
        HOST,
        CLIENT
    }
}
