package com.jacky.dateCounter.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private long timestamp;
    private String description;
    private boolean dateOnly;
    private boolean starred;

    public Event(
            String name,
            long timestamp,
            String description,
            boolean dateOnly
    ) {
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
        this.dateOnly = dateOnly;
        this.starred = false;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDateOnly() {
        return dateOnly;
    }

    public void setDateOnly(boolean dateOnly) {
        this.dateOnly = dateOnly;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
