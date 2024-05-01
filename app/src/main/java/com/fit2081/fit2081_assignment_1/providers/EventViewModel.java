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
    }
}
