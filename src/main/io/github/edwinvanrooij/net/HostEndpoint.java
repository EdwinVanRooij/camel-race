package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.domain.Game;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/host")
public class HostEndpoint {

    @OnOpen
    public void open(Session session) throws IOException {
        Game game = SocketServer.getInstance().createGame();
        session.getBasicRemote().sendText(String.format("Created game with ID: %s", game.getId()));
    }

    @OnClose
    public void close(Session session) {
        print("close");
    }

    @OnError
    public void onError(Throwable error) {
        print(error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        print(message);
    }

    private void print(String message, Object... args) {
        System.out.printf("HostEndpoint: " + message + "\n", args);
    }
}
