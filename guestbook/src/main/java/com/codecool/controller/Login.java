package com.codecool.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;
import java.util.Map;
import java.util.UUID;

public class Login extends AbstractHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
        String sessionId;
        if (cookieStr == null) {
            sessionId = generateSID();
            cookie = new HttpCookie("SID", sessionId);
            httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
        } else {
            sessionId = getSidFromCookieStr(cookieStr);
        }
        if(method.equals("GET") && !isLoggedIn(sessionId)){
            sendTemplateResponse(httpExchange, "login");
        }

        if(method.equals("GET") && isLoggedIn(sessionId)){
            redirectToLocation(httpExchange, "guest");
        }

        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")){
            Map inputs = readFormData(httpExchange);
            String login = (String) inputs.get("login");
            String password = (String) inputs.get("password");
            if (login.equalsIgnoreCase(password)) {
                getSessionIdContainer().add(sessionId, login);
                redirectToLocation(httpExchange, "guest");
            } else {
                redirectToLocation(httpExchange, "login");
            }

        }
    }

    private String generateSID() {
        return UUID.randomUUID().toString();
    }

}
