package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ContactViewModel _model;
    private List<Contact> _contacts;
    private int _pos;

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
        if(_pos == position){
            holder._parentView.findViewById(R.id.row_layout_main).setBackground(ContextCompat.getDrawable( holder._parentView.getContext(), R.drawable.list_entry_selected));
        }
        else{
            holder._parentView.findViewById(R.id.row_layout_main).setBackground(ContextCompat.getDrawable( holder._parentView.getContext(), R.drawable.list_entry));
        }
    }

    @Override
    public int getItemCount() {
        return _contacts.size();
    }

    public void updatePosition(Integer position) {
        _pos = position;
        this.notifyDataSetChanged();
    }

    public void updateContactsList(ArrayList<Contact> contacts) {
        _contacts = contacts;
        this.notifyDataSetChanged();
    }

    /* ViewHolder class - sets the data for individual item lines in the list */
    public class ViewHolder extends RecyclerView.ViewHolder{

        private View _parentView;
        private TextView _contactNameTextView;
        private TextView _contactPhoneNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _parentView = itemView;
            _contactNameTextView = _parentView.findViewById(R.id.list_contact_tv_contactName);
            _contactPhoneNumberTextView = _parentView.findViewById(R.id.list_contact_tv_contactPhoneNum);
        }

        public void fillViewData(Contact c){
            _contactNameTextView.setText(c.getName());
            _contactPhoneNumberTextView.setText(c.getPhoneNumber());
            _parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _model.setSelected(c);
                    notifyDataSetChanged();
                }
            });
            _parentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    _model.removeContact(c);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

}
