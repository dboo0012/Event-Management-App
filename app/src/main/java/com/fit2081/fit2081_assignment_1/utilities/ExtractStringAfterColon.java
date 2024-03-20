package com.fit2081.fit2081_assignment_1.utilities;

/**
 * Extracts and returns the substring after a colon in a string.
 */
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
