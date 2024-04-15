package com.fit2081.fit2081_assignment_1.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefRestore {
    private Context context;
    public SharedPrefRestore(Context context) {
        this.context = context;
    }
    public String restoreData(String sharedPrefFile, String dataKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        return sharedPreferences.getString(dataKey, "");
    }
}
