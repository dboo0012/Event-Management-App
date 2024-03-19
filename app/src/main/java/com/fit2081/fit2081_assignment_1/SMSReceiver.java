package com.fit2081.fit2081_assignment_1;

import static com.fit2081.fit2081_assignment_1.MainActivity.LOG_KEY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    public static final String SMS_FILTER = "SMS_FILTER";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] incomingMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < incomingMessage.length; i++) {
            SmsMessage currentMessage = incomingMessage[i];
            String message = currentMessage.getDisplayMessageBody(); // Unpack the incoming message
            /*
             * Now, for each new message, send a broadcast contains the new message to MainActivity
             * The MainActivity has to tokenize the new message and update the UI
             * */
            Intent msgIntent = new Intent();
            msgIntent.setAction(SMS_FILTER); // destination
            msgIntent.putExtra(SMS_MSG_KEY, message); // packaged message
            context.sendBroadcast(msgIntent);
            Log.d(LOG_KEY, "launched SMS Broadcast");
        }
    }
}