package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.Util;
import io.github.edwinvanrooij.domain.events.Event;

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
        System.out.printf("ClientEndpoint (%s): %s\n", message, extra);
    }

    private void print(String message) {
        System.out.println("ClientEndpoint" + message);
    }
}
//        Game game = SocketServer.getInstance().createGame();
//        Event e = new Event(Event.GAME_ID, game);
//        String message = Util.objectToJson(e);
//
//        System.out.println(String.format("Sending: %s", message));
//        session.getBasicRemote().sendText(message);
