package com.codecool.database.psql;

import java.sql.Connection;
import java.sql.DriverManager;

class Connector {

    private Connection connection;

    Connector () {
        Connection c;
        try {
            Class.forName("org.postgresql.Driver");
            String dbUser = "kodojpass";
            String dbPassword = "pass";
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/guestbook", dbUser, dbPassword);
            connection = c;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    Connection getConnection() {
        return connection;
    }
}
