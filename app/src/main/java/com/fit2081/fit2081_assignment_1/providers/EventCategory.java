package com.fit2081.fit2081_assignment_1.providers;

public class EventCategory {
    private String categoryId;
    private String categoryName;
    private int eventCount;
    private boolean isCategoryActive;
    public EventCategory(String categoryId, String categoryName, int eventCount, boolean isCategoryActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.eventCount = eventCount;
        this.isCategoryActive = isCategoryActive;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getEventCount() {
        return eventCount;
    }

    public boolean isCategoryActive() {
        return isCategoryActive;
    }
}
