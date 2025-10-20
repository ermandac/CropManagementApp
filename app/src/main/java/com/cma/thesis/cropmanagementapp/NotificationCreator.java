package com.cma.thesis.cropmanagementapp;

import android.app.NotificationManager;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

/**
 * Created by Manzano on 12/5/2018.
 */

public class NotificationCreator extends AppCompatActivity {

    public void SetNotification()
    {
        NotificationCompat.Builder notificationbuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Sucess")
                .setContentText("You have successfully created a plan");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationbuilder.build());
    }
}
