package io.github.edwinvanrooij;

/**
 * Created by eddy
 * on 6/13/17.
 */
public class Config {

    // Port of the SocketServer
    public static final int PORT = 8085;

    // Max host timeout
    public static final int MAX_IDLE_TIMEOUT_CLIENT = 10; // in minutes
    public static final int MAX_IDLE_TIMEOUT_HOST = 30; // in minutes

    // Properties keys
    public static final String NAME_PROPERTIES_CONFIG = "config.properties";
    public static final String KEY_PROPERTIES_CONFIG_PRODUCTION = "production";

}
