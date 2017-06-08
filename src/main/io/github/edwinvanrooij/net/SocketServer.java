package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.Util;
import io.github.edwinvanrooij.domain.Game;
import io.github.edwinvanrooij.domain.GameManager;
import io.github.edwinvanrooij.domain.Player;
import io.github.edwinvanrooij.domain.events.Event;
import io.github.edwinvanrooij.domain.events.PlayerJoinRequest;
import io.github.edwinvanrooij.domain.events.PlayerNewBid;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.servlet.DispatcherType;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import java.io.IOException;
import java.util.*;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class SocketServer implements Runnable {

    private static SocketServer ourInstance = new SocketServer();

    public static SocketServer getInstance() {
        return ourInstance;
    }

    private SocketServer() {
    }



    private GameManager gameManager = new GameManager();

    @Override
    public void run() {
        try {

            Server server = new Server(8082);

            // Setup the context for servlets
            ServletContextHandler context = new ServletContextHandler();
            // Set the context for all filters and servlets
            // Required for the internal servlet & filter ServletContext to be sane
            context.setContextPath("/");
            // The servlet context is what holds the welcome list
            // (not the ResourceHandler or DefaultServlet)
            context.setWelcomeFiles(new String[]{"index.html"});

            // Add the filter, and then use the provided FilterHolder to configure it
            FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
            cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
            cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
            cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
            cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

            // Use a DefaultServlet to serve static files.
            // Alternate Holder technique, prepare then add.
            // DefaultServlet should be named 'default'
            ServletHolder def = new ServletHolder("default", DefaultServlet.class);
            def.setInitParameter("resourceBase", "./http/");
            def.setInitParameter("dirAllowed", "false");
            context.addServlet(def, "/");

            // Create the server level handler list.
            HandlerList handlers = new HandlerList();
            // Make sure DefaultHandler is last (for error handling reasons)
            handlers.setHandlers(new Handler[]{context, new DefaultHandler()});

            server.setHandler(handlers);

            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
            wscontainer.addEndpoint(HostEndpoint.class);
            wscontainer.addEndpoint(ClientEndpoint.class);
            wscontainer.addEndpoint(DefaultEndpoint.class);

            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(String message, Session session) {
        try {
            Event event = Util.jsonToEvent(message);

            switch (event.getEventType()) {

                case Event.KEY_GAME_CREATE: {
                    System.out.println(String.format("session in handleMessage socketserver: %s", session));
                    Game game = gameManager.createGame(session);
                    sendMessage(Event.KEY_GAME_CREATED, game, session);
                    break;
                }

                case Event.KEY_PLAYER_JOIN: {
                    PlayerJoinRequest playerJoinRequest = (PlayerJoinRequest) event.getValue();
                    String gameId = playerJoinRequest.getGameId();
                    Player player = gameManager.playerJoin(gameId, playerJoinRequest.getPlayer(), session);
                    sendMessage(Event.KEY_PLAYER_JOINED, player, session);

                    Game game = gameManager.getGameById(gameId);
                    System.out.println(String.format("Got this game: %s", game.toString()));
                    sendMessage(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));
                    break;
                }

                case Event.KEY_PLAYER_NEW_BID: {
                    PlayerNewBid playerNewBid = (PlayerNewBid) event.getValue();
                    Boolean result = gameManager.playerNewBid(playerNewBid.getGameId(), playerNewBid.getPlayer(), playerNewBid.getCamel(), playerNewBid.getValue());
                    sendMessage(Event.KEY_PLAYER_BID_HANDED_IN, result, session);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String eventType, Object value, Session session) {
        try {
            System.out.println(String.format("About to send '%s' with object '%s'", eventType, value));
            Event event = new Event(eventType, value);
            String message = Util.objectToJson(event);
            System.out.println(String.format("Sending: %s", message));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
