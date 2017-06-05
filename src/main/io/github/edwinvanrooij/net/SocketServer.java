package io.github.edwinvanrooij.net;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerContainer;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class SocketServer implements Runnable {

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
