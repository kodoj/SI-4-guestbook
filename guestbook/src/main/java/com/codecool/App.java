package com.codecool;

import com.codecool.controller.*;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        // create a server on port 8001
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        // set routes
        server.createContext("/guest", new Guestbook());
        server.createContext("/login", new Login());
        server.createContext("/static", new Static());
        server.createContext("/logout", new Logout());
        server.createContext("/students", new StudentController());

        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}