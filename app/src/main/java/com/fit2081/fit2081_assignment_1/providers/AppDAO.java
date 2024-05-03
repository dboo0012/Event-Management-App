package com.fit2081.fit2081_assignment_1.providers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AppDAO {
    @Query ("select * from eventCategories")
    LiveData<List<EventCategory>> getAllEventCategories();

    @Insert
    void addEventCategory(EventCategory eventCategory);

    @Query("delete from eventCategories")
    void deleteAllEventCategory();

    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();
}
