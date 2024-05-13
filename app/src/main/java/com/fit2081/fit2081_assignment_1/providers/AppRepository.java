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

    // Event Category
    // Read category

    LiveData<List<EventCategory>> getAllEventCategories(){
        return mAllEventCategories;
    }
    LiveData<List<String>> getAllCategoryIds(){return appDAO.getAllCategoryIds();}

    // Save new category
    void addEventCategory(EventCategory eventCategory){
        AppDatabase.databaseWriteExecutor.execute(()->{
            appDAO.addEventCategory(eventCategory);
        });
    }

    // Delete all category
    void deleteAllEventCategory(){
        AppDatabase.databaseWriteExecutor.execute(()->{
            appDAO.deleteAllEventCategory();
        });
    }

    // Events
    LiveData<List<Event>> getAllEvents(){
        return mAllEvents;
    }

    // Save new event
    void addEvent(Event event){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            appDAO.addEvent(event);
        });
    }

    // Delete all events
    void deleteAllEvents(){
        AppDatabase.databaseWriteExecutor.execute(()->{
            appDAO.deleteAllEvents();
        });
    }

    void updateCategoryEventCount(String categoryId, int eventCount){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            appDAO.updateCategoryEventCount(categoryId, eventCount);
        });
    }
}
