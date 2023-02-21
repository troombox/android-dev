package com.example.afinal;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

public class MainActivity extends AppCompatActivity implements ContactViewModel.ShareContactModel, ContactHistoryViewModel.ShareHistoryModel, RecycleFragment.RecycleListener {

    static final int REQUEST_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    static final int REQUEST_PERMISSIONS_REQUEST_SEND_SMS = 1;
    static final int REQUEST_PERMISSIONS_REQUEST_READ_SMS = 2;
    static final int REQUEST_PERMISSIONS_REQUEST_RECEIVE_SMS = 3;

    ContactViewModel _contactViewModel;
    ContactHistoryViewModel _contactHistoryViewModel;

    private UpdatableView _uView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        _contactHistoryViewModel = new ViewModelProvider(this).get(ContactHistoryViewModel.class);
        _contactHistoryViewModel.initViewModelFromFile();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            recycleViewInitOnStart();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        //rewrite
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            requestPermissions(new String[] { Manifest.permission.RECEIVE_SMS }, REQUEST_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            requestPermissions(new String[] { Manifest.permission.READ_SMS }, REQUEST_PERMISSIONS_REQUEST_READ_SMS);
        }
        //
//        DataFragment countryDataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("CDF");
//        FragmentContainerView fragmentContainerViewDetails = (FragmentContainerView) findViewById(R.id.fragmentContainerView);

        Intent intent = getIntent();
        if(intent != null){
            if(intent.getAction().equals("com.example.afinal.ACTION_REMOVE_CONTACT")){
                removeContactUpdatePrefDueToIntent(intent);
                finish();
            }
        }

//        Intent intentSms = new Intent(this, SmsService.class);
//        ContextCompat.startForegroundService(this, intentSms);


    }

    private void removeContactUpdatePrefDueToIntent(Intent intent) {
        String contact = intent.getStringExtra("contact");
        String preference = intent.getStringExtra("preference");
        if(contact == null || preference == null)
            return;
        Toast.makeText(this, contact +" " + preference, Toast.LENGTH_SHORT).show();
        if(preference.equals("DELETE") && _contactViewModel.checkFlagDelete()){
            Contact c = _contactViewModel.findContactByPhone(contact);
            if(c != null)
                _contactViewModel.removeContact(c);
            if(_uView != null)
                _uView.updateView();
            return;
        }
        _contactViewModel.saveContactPreference(contact,preference);
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
        _contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        _contactViewModel.initViewModelFromRepository(ContactRepository.getInstance(this));
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView, RecycleFragment.class, null,"RFC")
                    .commit();

        } else {//landscape
            DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("CDF");
            if(dataFragment != null){
                getSupportFragmentManager().beginTransaction().hide(dataFragment).commitNow();
            }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainerView, RecycleFragment.class, null,"RFC")
                        .add(R.id.fragmentContainerViewDetails, DataFragment.class, null,"CDF").commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }


    @Override
    public ContactViewModel shareContactModel() {
        return _contactViewModel;
    }

    @Override
    public ContactHistoryViewModel shareHistoryModel() {
        return _contactHistoryViewModel;
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

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getAction().equals("com.example.afinal.ACTION_REMOVE_CONTACT")){
                removeContactUpdatePrefDueToIntent(intent);
                return;
            }
        }
    }

    //RecycleListener implementation:
    @Override
    public void onClickEvent() {
        DataFragment contactDataFragment;
        contactDataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("CDF");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, DataFragment.class, null,"CDF")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        } else{
            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerViewDetails, DataFragment.class, null,"CDF")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }

    }

    public void setUpdatableViewInMain(UpdatableView uView){
        this._uView = uView;
    }

    interface UpdatableView{
        public void updateView();
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