package com.example.afinal;



import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements ContactViewModel.ShareModel {

    ContactViewModel _model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);

        _model = new ViewModelProvider(this).get(ContactViewModel.class);
        _model.initViewModelFromRepository(new ContactRepository(this));
//        ContactRepository r = new ContactRepository(this);
//        TextView t = (TextView)findViewById(R.id.tv_test);
//        t.setText(((Contact)r.getContactsList().get(0)).getName());
    }

    @Override
    public ContactViewModel shareModel() {
        return _model;
    }
}