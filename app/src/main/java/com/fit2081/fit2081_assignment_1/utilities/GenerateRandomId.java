package com.fit2081.fit2081_assignment_1.utilities;

import java.util.Random;

public class GenerateRandomId {
    public static String generateRandomUpperString(int stringLength){
        StringBuilder res = new StringBuilder();
        Random random = new Random();

        // Iterate the required amount of letters
        for (int i=0; i < stringLength; i++){
            // Choose a random letter between A-Z
            int randomInt = random.nextInt(26);
            char randomChar = (char) ('A' + randomInt);
            res.append(randomChar);
        }

        return res.toString();
    }

    public static String generateRandomInt(int intLength){
        StringBuilder res = new StringBuilder();
        Random random = new Random();

        for (int i=0; i < intLength; i++){
            // Choose a random digit between 0-9
            int randomInt = random.nextInt(10);
            res.append(randomInt);
        }
        return res.toString();
    }
}
