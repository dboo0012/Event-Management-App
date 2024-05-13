package com.fit2081.fit2081_assignment_1.providers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AppDAO {
    // Event Categories
    @Query ("select * from eventCategories")
    LiveData<List<EventCategory>> getAllEventCategories();

    @Query ("SELECT categoryId FROM eventCategories")
    LiveData<List<String>> getAllCategoryIds();

    @Insert
    void addEventCategory(EventCategory eventCategory);

    @Query("delete from eventCategories")
    void deleteAllEventCategory();

    // Events
    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();

    @Insert
    void addEvent(Event event);

    @Query("delete from events")
    void deleteAllEvents();

    @Query("UPDATE eventCategories SET eventCount = :eventCount WHERE categoryId = :categoryId")
    void updateCategoryEventCount(String categoryId, int eventCount);
}
