package com.example.afinal;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactHistory implements Serializable {

    private Contact _c;
    private ArrayList<Integer> factIDs;
    private ArrayList<String> messages;

    public ContactHistory(Contact contact, ArrayList<Integer> factIDs, ArrayList<String> messages) {
        this._c = contact;
        this.factIDs = factIDs;
        this.messages = messages;
    }

    public ContactHistory(Contact contact) {
        this._c = contact;
        this.factIDs = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public ContactHistory() {
        this._c = null;
        this.factIDs = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Contact getContact() {
        return _c;
    }

    public void setContact(Contact contact) {
        this._c = contact;
    }

    public ArrayList<Integer> getFactIDsArray() {
        return factIDs;
    }

    public void setFactIDsArray(ArrayList<Integer> factIDs) {
        this.factIDs = factIDs;
    }

    public ArrayList<String> getMessagesArray() {
        return messages;
    }

    public void setMessagesArray(ArrayList<String> messages) {
        this.messages = messages;
    }

    public String printFormattedMessages(){
        StringBuilder st = new StringBuilder();
        for(String s : messages){
            st.append(s);
            st.append("\n\n");
        }
        return st.toString();
    }

}
