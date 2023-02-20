package com.example.afinal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class RecycleFragment extends Fragment {
    private ContactViewModel _model;
    private RecycleListener _listener;

    public RecycleFragment() {
        // Required empty public constructor
    }

    public static RecycleFragment newInstance() {
        RecycleFragment fragment = new RecycleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this._listener = (RecycleListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvContacts = (RecyclerView)view.findViewById(R.id.rvContacts);
        _model = ((ContactViewModel.ShareContactModel)(view.getContext())).shareContactModel();
        ContactsAdapter adapter = new ContactsAdapter(_model, _listener);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        _model.getSelectedPositionLiveData().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
               adapter.updatePosition(integer);
            }
        });

        _model.getContactsArrayLiveData().observe(getActivity(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                adapter.updateContactsList(contacts);
            }
        });
    }

    interface RecycleListener{
        void onClickEvent();
    }

}