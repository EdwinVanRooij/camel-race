package io.github.edwinvanrooij.net;

import javax.websocket.*;
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
        print(message, "from handleMessage");
        SocketServer.getInstance().handleMessage(message, session);
    }

    @SuppressWarnings("SameParameterValue")
    private void print(String message, String extra) {
        System.out.printf("HostEndpoint (%s): %s\n", message, extra);
    }

    private void print(String message) {
        System.out.println("HostEndpoint: " + message);
    }
}



