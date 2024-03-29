package com.example.afinal;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ContactHistoryViewModel extends AndroidViewModel {
    //Live data
    private ArrayList<ContactHistory> _contactHistories;
    private MutableLiveData<ArrayList<ContactHistory>> _contactHistoriesLiveData;

    private final String HISTORY_FILE = "history_file";
    private SharedPreferences _pref;
    private Context _context;

    public ContactHistoryViewModel(@NonNull Application application){
        //init
        super(application);
        _context = application.getBaseContext();
        _contactHistories = new ArrayList<>();
        _pref = PreferenceManager.getDefaultSharedPreferences(application.getBaseContext());
        //prepare data
        _contactHistoriesLiveData = new MutableLiveData<>();
        _contactHistories = new ArrayList<>();
        //set live data
        _contactHistoriesLiveData.setValue(_contactHistories);
    }

    public MutableLiveData<ArrayList<ContactHistory>> getContactHistoriesArrayLiveData() {
        return _contactHistoriesLiveData;
    }

    public void saveContactHistories(){
        saveArrayListToFile(_contactHistories, HISTORY_FILE);
    }

    public void loadContactHistories(){
        _contactHistories = loadArrayListFromFile(HISTORY_FILE);
    }

    public void initViewModelFromFile(){
        boolean flagRememberHistory = _pref.getBoolean("preference_cb_rememberContactsMessageHistory", false);
        if(flagRememberHistory){
            loadContactHistories();
        }
        _contactHistoriesLiveData.setValue(_contactHistories);
        saveContactHistories();
    }

    public ContactHistory getContactHistoryByContactName(String contactName){
        for(ContactHistory ch : _contactHistories){
            if(ch.getContact().getName().equals(contactName)){
                return ch;
            }
        }
        return null;
    }

    public void saveContactHistory(ContactHistory contactHistory){
        int index = -1;
        for(ContactHistory ch : _contactHistories){
            if(ch.getContact().equals(contactHistory.getContact())){
                index = _contactHistories.indexOf(ch);
            }
        }
        if(index >= 0){
            _contactHistories.remove(index);
        }
        _contactHistories.add(contactHistory);
        _contactHistoriesLiveData.setValue(_contactHistories);
    }

    private void saveArrayListToFile(ArrayList<ContactHistory> arrayList, String filename) {
        try {
            FileOutputStream fileOutputStream = _context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            return;
        }
    }

    private ArrayList<ContactHistory> loadArrayListFromFile(String filename) {
        ArrayList<ContactHistory> arrayList = null;

        try {
            FileInputStream fileInputStream = _context.openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arrayList = (ArrayList<ContactHistory>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            return null;
        }

        return arrayList;
    }

    interface ShareHistoryModel{
        ContactHistoryViewModel shareHistoryModel();
    }
}
