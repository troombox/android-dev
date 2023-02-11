package com.example.afinal;



import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

public class MainActivity extends AppCompatActivity implements ContactViewModel.ShareModel {

    ContactViewModel _model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);

        _model = new ViewModelProvider(this).get(ContactViewModel.class);
        _model.initViewModelFromRepository(new ContactRepository(this));
    }

    @Override
    public ContactViewModel shareModel() {
        return _model;
    }

    //Menu + options:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_xml, menu); // show the menu bar
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                getSupportFragmentManager().beginTransaction().add(android.R.id.content, new MyPreferences()).addToBackStack(null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyPreferences extends PreferenceFragmentCompat{


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(getResources().getColor(android.R.color.white));
            return view;
        }

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.my_preference, rootKey);
        }
    }
}