package com.fit2081.fit2081_assignment_1.utilities;

import static com.fit2081.fit2081_assignment_1.activities.MainActivity.LOG_KEY;

import android.util.Log;

import java.util.HashMap;

public class BroadcastTracker {
    /**
     * Store a map of classes with a boolean value that represents the status of
     * the broadcast receivers.
     */

    private static final HashMap<Class<?>, Boolean> activityStatusMap = new HashMap<>();

    public static boolean isBroadcastActive(Class<?> className) {
        Boolean isActive = activityStatusMap.get(className);
        return isActive != null && isActive;
    }

    public static void createBroadcastReceiver(Class<?> className) {
        activityStatusMap.put(className, true);
        Log.d(LOG_KEY, "New BroadCastReceiver created");
    }

    public static void killedBroadcastReceiver(Class<?> className) {
        activityStatusMap.remove(className);
    }
}