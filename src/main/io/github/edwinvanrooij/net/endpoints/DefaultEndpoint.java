package io.github.edwinvanrooij.net.endpoints;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import static io.github.edwinvanrooij.Util.log;
import static io.github.edwinvanrooij.Util.logError;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint("/")
public class DefaultEndpoint {

    @OnOpen
    public void open(Session session) {
        try {
            session.close();
            log("Default connection opened and manually closed, because this is not allowed.");
        } catch (IOException e) {
            logError(e);
        }
    }

    @OnClose
    public void close(Session session) {
        log("Default connection closed.");
    }

    @OnError
    public void onError(Throwable error) {
        log(error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log("Received from Default: " + message);
    }
}
