package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.domain.Game;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class SocketServer implements Runnable {

    private static SocketServer ourInstance = new SocketServer();

    public static SocketServer getInstance() {
        return ourInstance;
    }

    private List<Game> games;

    private SocketServer() {
        games = new ArrayList<>();
    }

    public Game createGame() {
        String id = generateUniqueId();
        Game game = new Game(id);
        games.add(game);
        return game;
    }

    private String generateUniqueId() {
        String id;
        do {
            id = generateId();
        } while (gameWithIdExists(id));

        return id;
    }

    private String generateId() {
        char[] vowels = "aeiouy".toCharArray(); // klinkers
        char[] consonants = "bcdfghjklmnpqrstvwxz".toCharArray(); // medeklinkers

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char c;
            if (i % 2 == 1) {
                c = vowels[random.nextInt(vowels.length)];
            } else {
                c = consonants[random.nextInt(consonants.length)];
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private boolean gameWithIdExists(String id) {
        for (Game g : games) {
            if (Objects.equals(g.getId(), id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8082);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        try {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(HostEndpoint.class);
            wscontainer.addEndpoint(ClientEndpoint.class);

            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
