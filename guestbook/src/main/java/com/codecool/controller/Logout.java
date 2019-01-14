package com.codecool.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;

public class Logout extends AbstractHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String sid = getSidFromCookieStr(cookieStr);

        getSessionIdContainer().remove(sid);
        httpExchange.getResponseHeaders().add("Set-Cookie", cookieStr + ";Max-Age=0");

        redirectToLocation(httpExchange,"login");

    }
}
