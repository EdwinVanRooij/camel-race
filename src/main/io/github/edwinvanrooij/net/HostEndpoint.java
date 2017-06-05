package io.github.edwinvanrooij.net;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/host")
public class HostEndpoint {

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
        System.out.printf("HostEndpoint: " + message + "\n", args);
    }
}
