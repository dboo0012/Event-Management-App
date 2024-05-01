package com.fit2081.fit2081_assignment_1.providers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Event.class, EventCategory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // database name, this is important as data is contained inside a file named "card_database"
    public static final String APP_DATABASE_NAME = "app_database";
    public abstract AppDAO appDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    // ExecutorService is a JDK API that simplifies running tasks in asynchronous mode.
    // Generally speaking, ExecutorService automatically provides a pool of threads and an API
    // for assigning tasks to it.
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, APP_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
