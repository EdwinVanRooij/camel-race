package io.github.edwinvanrooij.net.endpoints.mexican;


import io.github.edwinvanrooij.Const;
import io.github.edwinvanrooij.net.*;
import io.github.edwinvanrooij.net.BaseEventHandler;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import static io.github.edwinvanrooij.Config.MAX_IDLE_TIMEOUT_CLIENT;
import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/mexican/client")
public class ClientEndpoint {
    private static String gameName = Const.KEY_GAME_NAME_MEXICAN;

    @OnOpen
    public void open(Session session) {
        log(String.format("%s client connection opened.", gameName));
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT_CLIENT * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes
    }

    @OnClose
    public void close(Session session) {
        log(String.format("%s client connection closed.", gameName));
    }

    @OnError
    public void onError(Throwable error) {
        log(String.format("%s (client): %s", gameName, error.getMessage()));
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log(String.format("Received from %s client: %s", gameName, message));
        SocketServer.getInstance().handleMessage(Const.KEY_GAME_NAME_MEXICAN, BaseEventHandler.EventType.CLIENT, message, session);
    }
}
