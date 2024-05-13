package com.fit2081.fit2081_assignment_1.providers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<EventCategory>> mAllEventCategories;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllEventCategories = mRepository.getAllEventCategories();
    }
    public LiveData<List<EventCategory>> getAllEventCategories() {
        return mAllEventCategories;
    }
    public void addEventCategory(EventCategory eventCategory){
        mRepository.addEventCategory(eventCategory);
    }
    public void deleteAllEventCategory(){
        mRepository.deleteAllEventCategory();
    }
}
