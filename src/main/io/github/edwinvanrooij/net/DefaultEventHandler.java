package io.github.edwinvanrooij.net;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.Util;
import io.github.edwinvanrooij.camelraceshared.events.Event;

import javax.websocket.Session;

/**
 * Created by eddy
 * on 7/25/17.
 */
public class DefaultEventHandler extends BaseEventHandler {

    public DefaultEventHandler() {

    }

    @Override
    protected void handleClientEvent(String event, JsonObject json, Session session) throws Exception {
        switch (event) {
            case Event.KEY_WHICH_GAME_TYPE: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                String gameType = Util.extractGameType(gameId);

                sendEvent(Event.KEY_GAME_TYPE, gameType, session);
                break;
            }
            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
    }

    @Override
    protected void handleHostEvent(String event, JsonObject json, Session session) throws Exception {

    }
}
