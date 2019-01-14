package com.codecool.controller;

import com.codecool.controller.session.SessionIdContainer;
import com.codecool.database.model.Student;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractHandler implements HttpHandler {
    private SessionIdContainer sessionIdContainer;

    public AbstractHandler() {
        this.sessionIdContainer = SessionIdContainer.getSessionIdContainer();
    }

    public SessionIdContainer getSessionIdContainer() {
        return sessionIdContainer;
    }

    public void redirectToLocation(HttpExchange exchange, String location) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Location", location);
        try {
            exchange.sendResponseHeaders(302, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        exchange.close();
    }

    public void sendResponse(HttpExchange exchange, String response) {
        byte[] bytes = response.getBytes();
        try {
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTemplateResponse(HttpExchange exchange, String templateName) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(String.format("templates/%s.twig", templateName));
        JtwigModel model = JtwigModel.newModel();
        String response = template.render(model);
        sendResponse(exchange, response);
    }

    public void sendTemplateResponseWithList(HttpExchange exchange, String templateName, Map<String, String> content) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(String.format("templates/%s.twig", templateName));
        JtwigModel model = JtwigModel.newModel();
        model.with("guestentry", content);
        String response = template.render(model);
        sendResponse(exchange, response);
    }

    public void sendTemplateResponseWithName(HttpExchange exchange, String templateName, String name) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(String.format("templates/%s.twig", templateName));
        JtwigModel model = JtwigModel.newModel();
        model.with("name", name);
        String response = template.render(model);
        sendResponse(exchange, response);
    }

    public void sendTemplateResponseWithForm(HttpExchange exchange, String templateName, String form){
        JtwigTemplate template = JtwigTemplate.classpathTemplate(String.format("templates/%s.twig", templateName));
        JtwigModel model = JtwigModel.newModel();
        model.with("form", form);
        String response = template.render(model);
        sendResponse(exchange, response);

    }

    public void sendTemplateResponseStudentList(HttpExchange exchange, String templateName, List<Student> students) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(String.format("templates/%s.twig", templateName));
        JtwigModel model = JtwigModel.newModel();
        model.with("students", students);
        String response = template.render(model);
        sendResponse(exchange, response);
    }

    public void sendTemplateResponseWithForm(HttpExchange exchange, String templateName, String form, String form2){
        JtwigTemplate template = JtwigTemplate.classpathTemplate(String.format("templates/%s.twig", templateName));
        JtwigModel model = JtwigModel.newModel();
        model.with("form", form);
        model.with("form2", form2);
        String response = template.render(model);
        sendResponse(exchange, response);

    }

    public Map<String, String> readFormData(HttpExchange exchange) {
        String loginData = "";

        try {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            loginData = br.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return parseFormData(loginData);
    }

    public Map<String, String> parseFormData(String formData) {
        Map<String, String> inputs = new HashMap<>();
        String key;
        String value;
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            key = keyValue[0];

            try {
                value = new URLDecoder().decode(keyValue[1], "UTF-8");
                inputs.put(key, value);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return inputs;
    }


    public boolean isLoggedIn(String sid) {
        return getSessionIdContainer().contains(sid);
    }

    public String getSidFromCookieStr(String cookieStr) {
        HttpCookie cookie = HttpCookie.parse(cookieStr).get(0);
        return cookie.toString().split("=")[1];
    }

//    public User getUserBySession(HttpExchange httpExchange){
//        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
//        String sid = getSidFromCookieStr(cookieStr);
//        return new SQLiteUserDAO().getUserById(sessionIdContainer.getUserId(sid));
//    }

    public String getAction(HttpExchange httpExchange){
        int actionIndex = 2;

        String uri = httpExchange.getRequestURI().toString();
        String[] uriParts = uri.split("/");

        return uriParts[actionIndex];
    }


    public int getUserIDFromURI(HttpExchange httpExchange){
        String uri = httpExchange.getRequestURI().toString();
        String[] uriParts = uri.split("/");
        int userID = -1;
        try {
            userID = Integer.parseInt(uriParts[uriParts.length -1]);

        }catch (NumberFormatException e){
            return userID;

        }
        return userID;

    }
}
