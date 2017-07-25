package io.github.edwinvanrooij.net.endpoints.mexican;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.net.BaseEventHandler;
import io.github.edwinvanrooij.net.GameEventHandler;

import javax.websocket.Session;

/**
 * Created by eddy
 * on 7/24/17.
 */
public class MexicanEventHandler extends GameEventHandler {
    @Override
    protected void handleClientEvent(String event, JsonObject json, Session session) {

    }

    @Override
    protected void handleHostEvent(String event, JsonObject json, Session session) {

    }
}
