package com.cma.thesis.cropmanagementapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Manzano on 11/30/2018.
 */


public class ServiceAlarm extends Service {
    String titles = "";
    Intent intent = new Intent();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent broadcastedIntent=new Intent(this, AlarmReciever.class);
        broadcastedIntent.putExtra("xtest","xxxx" );
        sendBroadcast(broadcastedIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}