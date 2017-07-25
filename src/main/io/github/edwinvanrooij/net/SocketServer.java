package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.Config;
import io.github.edwinvanrooij.Const;
import io.github.edwinvanrooij.net.endpoints.camelrace.CamelRaceEventHandler;

import io.github.edwinvanrooij.net.endpoints.camelrace.*;

import io.github.edwinvanrooij.net.endpoints.mexican.*;

import io.github.edwinvanrooij.net.endpoints.mexican.MexicanEventHandler;
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
import java.util.*;

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

    private CamelRaceEventHandler camelRaceEventHandler;
    private MexicanEventHandler mexicanEventHandler;
    private DefaultEventHandler defaultEventHandler;

    private SocketServer() {
        camelRaceEventHandler = new CamelRaceEventHandler();
        mexicanEventHandler = new MexicanEventHandler();
        defaultEventHandler = new DefaultEventHandler();
    }

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

            wscontainer.addEndpoint(io.github.edwinvanrooij.net.endpoints.camelrace.HostEndpoint.class);
            wscontainer.addEndpoint(io.github.edwinvanrooij.net.endpoints.camelrace.ClientEndpoint.class);

            wscontainer.addEndpoint(io.github.edwinvanrooij.net.endpoints.mexican.HostEndpoint.class);
            wscontainer.addEndpoint(io.github.edwinvanrooij.net.endpoints.mexican.ClientEndpoint.class);

            wscontainer.addEndpoint(io.github.edwinvanrooij.net.endpoints.DefaultClientEndpoint.class);
            wscontainer.addEndpoint(io.github.edwinvanrooij.net.endpoints.DefaultHostEndpoint.class);

            server.start();
            server.join();

        } catch (Exception e) {
            logError(e);
        }
    }

    public void handleMessage(String gameKey, BaseEventHandler.EventType eventType, String message, Session session) {
        try {
            switch (gameKey) {
                case Const.KEY_GAME_NAME_CAMELRACE:
                    camelRaceEventHandler.handleEvent(eventType, message, session);
                    break;
                case Const.KEY_GAME_NAME_MEXICAN:
                    mexicanEventHandler.handleEvent(eventType, message, session);
                    break;
                case Const.KEY_DEFAULT_ENDPOINT:
                    defaultEventHandler.handleEvent(eventType, message, session);
                    break;
                default:
                    throw new Exception("Could not determine which game this event came from.");
            }
        } catch (Exception e) {
            logError(e);
        }
    }
}
