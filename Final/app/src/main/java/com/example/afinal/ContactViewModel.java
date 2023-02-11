package com.example.afinal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ContactViewModel extends AndroidViewModel {

    //Live data
    ArrayList<Contact> _contactsArray;
    MutableLiveData<ArrayList<Contact>> _contactsArrayLiveData;
    Integer _selectedPosition;
    MutableLiveData<Integer> _selectedPositionLiveData;

    //
    private Application _app;

    public ContactViewModel(@NonNull Application application) {
        //init
        super(application);
        _app = application;

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
        _contactsArray = cr.getContactsList();
        _contactsArrayLiveData.setValue(_contactsArray);
    }

    public void setSelected(Contact c){
        int pos = _contactsArray.indexOf(c);
        if(pos < 0)
            return;
        if(pos == _selectedPosition){
            _selectedPosition = -1;
        } else{
            _selectedPosition = pos;
        }
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
    }

    public interface ShareModel{
        ContactViewModel shareModel();
    }
}
