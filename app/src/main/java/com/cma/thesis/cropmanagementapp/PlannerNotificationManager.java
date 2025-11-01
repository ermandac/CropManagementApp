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
     * Schedule harvest notification for end date - SIMPLIFIED VERSION
     * Works completely offline!
     * @param planId Unique plan identifier
     * @param cropName Name of the crop
     * @param endDate Harvest date in MM/dd/yyyy format
     */
    public void scheduleHarvestNotification(String planId, String cropName, String cropId, String endDate) {
        try {
            Log.d(TAG, "📅 scheduleHarvestNotification called");
            long notificationTime = calculateHarvestTime(endDate);
            long currentTime = System.currentTimeMillis();
            long delaySeconds = (notificationTime - currentTime) / 1000;
            
            Log.d(TAG, "   Current time: " + new Date(currentTime));
            Log.d(TAG, "   Notification time: " + new Date(notificationTime));
            Log.d(TAG, "   Delay: " + delaySeconds + " seconds");

            // Only schedule if the date is in the future
            if (notificationTime > currentTime) {
                Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
                notificationIntent.putExtra("planId", planId);
                notificationIntent.putExtra("cropName", cropName);
                notificationIntent.putExtra("cropId", cropId);
                notificationIntent.putExtra("endDate", endDate);
                notificationIntent.putExtra("isHarvestNotification", true);

                int requestCode = ("harvest_" + planId).hashCode();
                Log.d(TAG, "   Request code: " + requestCode);
                
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    // Check if we can schedule exact alarms (Android 12+)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (alarmManager.canScheduleExactAlarms()) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                            Log.d(TAG, "✅ Exact alarm scheduled (Android 12+) for " + cropName + " at " + new Date(notificationTime));
                        } else {
                            Log.e(TAG, "❌ Cannot schedule exact alarms - permission denied!");
                            // Fall back to inexact alarm
                            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                            Log.d(TAG, "⚠️ Using inexact alarm instead");
                        }
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                        Log.d(TAG, "✅ Exact alarm scheduled for " + cropName + " at " + new Date(notificationTime));
                    }
                } else {
                    Log.e(TAG, "❌ AlarmManager is null!");
                }
            } else {
                Log.w(TAG, "⚠️ End date is in the past - no notification scheduled");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error scheduling harvest notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculate harvest notification time (8:00 AM on harvest date)
     * 
     * ✅ PRODUCTION MODE: Notifications scheduled for actual harvest date at 8:00 AM
     */
    private long calculateHarvestTime(String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date date = sdf.parse(endDate);
            
            if (date == null) {
                Log.e(TAG, "Failed to parse date: " + endDate);
                return System.currentTimeMillis();
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            
            Log.d(TAG, "✅ Notification scheduled for: " + new Date(calendar.getTimeInMillis()));
            return calendar.getTimeInMillis();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating harvest time: " + e.getMessage());
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }

    /**
     * Cancel harvest notification for a plan
     */
    public void cancelHarvestNotification(String planId) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
                int requestCode = ("harvest_" + planId).hashCode();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);
                Log.d(TAG, "Cancelled harvest notification for plan: " + planId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling harvest notification: " + e.getMessage());
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
            Log.d(TAG, "🔔 BroadcastReceiver.onReceive() CALLED!");
            try {
                String cropName = intent.getStringExtra("cropName");
                String cropId = intent.getStringExtra("cropId");
                String planId = intent.getStringExtra("planId");
                boolean isHarvest = intent.getBooleanExtra("isHarvestNotification", false);
                
                Log.d(TAG, "   cropName: " + cropName);
                Log.d(TAG, "   cropId: " + cropId);
                Log.d(TAG, "   planId: " + planId);
                Log.d(TAG, "   isHarvest: " + isHarvest);

                if (isHarvest && cropName != null) {
                    String title = "🌾 Harvest Time!";
                    String message = "Your " + cropName + " is ready for harvest today!";
                    
                    Log.d(TAG, "   About to send notification...");
                    sendNotification(context, title, message, cropId, cropName.hashCode());
                    Log.d(TAG, "✅ Harvest notification delivered for: " + cropName);
                } else {
                    Log.w(TAG, "⚠️ Not sending notification - isHarvest=" + isHarvest + ", cropName=" + cropName);
                }
            } catch (Exception e) {
                Log.e(TAG, "❌ Error in NotificationBroadcastReceiver: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void sendNotification(android.content.Context context, String title, String message, String cropId, int notificationId) {
            try {
                // Open the crop planner activity with the crop ID
                Intent intent = new Intent(context, Activity_CreatePlannerActivity.class);
                intent.putExtra("passedID", cropId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
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
