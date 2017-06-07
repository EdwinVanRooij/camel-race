package io.github.edwinvanrooij.net;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/")
public class DefaultEndpoint {

    @OnOpen
    public void open(Session session) {
        print("open");
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
        System.out.printf("DefaultEndpoint: " + message + "\n", args);
    }
}
