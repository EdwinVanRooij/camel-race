package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.Util;
import io.github.edwinvanrooij.domain.Event;
import io.github.edwinvanrooij.domain.Game;
import io.github.edwinvanrooij.domain.Player;

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
//        Game game = SocketServer.getInstance().createGame();
//        Event e = new Event(Event.GAME_ID, game);
//        String message = Util.objectToJson(e);
//
//        System.out.println(String.format("Sending: %s", message));
//        session.getBasicRemote().sendText(message);
//
//        Player player = new Player(1, "PlayerOne");
//        Event playerEvent = new Event(Event.PLAYER_JOINED, player);
//        String playerMessage = Util.objectToJson(playerEvent);
//
//        System.out.println(String.format("Sending: %s", playerMessage));
//        session.getBasicRemote().sendText(playerMessage);
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
