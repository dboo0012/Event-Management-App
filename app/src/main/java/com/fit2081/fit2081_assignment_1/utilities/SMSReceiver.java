package com.fit2081.fit2081_assignment_1.utilities;

import static com.fit2081.fit2081_assignment_1.activities.MainActivity.LOG_KEY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    public static final String EVENT_SMS_FILTER = "EVENT_SMS_FILTER";
    public static final String EVENT_CATEGORY_SMS_FILTER = "EVENT_CATEGORY_SMS_FILTER";
    public static final String SMS_FILTER = "SMS_FILTER";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] incomingMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        String destinationFilter = null;
        for (int i = 0; i < incomingMessage.length; i++) {
            SmsMessage currentMessage = incomingMessage[i];
            String message = currentMessage.getDisplayMessageBody(); // Unpack the incoming message
            Intent msgIntent = new Intent();

            // Pass the sms to its corresponding context
            if (message.startsWith("category:")) {
                destinationFilter = EVENT_CATEGORY_SMS_FILTER;

            } else if (message.startsWith("event:")) {
                destinationFilter = EVENT_SMS_FILTER;
            }
            msgIntent.setAction(destinationFilter); // destination
            msgIntent.putExtra(SMS_MSG_KEY, message); // packaged message
            context.sendBroadcast(msgIntent); // Send broadcast
            Log.d(LOG_KEY, "launched SMS Broadcast");
        }
    }
}