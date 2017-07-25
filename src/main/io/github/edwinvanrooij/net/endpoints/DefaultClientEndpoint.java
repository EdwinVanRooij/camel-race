package io.github.edwinvanrooij.net.endpoints;

import io.github.edwinvanrooij.Const;
import io.github.edwinvanrooij.net.BaseEventHandler;
import io.github.edwinvanrooij.net.SocketServer;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import static io.github.edwinvanrooij.Config.MAX_IDLE_TIMEOUT_DEFAULT;
import static io.github.edwinvanrooij.Util.log;
import static io.github.edwinvanrooij.Util.logError;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint(Const.DEFAULT_ENDPOINT_CLIENT)
public class DefaultClientEndpoint {

    @OnOpen
    public void open(Session session) {
        log("Default client connection opened.");
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT_DEFAULT * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes
    }

    @OnClose
    public void close(Session session) {
        log("Default client connection closed.");
    }

    @OnError
    public void onError(Throwable error) {
        log(String.format("Default (client): %s", error.getMessage()));
        logError(error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log(String.format("Received from default client endpoint: %s", message));
        SocketServer.getInstance().handleMessage(Const.KEY_DEFAULT_ENDPOINT, BaseEventHandler.EventType.CLIENT, message, session);
    }
}
