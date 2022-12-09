package com.example.reihaneh_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "";

        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            for(SmsMessage msg : messages){
                message += msg.getOriginatingAddress() + " : " + msg.getMessageBody();
                Log.d(TAG, "onReceive: SMS Received : " + message);
            }
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SMS_RECEIVED_ACTION");
        broadcastIntent.putExtra("EXTRA_SMS", message);
        context.sendBroadcast(broadcastIntent);
    }
}
