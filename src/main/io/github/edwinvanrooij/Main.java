package io.github.edwinvanrooij;

import io.github.edwinvanrooij.net.SocketServer;

public class Main {
    public static void main(String[] args) {
        SocketServer.getInstance().run();
    }
}
