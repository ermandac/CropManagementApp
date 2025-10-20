package com.cma.thesis.cropmanagementapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AlarmReciever extends BroadcastReceiver{
    private static final String CHANNEL_ID = "cropmanagementapp";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent notificationIntent = new Intent(context, Activity_PlannerList.class);
//
//
//
       String value=intent.getStringExtra("procedure");
        //Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(Activity_PlannerList.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(context);
//
//        Notification notification = builder.setContentTitle(value)
//                .setContentText(Activity_Crop_PlanDetails.passString)
//                .setTicker(value)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent).build();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setChannelId(CHANNEL_ID);
//        }
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "NotificationDemo",
//                    IMPORTANCE_DEFAULT
//            );
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notification);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentgo = new Intent(context,Activity_PlannerList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intentgo,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle(value)
                .setContentText(Activity_Crop_PlanDetails.passString)
                .setTicker(value)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(100,builder.build());


    }
}
