package io.github.edwinvanrooij.net;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/client")
public class ClientEndpoint {

    @OnOpen
    public void open(Session session) {
        System.out.println("Open!");
    }

    @OnClose
    public void close(Session session) {
        System.out.println("Close!");
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("Error!");
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println(String.format("Message: %s!", message));
    }
}
