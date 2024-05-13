package com.fit2081.fit2081_assignment_1.providers;

import android.app.Application;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<Event>> mAllEvents;
    public EventViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllEvents = mRepository.getAllEvents();
    }
    public LiveData<List<Event>> getAllEvents (){
        return mAllEvents;
    }
    public LiveData<List<EventCategory>> getAllEventCategories(){
        return mRepository.getAllEventCategories();
    }

    public void addEvent(Event event){
        mRepository.addEvent(event);
    }

    public void deleteAllEvents(){
        mRepository.deleteAllEvents();
    }

    public LiveData<List<String>> getAllCategoryIds(){
        return mRepository.getAllCategoryIds();
    }

    public void updateCategoryEventCount(String categoryId, int eventCount){
        mRepository.updateCategoryEventCount(categoryId, eventCount);
    }
}
