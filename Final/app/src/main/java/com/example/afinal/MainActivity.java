package com.example.afinal;



import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
//        ContactRepository r = new ContactRepository(this);
//        TextView t = (TextView)findViewById(R.id.tv_test);
//        t.setText(((Contact)r.getContactsList().get(0)).getName());
    }
}