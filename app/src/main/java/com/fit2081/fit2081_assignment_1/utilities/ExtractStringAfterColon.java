package com.fit2081.fit2081_assignment_1.utilities;

public class ExtractStringAfterColon {
    public static String extract (String string){
        int colonIndex = string.indexOf(":"); // Find the index of substring
        if (colonIndex != -1) {
            return string.substring(colonIndex + 1); // Return any string after the colon
        } else {
            return ""; // Sub-string is empty
        }
    }
}
