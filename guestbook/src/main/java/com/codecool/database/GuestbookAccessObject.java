package com.codecool.database;

import com.codecool.database.model.GuestEntry;
import com.codecool.database.psql.SQLQueryHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuestbookAccessObject implements IDAO {
    @Override
    public List<GuestEntry> loadGuestbook() {
        try {
            String query = "SELECT name, msg, date FROM guest_entry order by entry_id desc";

            ResultSet guestbookContent = SQLQueryHandler.getInstance().executeQuery(query);

            List<GuestEntry> result = new ArrayList<>();
            String name, message, date;

            while(guestbookContent.next()) {
                name = guestbookContent.getString(1);
                message = guestbookContent.getString(2);
                date = guestbookContent.getString(3);
                result.add(new GuestEntry(name, message, date));
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addGuestEntry(GuestEntry entry) {
        String name = entry.getName();
        String msg = entry.getMessage();
        String date = entry.getDate();

        String query = "insert into guest_entry values (DEFAULT, ? , ? , ?);";

        try {
            Connection c = SQLQueryHandler.getInstance().getConnection();

            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, msg);
            statement.setString(3, date);

            query = statement.toString();
            SQLQueryHandler.getInstance().executeQuery(query);

        } catch (SQLException e) {

        }
    }

}