package io.github.edwinvanrooij.net.endpoints.camelrace;

import io.github.edwinvanrooij.Const;
import io.github.edwinvanrooij.net.BaseEventHandler;
import io.github.edwinvanrooij.net.SocketServer;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static io.github.edwinvanrooij.Config.MAX_IDLE_TIMEOUT_HOST;
import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint(Const.CAMEL_RACE_ENDPOINT_HOST)
public class HostEndpoint {
    private static String gameName = Const.KEY_GAME_NAME_CAMELRACE;

    @OnOpen
    public void open(Session session) throws IOException {
        log(String.format("%s host connection opened.", gameName));
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT_HOST * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes
    }

    @OnClose
    public void close(Session session) {
        log(String.format("%s host connection closed.", gameName));
    }

    @OnError
    public void onError(Throwable error) {
        log(String.format("%s (host): %s", gameName, error.getMessage()));
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log(String.format("Received from %s host: %s", gameName, message));
        SocketServer.getInstance().handleMessage(Const.KEY_GAME_NAME_CAMELRACE, BaseEventHandler.EventType.HOST, message, session);
    }
}
