package com.example.afinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private static final String SMS_DESTINATION = "0548054078"; // replace with the phone number you want to send the SMS to

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Send the SMS message
        String phone = getInputData().getString("phone");
        String fact  = getInputData().getString("fact");
        if(phone == null || fact == null){
            return Result.failure();
        }
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, fact, null, null);
        } catch (Exception e) {
            return Result.failure();
        }

//        // Show a notification to indicate that the work is running
//        showNotification("Sent SMS message");

        return Result.success();
    }

//    private void showNotification(String message) {
//        // Create a notification channel for the notification
//        NotificationChannel channel = new NotificationChannel(
//                CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
//        NotificationManager notificationManager = (NotificationManager)
//                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(channel);
//
//        // Create the notification and show it
//        Notification notification = new NotificationCompat.Builder(
//                getApplicationContext(), CHANNEL_ID)
//                .setContentTitle("My App")
//                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .build();
//        notificationManager.notify(NOTIFICATION_ID, notification);
//    }

//    public static void scheduleWeekly(Context context) {
//        // Create a periodic work request to run the worker every week
//        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
//                MyWorker.class, 7, TimeUnit.DAYS)
//                .build();
//
//        // Enqueue the work request with WorkManager
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//                "my_weekly_work",
//                ExistingPeriodicWorkPolicy.KEEP,
//                workRequest);
//
//        // Start the foreground service to keep the app running in the background
//        startForegroundService(context);
//    }
//
//    private static void startForegroundService(Context context) {
//        Intent serviceIntent = new Intent(context, MyForegroundService.class);
//        ContextCompat.startForegroundService(context, serviceIntent);
//    }
}

