package io.github.edwinvanrooij;

import io.github.edwinvanrooij.net.SocketServer;

import java.io.*;
import java.util.Properties;

import static io.github.edwinvanrooij.Util.log;

public class Main {
    public static void main(String[] args) {
        log("Started server.");
        SocketServer.getInstance().run();
    }
}
