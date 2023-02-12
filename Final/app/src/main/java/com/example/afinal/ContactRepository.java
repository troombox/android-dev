package com.example.afinal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class ContactRepository {

    private static ContactRepository _cr;

    private ArrayList<Contact> c;
    private Activity a;

    private ContactRepository(Activity a){
        this.a = a;
        c = new ArrayList<>();
    }

    public static ContactRepository getInstance(Activity a){
        if(_cr == null){
            _cr = new ContactRepository(a);
        }
        return _cr;
    }

    @SuppressLint("Range")
    private void populateContactsList() {
        //HashSet used to avoid duplicate contacts, using .equals() in Contact class
        TreeSet<Contact> contactHashSet = new TreeSet<Contact>();
        ContentResolver cr = a.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactHashSet.add(new Contact(name,phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        c.addAll(contactHashSet);
    }

    public ArrayList<Contact> getContactsList(){
        populateContactsList();
        return c;
    }

    public ArrayList<Contact> getTestContactData(){
        ArrayList<Contact> list = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            Contact contact = new Contact("Semion" +" " + Integer.toString(i), "0545441000");
            list.add(contact);
        }
        for(int i = 0; i < 8; i++){
            Contact contact = new Contact("BatEl" +" " + Integer.toString(i), "0548054078");
            list.add(contact);
        }
        return list;
    }

}
