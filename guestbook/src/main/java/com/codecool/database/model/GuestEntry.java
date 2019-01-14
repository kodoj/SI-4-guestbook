package com.codecool.database.model;

public class GuestEntry {

    private final String name;
    private final String message;
    private final String date;

    public GuestEntry(String name, String message, String date) {
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String toHTMLTag() {
        String result = "<personaldata>" +
                name + "</br>" +
                message + "</br>" +
                date + "</br>";

        return result;
    }

    public String toString() {
        return this.name + "\n" + this.message + "\n" + this.date;
    }
}
