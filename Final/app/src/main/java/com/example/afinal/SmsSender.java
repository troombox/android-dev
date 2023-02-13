package com.example.afinal;

import android.telephony.SmsManager;

public class SmsSender {

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
    }

}
