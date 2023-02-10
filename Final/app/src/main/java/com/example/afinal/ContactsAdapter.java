package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ContactViewModel _model;
    private List<Contact> _contacts;

    public ContactsAdapter(ContactViewModel cvm){
        _model = cvm;
        _contacts = cvm.getContactsArrayLiveData().getValue();
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.fillViewData(_contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return _contacts.size();
    }

    /* ViewHolder class - sets the data for individual item lines in the list */
    public class ViewHolder extends RecyclerView.ViewHolder{

        private View _parentView;
        private TextView _contactNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _parentView = itemView;
            _contactNameTextView = _parentView.findViewById(R.id.list_contact_tv_contactName);
        }

        public void fillViewData(Contact c){
            _contactNameTextView.setText(c.getName());
        }
    }

}
