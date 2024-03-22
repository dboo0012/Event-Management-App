package com.fit2081.fit2081_assignment_1.Trackers;

import static com.fit2081.fit2081_assignment_1.activities.MainActivity.LOG_KEY;

import android.util.Log;

import java.util.HashMap;

public class BroadcastTracker {
    /**
     * Store a map of classes with a boolean value that represents the status of
     * the broadcast receivers.
     */

    private static final HashMap<Class<?>, Boolean> broadcastStatusMap = new HashMap<>();

    public static boolean isBroadcastActive(Class<?> className) {
        Boolean isActive = broadcastStatusMap.get(className);
        return isActive != null && isActive;
    }

    public static void createBroadcastReceiver(Class<?> className) {
        broadcastStatusMap.put(className, true);
        Log.d(LOG_KEY, "New BroadCastReceiver created for " + className);
    }

    public static void killedBroadcastReceiver(Class<?> className) {
        broadcastStatusMap.remove(className);
    }
}