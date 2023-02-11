package com.example.afinal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SmsSender {

//    private Activity _act;
//
//    public SmsSender(Activity activity){
//        _act = activity;
//    }

    public void sendSms(String phoneNum, String smsText){
        //setup for sms data
        String phoneNumber = "0548054078";
        String message = "Test";
        if(phoneNum != null){
            phoneNumber = phoneNum;
        }
        if(smsText != null){
            message = smsText;
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message,null,null);

//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse("smsto:"));
//        i.setType("vnd.android-dir/mms-sms");
//        i.putExtra("address", phoneNumber);
//        i.putExtra("sms_body",message);
//        _act.startActivity(Intent.createChooser(i, "Send sms via:"));
    }

}
