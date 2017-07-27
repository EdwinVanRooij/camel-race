package io.github.edwinvanrooij.net.endpoints.mexican;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.MexicanGame;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.net.BaseEventHandler;
import io.github.edwinvanrooij.net.GameEventHandler;

import javax.websocket.Session;

import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 7/24/17.
 */
public class MexicanEventHandler extends GameEventHandler {
    @Override
    protected boolean handleClientEvent(String event, JsonObject json, Session session) throws Exception {
        if (super.handleClientEvent(event, json, session)) {
            log("Client Event was handled by super, from camel race event handler.");
            return true;
        }
        return true;
    }

    @Override
    protected boolean handleHostEvent(String event, JsonObject json, Session session) throws Exception {
        // Let base handlers handle this event, if possible.
        if (super.handleHostEvent(event, json, session)) {
            return true;
        }

        switch (event) {
            case Event.KEY_MEXICAN_GAME_CREATE: {
                MexicanGame game = (MexicanGame) gameManager.createMexicanGame(session);
                sendEvent(Event.KEY_GAME_CREATED, game, session);
                return true;
            }
            default:
                throw new Exception("Could not determine a correct event type for host message.");
        }
    }
}
