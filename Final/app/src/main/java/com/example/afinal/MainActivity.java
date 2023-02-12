package com.example.afinal;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactViewModel.ShareModel, RecycleFragment.RecycleListener {

    static final int REQUEST_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    static final int REQUEST_PERMISSIONS_REQUEST_SEND_SMS = 1;

    ContactViewModel _model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            recycleViewInitOnStart();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
//        /*  */
//        ContactHistoryManager cm =  new ContactHistoryManager(this);
//        for(int i = 0; i < 5; i++){
//            ContactHistory ch = new ContactHistory();
//            ch.setContact(new Contact("S" + Integer.toString(i),"0000000"));
//            ArrayList<Integer> array = new ArrayList<>();
//            array.add(1);
//            ch.setFactIDsArray(array);
//            ArrayList<String> arrayS = new ArrayList<>();
//            arrayS.add("string");
//            ch.setMessagesArray(arrayS);
//            cm.saveContactHistory(ch);
//        }
//        cm.saveContactHistories();
//        cm = new ContactHistoryManager(this);
//        cm.loadContactHistories();
//
//        cm.getContactHistoryByContactName("S1");
//
//        /*  */
        DataFragment countryDataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("CDF");
        FragmentContainerView fragmentContainerViewDetails = (FragmentContainerView) findViewById(R.id.fragmentContainerView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recycleViewInitOnStart();
                }
                return;
        }
    }

    private void recycleViewInitOnStart(){
        _model = new ViewModelProvider(this).get(ContactViewModel.class);
        _model.initViewModelFromRepository(ContactRepository.getInstance(this));
        getSupportFragmentManager().beginTransaction()
//                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, RecycleFragment.class, null,"RFC")
//                .addToBackStack("BBB")
                .commit();
        getSupportFragmentManager().executePendingTransactions();
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

    //RecycleListener implementation:
    @Override
    public void onClickEvent() {
        DataFragment contactDataFragment;
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, DataFragment.class, null,"CDF")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
//        }
        contactDataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("CDF");
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