package com.codecool.database;

import com.codecool.database.model.GuestEntry;

import java.util.Iterator;
import java.util.List;

public class guestEntryIterator implements Iterator<GuestEntry> {

    private final int DISPLAY_LIMIT = 5;

    private List<GuestEntry> list;
    private int index;

    public guestEntryIterator(List<GuestEntry> list) {
        this.list = list;
        this.index = 0;
    }

    public boolean hasNext() {
        return index < list.size() && index < DISPLAY_LIMIT;
    }

    public GuestEntry next() {
        return list.get(index++);
    }
}
