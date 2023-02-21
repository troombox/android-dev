package com.example.afinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import java.util.Locale;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                    + "Display message body: " + smsMessage.getDisplayMessageBody()
                    + "Message: " + smsMessage.getMessageBody();
            Toast.makeText(context, "New Message Received: " + message, Toast.LENGTH_LONG).show();
            String pref;
            if(smsMessage.getMessageBody().toLowerCase(Locale.ROOT).equals("dog")){
                pref = "DOG";
            }else if(smsMessage.getMessageBody().toLowerCase(Locale.ROOT).equals("cat")){
                pref = "CAT";
            }else if(smsMessage.getMessageBody().toLowerCase(Locale.ROOT).equals("delete")){
                pref = "DELETE";
            }
            else
                return;
            Intent newIntent = new Intent(context, MainActivity.class);
            newIntent.putExtra("contact", smsMessage.getDisplayOriginatingAddress());
            newIntent.putExtra("preference", pref);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.setAction("com.example.afinal.ACTION_REMOVE_CONTACT");
            context.startActivity(newIntent);
        }
    }
}
