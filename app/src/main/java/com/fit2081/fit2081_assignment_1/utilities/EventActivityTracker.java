package com.fit2081.fit2081_assignment_1.utilities;

public class EventActivityTracker{
    private static boolean activityVisible;
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}