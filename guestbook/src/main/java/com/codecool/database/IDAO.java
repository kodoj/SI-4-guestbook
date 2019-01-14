package com.codecool.database;

import com.codecool.database.model.GuestEntry;

import java.util.List;

public interface IDAO {

    List<GuestEntry> loadGuestbook();
    void addGuestEntry(GuestEntry entry);
}
