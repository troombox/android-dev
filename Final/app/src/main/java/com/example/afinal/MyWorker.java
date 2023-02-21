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

        return Result.success();
    }
}

