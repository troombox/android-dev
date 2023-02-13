package com.example.afinal;

import static com.example.afinal.MainActivity.REQUEST_PERMISSIONS_REQUEST_SEND_SMS;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataFragment extends Fragment {
    //data
    private FactDispenser _fd;
    private ContactViewModel _model;
    private Contact _currentContact;

    private ContactHistoryViewModel _historyModel;
    private ContactHistory _currentContactHistory;

    private TextView _ui_messages;
    private TextView _ui_contactName;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _fd = FactDispenser.getInstance(view.getContext());
        _model = ((ContactViewModel.ShareContactModel)(view.getContext())).shareContactModel();
        _currentContact = _model.getContactByPosition(_model.getSelectedPositionLiveData().getValue());

        _historyModel = ((ContactHistoryViewModel.ShareHistoryModel)(view.getContext())).shareHistoryModel();
        _currentContactHistory = _historyModel.getContactHistoryByContactName(_currentContact.getName());
        if(_currentContactHistory == null){
            _currentContactHistory = new ContactHistory(_currentContact);
        }

        _ui_contactName = ((TextView)view.findViewById(R.id.f_data_tv_contactName));
        _ui_messages = ((TextView)view.findViewById(R.id.f_data_linear_tv_messageData));

        _ui_contactName.setText(_currentContact.getName());
        _ui_messages.setText(_currentContactHistory.printFormattedMessages());
        _historyModel.getContactHistoriesArrayLiveData().observe(getActivity(), new Observer<ArrayList<ContactHistory>>() {
            @Override
            public void onChanged(ArrayList<ContactHistory> contacts) {
                _ui_messages = ((TextView)view.findViewById(R.id.f_data_linear_tv_messageData));
                _ui_messages.setText(_currentContactHistory.printFormattedMessages());
            }
        });

        view.findViewById(R.id.f_data_btn_sendSms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()); // Replace "context" with the actual context of your app
                builder.setMessage("Are you sure you want to send SMS message?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            SmsSender s = new SmsSender();
//                             TODO: Firstm send the preference sms. Then decide which fact to send, by the received sms.
//                             if (...) {
                            Fact f = _fd.getRandomFact(Fact.FACT_TYPE_CAT);
                            String factText = f.getFactText();
                            s.sendSms(null,factText);
                            _currentContactHistory.getMessagesArray().add(factText);
                            _currentContactHistory.getFactIDsArray().add(f.getFactID());
////                          s.sendSms("0545477901", _fd.getRandomFact(Fact.FACT_TYPE_CAT).getFactText());
//                            // }
//                            // else {
//                            s.sendSms(null,_fd.getRandomFact(Fact.FACT_TYPE_DOG).getFactText());
//                            // }
                            Toast.makeText(view.getContext(),"SMS sent", Toast.LENGTH_LONG).show();
                            _historyModel.saveContactHistory(_currentContactHistory);
                        } else {
                            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.SEND_SMS},REQUEST_PERMISSIONS_REQUEST_SEND_SMS);
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}