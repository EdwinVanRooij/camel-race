package io.github.edwinvanrooij.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.Config;
import io.github.edwinvanrooij.camelraceshared.domain.*;
import io.github.edwinvanrooij.camelraceshared.events.*;
import io.github.edwinvanrooij.domain.GameManager;
import io.github.edwinvanrooij.net.endpoints.ClientEndpoint;
import io.github.edwinvanrooij.net.endpoints.DefaultEndpoint;
import io.github.edwinvanrooij.net.endpoints.HostEndpoint;
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

import static io.github.edwinvanrooij.Util.log;
import static io.github.edwinvanrooij.Util.logError;

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
    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    @Override
    public void run() {
        try {
            Server server = new Server(Config.PORT);

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
            logError(e);
        }
    }

    private void handleClientMessage(String type, JsonObject json, Session session) throws Exception {
        try {
            switch (type) {
                case Event.KEY_PLAYER_JOIN: {
                    PlayerJoinRequest playerJoinRequest = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerJoinRequest.class);
                    String gameId = playerJoinRequest.getGameId();
                    Player player = gameManager.playerJoin(gameId, playerJoinRequest.getPlayer(), session);
                    sendMessage(Event.KEY_PLAYER_JOINED, player, session);

                    Game game = gameManager.getGameById(gameId);
                    sendMessage(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));
                    break;
                }

                case Event.KEY_PLAYER_NEW_BID: {
                    PlayerNewBid playerNewBid = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNewBid.class);
                    Boolean result = gameManager.playerNewBid(playerNewBid.getGameId(), playerNewBid.getPlayer(), playerNewBid.getBid());
                    sendMessage(Event.KEY_PLAYER_BID_HANDED_IN, result, session);

                    Session gameSession = gameManager.getSessionByGameId(playerNewBid.getGameId());
                    sendMessage(Event.KEY_PLAYER_NEW_BID, playerNewBid, gameSession);
                    break;
                }

                case Event.KEY_PLAYER_READY: {
                    PlayerNewBid playerNewBid = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNewBid.class);
                    Boolean result = gameManager.playerNewBidAndReady(playerNewBid.getGameId(), playerNewBid.getPlayer(), playerNewBid.getBid());
                    sendMessage(Event.KEY_PLAYER_READY_SUCCESS, result, session);

                    Session gameSession = gameManager.getSessionByGameId(playerNewBid.getGameId());
                    sendMessage(Event.KEY_PLAYER_READY, playerNewBid, gameSession);

                    if (gameManager.isEveryoneReady(playerNewBid.getGameId())) {
                        sendMessage(Event.KEY_GAME_READY, "", gameSession);
                    }
                    break;
                }

                case Event.KEY_PLAYER_NOT_READY: {
                    PlayerNotReady playerNotReady = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNotReady.class);
                    Boolean result = gameManager.playerNotReady(playerNotReady.getGameId(), playerNotReady.getPlayer());
                    sendMessage(Event.KEY_PLAYER_NOT_READY_SUCCESS, result, session);

                    Session gameSession = gameManager.getSessionByGameId(playerNotReady.getGameId());
                    sendMessage(Event.KEY_PLAYER_NOT_READY, playerNotReady, gameSession);
                    break;
                }

                case Event.KEY_PLAYER_ALIVE_CHECK: {
                    PlayerAliveCheck playerAliveCheck = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerAliveCheck.class);
                    gameManager.playerAliveCheck(playerAliveCheck);

                    sendMessage(Event.KEY_PLAYER_ALIVE_CHECK_CONFIRMED, true, session);
                    break;
                }

                case Event.KEY_PLAY_AGAIN: {
                    PlayAgainRequest playAgainRequest = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayAgainRequest.class);

                    String gameId = playAgainRequest.getGameId();
                    Game game = gameManager.getGameById(gameId);

                    game.playAgain(playAgainRequest.getPlayer().getId(), true);

                    sendMessage(Event.KEY_PLAY_AGAIN_SUCCESSFUL, true, session);

                    if (game.allPlayAgain()) {
                        restartGame(game);
                    }
                    break;
                }
                default:
                    throw new Exception("Could not determine a correct event type for client message.");
            }
        } catch (Exception e) {
            logError(e);
        }
    }

    private void handleHostMessage(String type, JsonObject json, Session session) throws Exception {
        try {
            switch (type) {
                case Event.KEY_GAME_CREATE: {
                    Game game = gameManager.createGame(session);
                    sendMessage(Event.KEY_GAME_CREATED, game, session);
                    break;
                }

                case Event.KEY_GAME_START: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    GameState currentGameState = game.generateGameState();
                    sendMessage(Event.KEY_GAME_STARTED_WITH_STATE, currentGameState, session);

                    List<Session> playerSessions = gameManager.getPlayerSessionsByGame(game);
                    sendMessages(Event.KEY_GAME_STARTED, "", playerSessions);

                    break;
                }

                case Event.KEY_PICK_CARD: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    Card card = game.pickCard();

                    sendMessage(Event.KEY_PICKED_CARD, card, session);
                    break;
                }

                case Event.KEY_CAMEL_WON: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    Camel camel = game.didCamelWinYet();

                    if (camel != null) {
                        sendMessage(Event.KEY_CAMEL_DID_WIN, camel, session);
                    } else {
                        sendMessage(Event.KEY_CAMEL_DID_NOT_WIN, "", session);
                    }
                    break;
                }

                case Event.KEY_MOVE_CARDS_BY_LATEST: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    game.moveCamelAccordingToLastCard();
                    List<Camel> newCamelPositions = game.getCamelList();

                    sendMessage(Event.KEY_NEW_CAMEL_POSITIONS, newCamelPositions, session);
                    break;
                }

                case Event.KEY_SHOULD_SIDE_CARD_TURN: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    boolean shouldItTurn = game.shouldTurnSideCard();

                    if (shouldItTurn) {
                        sendMessage(Event.KEY_SHOULD_SIDE_CARD_TURN_YES, game.getSideCardList(), session);
                    } else {
                        sendMessage(Event.KEY_SHOULD_SIDE_CARD_TURN_NO, "", session);
                    }
                    break;
                }

                case Event.KEY_NEW_CAMEL_LIST: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);

                    sendMessage(Event.KEY_NEW_CAMEL_LIST, game.newCamelList(), session);
                    break;
                }

                case Event.KEY_GET_ALL_RESULTS: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);

                    GameResults gameResults = game.generateGameResults();
                    sendMessage(Event.KEY_ALL_RESULTS, gameResults, session);

                    for (Map.Entry<Player, Session> entry : gameManager.getPlayerSessionMapByGame(game).entrySet()) {
                        Bid bid = game.getBid(entry.getKey().getId());
                        boolean thisPlayerWon = bid.getType() == game.getWinner().getCardType();
                        PersonalResultItem item = new PersonalResultItem(bid, thisPlayerWon);
                        sendMessage(Event.KEY_GAME_OVER_PERSONAL_RESULTS, item, entry.getValue());
                    }
                    break;
                }

                case Event.KEY_GAME_RESTART: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    restartGame(game);
                    break;
                }

                default:
                    throw new Exception("Could not determine a correct event type for host message.");
            }
        } catch (Exception e) {
            logError(e);
        }
    }

    public void handleMessage(MessageType messageType, String message, Session session) {
        try {
            JsonObject json = parser.parse(message).getAsJsonObject();
            String type = json.get(Event.KEY_TYPE).getAsString();

            switch (messageType) {
                case CLIENT:
                    handleClientMessage(type, json, session);
                    break;
                case HOST:
                    handleHostMessage(type, json, session);
                    break;
                default:
                    throw new RuntimeException("Could not determine whether a client or a host sent this message.");
            }
        } catch (Exception e) {
            logError(e);
        }
    }

    private void restartGame(Game game) throws Exception {
        Session gameSession = gameManager.getSessionByGameId(game.getId());
        sendMessage(Event.KEY_GAME_RESTART, "", gameSession);

        game.restart();

        for (Player player : game.getPlayers()) {
            sendMessage(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));

            Bid bid = game.getBid(player.getId());
            if (bid == null) {
                throw new Exception("Bid is null! This is not allowed when restarting a game.");
            }
            PlayerNewBid playerBid = new PlayerNewBid(game.getId(), player, bid);
            sendMessage(Event.KEY_PLAYER_NEW_BID, playerBid, gameManager.getSessionByGameId(game.getId()));
        }
        for (Map.Entry<Player, Session> entry : gameManager.getPlayerSessionMapByGame(game).entrySet()) {
            sendMessage(Event.KEY_PLAYER_JOINED, entry.getKey(), entry.getValue());
        }
    }

    private void sendMessage(String eventType, Object value, Session session) {
        try {
            Event event = new Event(eventType, value);
            String message = new Gson().toJson(event);
            log(String.format("Sending: %s", message));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logError(e);
        }
    }

    private void sendMessages(String eventType, Object value, List<Session> sessionList) {
        Event event = new Event(eventType, value);
        String message = new Gson().toJson(event);
        log(String.format("Sending to multiple sessions: %s", message));

        for (Session session : sessionList) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException | NullPointerException e) {
                logError(e);
            }
        }
    }

    public enum MessageType {
        HOST,
        CLIENT
    }
}
