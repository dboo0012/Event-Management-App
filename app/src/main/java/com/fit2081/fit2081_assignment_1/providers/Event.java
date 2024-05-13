package com.fit2081.fit2081_assignment_1.providers;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Event.TABLE_NAME)
public class Event {
    public static final String TABLE_NAME = "events";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "eventId")
    private  String eventId;

    @ColumnInfo(name = "eventName")
    private String eventName;

    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "ticketsAvailable")
    private int ticketsAvailable;

    @ColumnInfo(name = "isEventActive")
    private boolean isEventActive;

    public Event(String eventId, String categoryId, String eventName, int ticketsAvailable, boolean isEventActive) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.categoryId = categoryId;
        this.ticketsAvailable = ticketsAvailable;
        this.isEventActive = isEventActive;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public void setEventActive(boolean eventActive) {
        isEventActive = eventActive;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public boolean isEventActive() {
        return isEventActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
