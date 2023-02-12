package com.example.afinal;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ContactHistoryManager {

    private final String HISTORY_FILE = "history_file";

    private Context _context;
    private ArrayList<ContactHistory> _contactHistories;

    public ContactHistoryManager(Context context){
        _context = context;
        _contactHistories = new ArrayList<>();
    }

    public void saveContactHistories(){
        saveArrayListToFile(_contactHistories, HISTORY_FILE);
    }

    public void loadContactHistories(){
        _contactHistories = loadArrayListFromFile(HISTORY_FILE);
    }

    public ContactHistory getContactHistoryByContactName(String contactName){
        for(ContactHistory ch : _contactHistories){
            if(ch.getContact().getName().equals(contactName)){
                return ch;
            }
        }
        return new ContactHistory();
    }

    public void saveContactHistory(ContactHistory contactHistory){
        _contactHistories.add(contactHistory);
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




}
