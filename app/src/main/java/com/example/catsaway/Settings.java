package com.example.catsaway;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.NotificationCompat;


import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    FloatingActionButton fab;
    private MyAdapter adapter;
    private Context mContext;
//    public static final String PREFS = "examplePrefs";
//    final SharedPreferences examplePrefs = getSharedPreferences(PREFS,0);
//    final SharedPreferences.Editor editor = examplePrefs.edit();

    public final int[] Notification_priority = new int[1];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
//        Switch Notification_switch = (Switch) findViewById(R.id.NotiSwitch);


//        Notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                    Notification_priority[0] = NotificationCompat.PRIORITY_HIGH;
//
//                } else {
//                    // The toggle is disabled
//                    Notification_priority[0] = NotificationCompat.PRIORITY_LOW;
//                }
//            }
//        });
        // Create a notification builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("Test Message")
                .setContentText("message successful")
                .setPriority(Notification_priority[0]);

        // Create a notification manager object
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel
        createNotificationChannel(notificationManager);

        // To see the message in logcat
        Log.i("Notify", builder.toString());

        // Issue the notification
        notificationManager.notify(1, builder.build());

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
                               {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        );

    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        // Create a notification channel
        NotificationChannel channel = new NotificationChannel("Main_setting", "Main_channel", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("CatsAway_Notification_Settings");

        // Register the channel with the system
        notificationManager.createNotificationChannel(channel);
    }


}
