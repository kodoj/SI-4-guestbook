package com.codecool.controller;

import com.codecool.database.GuestbookAccessObject;
import com.codecool.database.IDAO;
import com.codecool.database.guestEntryIterator;
import com.codecool.database.model.GuestEntry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Guestbook extends AbstractHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        IDAO dao = new GuestbookAccessObject();

        List<GuestEntry> gb = dao.loadGuestbook();
        Iterator<GuestEntry> iterator = new guestEntryIterator(gb);
        Map<String, String> entryData = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            GuestEntry next = iterator.next();
            String key = next.getName() + " on day " + next.getDate();
            String msg = next.getMessage();
            System.out.println(key + "\n" + msg);
            entryData.put(key, msg);
        }

        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String sessionId = "";
        if (cookieStr == null) {
            redirectToLocation(httpExchange, "login");
        } else {
            sessionId = getSidFromCookieStr(cookieStr);
        }

        // Send a form if it wasn't submitted yet.
        if(method.equals("GET") && isLoggedIn(sessionId)){
            sendTemplateResponseWithList(httpExchange, "hello", entryData);
        }

        if(method.equals("GET") && !isLoggedIn(sessionId)){
            redirectToLocation(httpExchange, "login");
        }

        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")) {
            Map inputs = readFormData(httpExchange);
            addNewGuestEntry(inputs);
            redirectToLocation(httpExchange, "guest");
        }
    }

    private static void addNewGuestEntry(Map<String, String> entryData) {
        String name = entryData.get("name");
        String msg = entryData.get("msg");
        LocalDate now = LocalDate.now();
        String date = "" + now.getYear() + "-" + now.getMonthValue() +
                "-" + now.getDayOfMonth() + " " +
                Time.valueOf(LocalTime.now()).getHours() + ":" +
                Time.valueOf(LocalTime.now()).getMinutes();
        System.out.println(date);
        GuestEntry entry = new GuestEntry(name, msg, date);
        IDAO dao = new GuestbookAccessObject();
        dao.addGuestEntry(entry);
    }
}
