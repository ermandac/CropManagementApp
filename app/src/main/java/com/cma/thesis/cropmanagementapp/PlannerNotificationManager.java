package com.cma.thesis.cropmanagementapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * PlannerNotificationManager
 * Manages scheduling and sending notifications for crop planning steps
 * Uses AlarmManager to schedule notifications at specific dates
 */
public class PlannerNotificationManager {
    private static final String TAG = "PlannerNotificationManager";
    private static final String CHANNEL_ID = "crop_plan_notifications";
    private static final String CHANNEL_NAME = "Crop Plan Notifications";
    private Context context;

    public PlannerNotificationManager(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    /**
     * Create notification channel (required for Android 8.0+)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription("Notifications for crop cultivation steps");
            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Schedule notifications for all steps of a plan
     */
    public void scheduleNotificationsForPlan(String planId, int cropId, String startDate,
                                             ArrayList<Class_Procedure> steps) {
        if (steps == null || steps.isEmpty()) {
            Log.w(TAG, "No steps to schedule for plan: " + planId);
            return;
        }

        try {
            for (Class_Procedure step : steps) {
                scheduleStepNotification(planId, cropId, startDate, step);
            }
            Log.d(TAG, "Scheduled " + steps.size() + " notifications for plan: " + planId);
        } catch (Exception e) {
            Log.e(TAG, "Error scheduling notifications: " + e.getMessage());
        }
    }

    /**
     * Schedule notification for a single step
     */
    private void scheduleStepNotification(String planId, int cropId, String startDate,
                                          Class_Procedure step) {
        try {
            long notificationTime = calculateNotificationTimeMillis(startDate, step.getDaysNotif());

            if (notificationTime > System.currentTimeMillis()) {
                Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
                notificationIntent.putExtra("planId", planId);
                notificationIntent.putExtra("cropId", cropId);
                notificationIntent.putExtra("stepId", step.getId());
                notificationIntent.putExtra("stepNumber", step.getStep());
                notificationIntent.putExtra("stepName", step.getProcedure());

                int requestCode = (planId + "_" + step.getId()).hashCode();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                    Log.d(TAG, "Scheduled notification for step " + step.getStep() +
                            " at " + new Date(notificationTime));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error scheduling step notification: " + e.getMessage());
        }
    }

    /**
     * Calculate notification time in milliseconds from current time to notification date
     */
    private long calculateNotificationTimeMillis(String startDate, int daysToAdd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date date = sdf.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

            return calendar.getTimeInMillis();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating notification time: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    /**
     * Cancel notifications for a plan
     */
    public void cancelNotificationsForPlan(String planId, ArrayList<Class_Procedure> steps) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && steps != null) {
                for (Class_Procedure step : steps) {
                    Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
                    int requestCode = (planId + "_" + step.getId()).hashCode();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.cancel(pendingIntent);
                }
            }
            Log.d(TAG, "Cancelled notifications for plan: " + planId);
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling notifications: " + e.getMessage());
        }
    }

    /**
     * Send immediate notification (for testing or manual trigger)
     */
    public void sendImmediateNotification(String title, String message, int notificationId) {
        try {
            Intent intent = new Intent(context, Activity_CreatePlannerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(notificationId, builder.build());
                Log.d(TAG, "Sent immediate notification: " + title);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending immediate notification: " + e.getMessage());
        }
    }

    /**
     * Broadcast receiver for alarm notifications
     * This should be registered in AndroidManifest.xml
     */
    public static class NotificationBroadcastReceiver extends android.content.BroadcastReceiver {
        private static final String TAG = "NotificationBroadcastReceiver";

        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            try {
                String planId = intent.getStringExtra("planId");
                String stepName = intent.getStringExtra("stepName");
                int stepNumber = intent.getIntExtra("stepNumber", 0);

                String title = "Crop Plan Reminder - Step " + stepNumber;
                String message = stepName;

                sendNotification(context, title, message, stepNumber);
                Log.d(TAG, "Notification delivered for step: " + stepNumber);
            } catch (Exception e) {
                Log.e(TAG, "Error in NotificationBroadcastReceiver: " + e.getMessage());
            }
        }

        private void sendNotification(android.content.Context context, String title, String message, int notificationId) {
            try {
                Intent intent = new Intent(context, Activity_CreatePlannerActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[]{0, 250, 250, 250})
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationId, builder.build());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error sending notification: " + e.getMessage());
            }
        }
    }
}
