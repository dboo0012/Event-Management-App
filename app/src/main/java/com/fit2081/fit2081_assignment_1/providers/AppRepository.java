package com.fit2081.fit2081_assignment_1.providers;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {
    private AppDAO appDAO;
    private LiveData<List<Event>> mAllEvents;
    private LiveData<List<EventCategory>> mAllEventCategories;

    AppRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        appDAO = db.appDAO();
        mAllEvents = appDAO.getAllEvents();
        mAllEventCategories = appDAO.getAllEventCategories();
    }

    LiveData<List<Event>> getAllEvents(){
        return mAllEvents;
    }

    LiveData<List<EventCategory>> getAllEventCategories(){
        return mAllEventCategories;
    }
}
