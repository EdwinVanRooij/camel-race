package io.github.edwinvanrooij.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.camelraceshared.events.Event;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

import static io.github.edwinvanrooij.Util.log;
import static io.github.edwinvanrooij.Util.logError;

/**
 * Created by eddy
 * on 7/24/17.
 */
public abstract class BaseEventHandler {

    protected static JsonParser parser = new JsonParser();
    protected static Gson gson = new Gson();

    public BaseEventHandler() {

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

    /**
     * Handles an event sent from a client.
     * @param event event key, as defined in the Event class
     * @param json json representation of the event data
     * @param session session from which this event was sent
     * @return a value indicating whether or not this event was dealt with yet
     * @throws Exception
     */
    protected abstract boolean handleClientEvent(String event, JsonObject json, Session session) throws Exception;

    /**
     * Handles an event sent from a host.
     * @param event event key, as defined in the Event class
     * @param json json representation of the event data
     * @param session session from which this event was sent
     * @return a value indicating whether or not this event was dealt with yet
     * @throws Exception
     */
    protected abstract boolean handleHostEvent(String event, JsonObject json, Session session) throws Exception;

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
