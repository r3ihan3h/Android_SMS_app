package com.example.reihaneh_sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.reihaneh_sms.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = this.getClass().getCanonicalName();
    private static final int SMS_RECEIVE_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE},
                SMS_RECEIVE_PERMISSION_REQUEST_CODE);

        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.contacts));
        binding.lvContacts.setAdapter(contactsAdapter);

        binding.lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d(TAG, "onItemClick: Trying to message " + binding.lvContacts.getItemAtPosition(position));

                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                messageIntent.putExtra("EXTRA_CONTACT_NAME", String.valueOf(binding.lvContacts.getItemAtPosition(position)));
                startActivity(messageIntent);

            }
        });
    }
}