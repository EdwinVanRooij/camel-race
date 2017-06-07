package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.Util;
import io.github.edwinvanrooij.domain.Event;
import io.github.edwinvanrooij.domain.Game;

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
        try {
            Event e = Util.jsonToEvent(message);
            SocketServer.getInstance().handlePlayerJoinRequest(e);

            print(e.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

//        Game game = SocketServer.getInstance().createGame();
//        Event e = new Event(Event.GAME_ID, game);
//        String message = Util.objectToJson(e);
//
//        System.out.println(String.format("Sending: %s", message));
//        session.getBasicRemote().sendText(message);
    }

    private void print(String message, Object... args) {
        System.out.printf("ClientEndpoint: " + message + "\n", args);
    }
}
