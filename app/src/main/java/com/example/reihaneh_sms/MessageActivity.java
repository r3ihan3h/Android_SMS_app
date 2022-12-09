package com.example.reihaneh_sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reihaneh_sms.databinding.ActivityMainBinding;
import com.example.reihaneh_sms.databinding.ActivityMessageBinding;

import kotlin.jvm.internal.Ref;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    private final String TAG = this.getClass().getCanonicalName();
    private String contactName;

    public static final String SENT = "SMS_SENT";
    public static final String DELIVERED = "SMS_DELIVERED";

    private PendingIntent sentPI, deliveredPI;
    private BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    private IntentFilter intentFilter;
    private BroadcastReceiver intentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.contactName = extras.getString("EXTRA_CONTACT_NAME" , "NA");
        }

        binding.tvChat.setText("Chatting with " + contactName);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
    }

    private void sendSMS(){
        String phoneNumber = binding.editPhone.getText().toString();
        String message = binding.editMessage.getText().toString();

        Log.d(TAG, "sendSMS: Trying to send message to " + contactName + "on " + phoneNumber);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

    }

    @Override
    protected void onResume(){
        super.onResume();

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE);

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: SMS snet with code " + getResultCode());

                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Log.d(TAG, "onReceive: SMS sent successfully!");
                        Toast.makeText(MessageActivity.this, "SMS sent", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.d(TAG, "onReceive: SMS not sent - Generic Failure");
                        break;

                    case SmsManager.RESULT_CANCELLED:
                        Log.d(TAG, "onReceive: SMS not sent - No Service Available");
                        break;

                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: SMS Delivered with code " + getResultCode());


                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Log.d(TAG, "onReceive: SMS delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d(TAG, "onReceive: SMS delivery cancelled");
                        break;
                }
            }
        };
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: SMS Received : " + intent.getStringExtra("EXTRA_SMS"));
                binding.tvChat.setText(binding .tvChat.getText().toString() + "/n" + intent.getStringExtra("EXTRA_SMS"));

            }
        };
        registerReceiver(intentReceiver,intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(intentReceiver);
    }
}