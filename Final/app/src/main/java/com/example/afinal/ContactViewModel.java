package com.example.afinal;

import android.app.Application;

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
    private ContactRepository _cRep;

    public ContactViewModel(@NonNull Application application) {
        //init
        super(application);
        _app = application;

        //prepare data
        _cRep = new ContactRepository(_app);
        _contactsArrayLiveData = new MutableLiveData<>();
        _selectedPositionLiveData = new MutableLiveData<>();
        _contactsArray = _cRep.getContactsList();
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
}
