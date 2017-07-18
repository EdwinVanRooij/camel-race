package io.github.edwinvanrooij.net.endpoints;


import io.github.edwinvanrooij.net.SocketServer;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import static io.github.edwinvanrooij.Config.MAX_IDLE_TIMEOUT_CLIENT;
import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/client")
public class ClientEndpoint {

    @OnOpen
    public void open(Session session) {
        log("Client connection opened.");
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT_CLIENT * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes
    }

    @OnClose
    public void close(Session session) {
        log("Client connection closed.");
    }

    @OnError
    public void onError(Throwable error) {
        log(error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log("Received from Client: " + message);
        SocketServer.getInstance().handleMessage(SocketServer.MessageType.CLIENT, message, session);
    }
}
