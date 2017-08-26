package io.github.edwinvanrooij.net.endpoints;

import io.github.edwinvanrooij.Const;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static io.github.edwinvanrooij.Util.log;
import static io.github.edwinvanrooij.Util.logError;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint(Const.DEFAULT_ENDPOINT)
public class DefaultEndpoint {

    @OnOpen
    public void open(Session session) {
        try {
            session.close();
            log("Session opened and closed. Default connections are not allowed.");
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
        log(String.format("Default: %s", error.getMessage()));
        logError(error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log(String.format("Received from default endpoint: %s", message));
    }
}
