package com.example.afinal;

import android.telephony.SmsManager;

public class SmsSender {

    private final String PREFERENCE_MESSAGE = "Hi, do you want to get DOG FACTS or CAT FACTS?\n" +
            "Respond to this message with the word DOG or CAT accordingly!\nRespond DELETE if you don't" +
            "want to receive any messages.";

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

    public void sendPreferencesRequestSms(String phoneNum){
        String phoneNumber = "0548054078";
        if(phoneNum != null){
            phoneNumber = phoneNum;
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, PREFERENCE_MESSAGE,null,null);
    }

}
