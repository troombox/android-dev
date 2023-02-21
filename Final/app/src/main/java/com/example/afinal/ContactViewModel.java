package com.example.afinal;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ContactViewModel extends AndroidViewModel {

    //Live data
    private ArrayList<Contact> _contactsArray;
    private MutableLiveData<ArrayList<Contact>> _contactsArrayLiveData;
    private Integer _selectedPosition;
    private MutableLiveData<Integer> _selectedPositionLiveData;

    //
    private SharedPreferences _pref;


    public ContactViewModel(@NonNull Application application) {
        //init
        super(application);
        _pref = PreferenceManager.getDefaultSharedPreferences(application.getBaseContext());
        //prepare data
        _contactsArrayLiveData = new MutableLiveData<>();
        _selectedPositionLiveData = new MutableLiveData<>();
        _contactsArray = new ArrayList<>();
        _selectedPosition = Integer.valueOf(-1);
        //set live data
        _contactsArrayLiveData.setValue(_contactsArray);
        _selectedPositionLiveData.setValue(_selectedPosition);
    }

    public MutableLiveData<ArrayList<Contact>> getContactsArrayLiveData() {
        return _contactsArrayLiveData;
    }

    public MutableLiveData<Integer> getSelectedPositionLiveData() {
        return _selectedPositionLiveData;
    }

    public void initViewModelFromRepository(ContactRepository cr){
        boolean flagLoadTestData = _pref.getBoolean("preference_test_sw_loadTestData", false);

        if(flagLoadTestData){
            _contactsArray = cr.getTestContactData();
        } else {
            _contactsArray = cr.getContactsList();
        }

        boolean flagRememberRemoved = _pref.getBoolean("preference_cb_rememberContactsRemoved", false);

        if(flagRememberRemoved){
            String contactDataSaved = _pref.getString("contactDataSaved","");
            _contactsArray = editArrayByGivenString(_contactsArray, contactDataSaved);
        }

        _contactsArrayLiveData.setValue(_contactsArray);
        saveToSharedPref("contactDataSaved", contactsArrayToContactsString(_contactsArray));

        //preference:
        boolean flagRememberPreferences = _pref.getBoolean("preference_cb_rememberContactsPreference",false);
        if(!flagRememberPreferences){
            for(Contact c : _contactsArray){
                saveToSharedPref(c.getName()+"_preference","");
            }
        }
    }

    public void setSelected(Contact c){
        int pos = _contactsArray.indexOf(c);

        if(pos < 0)
            return;

//        if(pos == _selectedPosition){
//            _selectedPosition = -1;
//        } else{
//            _selectedPosition = pos;
//        }
        _selectedPosition = pos;
        _selectedPositionLiveData.setValue(_selectedPosition);
    }

    public void removeContact(Contact c) {
        int pos = _contactsArray.indexOf(c);
        if(pos < 0)
            return;
        if(pos == _selectedPosition) {
            _selectedPosition = -1;
            _selectedPositionLiveData.setValue(_selectedPosition);
        }

        _contactsArray.remove(pos);
        _contactsArrayLiveData.setValue(_contactsArray);
        saveToSharedPref("contactDataSaved", contactsArrayToContactsString(_contactsArray));
    }

    public Contact getContactByPosition(int position){
        if(position < 0 ){
            return _contactsArray.get(0);
        }
        return _contactsArray.get(position);
    }

    public Contact findContactByPhone(String phoneNumber) {
        String phoneNumPrefix = phoneNumber;
        String phoneNumNoPrefix = phoneNumber;
        if (phoneNumPrefix.startsWith("+972")) {
            phoneNumNoPrefix = "0" + phoneNumPrefix.substring(4);
        }
        Contact c = null;
        for(Contact c1 : _contactsArray){
            if(c1.getPhoneNumber().equals(phoneNumPrefix) || c1.getPhoneNumber().equals(phoneNumNoPrefix)){
                c = c1;
            }
        }
        return  c;
    }

    public interface ShareContactModel {
        ContactViewModel shareContactModel();
    }
    private String contactsArrayToContactsString(ArrayList<Contact> contactsArray){
        StringBuilder r = new StringBuilder();

        for(Contact c: contactsArray){
            r.append(c.getName());
            r.append(",");
        }

        return r.toString();
    }

    private ArrayList<Contact> editArrayByGivenString(ArrayList<Contact> contactsArray, String contactsString){
        ArrayList<Contact> result = new ArrayList<>();
        String sp[] = contactsString.split(",");
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(sp));

        for (Contact c : contactsArray){
            if(list.contains(c.getName())){
                result.add(c);
            }
        }
        return result;
    }

    private void saveToSharedPref(String key, String value){
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getContactPreference(Contact c){
//        boolean flagRememberPreferences = _pref.getBoolean("preference_cb_rememberContactsPreference",false);
//        if(!flagRememberPreferences)
//            return "";
        return _pref.getString(c.getName() + "_preference","");
    }

    public void saveContactPreference(String phoneNumber, String preference){
        String phoneNumPrefix = phoneNumber;
        String phoneNumNoPrefix = phoneNumber;
        if (phoneNumPrefix.startsWith("+972")) {
            phoneNumNoPrefix = "0" + phoneNumPrefix.substring(4);
        }
        Contact c = null;
        for(Contact c1 : _contactsArray){
            if(c1.getPhoneNumber().equals(phoneNumPrefix) || c1.getPhoneNumber().equals(phoneNumNoPrefix)){
                c = c1;
            }
        }
        if(c == null)
            return;
        saveToSharedPref(c.getName()+"_preference",preference);
    }

    public boolean checkFlagAutoSend(){
        return _pref.getBoolean("preference_cb_autoSend", false);
    }

    public boolean checkFlagDelete(){
        return _pref.getBoolean("preference_cb_allowDelete", false);
    }
}
