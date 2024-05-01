package com.fit2081.fit2081_assignment_1.providers;

public class Event {
    private  String eventId;
    private String eventName;
    private String categoryId;
    private int ticketsAvailable;
    private boolean isEventActive;
    public Event(String eventId, String categoryId, String eventName, int ticketsAvailable,boolean isEventActive) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.categoryId = categoryId;
        this.ticketsAvailable = ticketsAvailable;
        this.isEventActive = isEventActive;
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
}
