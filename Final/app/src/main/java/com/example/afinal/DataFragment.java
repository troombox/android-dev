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
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DataFragment extends Fragment {
    //data
    private FactDispenser _fd;
    private ContactViewModel _model;
    private Contact _currentContact;

    private ContactHistoryViewModel _historyModel;
    private ContactHistory _currentContactHistory;
    private String _contactPreference;

    private View _view;
    private TextView _ui_messages;
    private TextView _ui_contactName;
    private TextView _ui_contactPreference;

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
        _view = view;
        _fd = FactDispenser.getInstance(view.getContext());
        _model = ((ContactViewModel.ShareContactModel)(view.getContext())).shareContactModel();
        _currentContact = _model.getContactByPosition(_model.getSelectedPositionLiveData().getValue());
        _contactPreference = _model.getContactPreference(_currentContact);
        _historyModel = ((ContactHistoryViewModel.ShareHistoryModel)(view.getContext())).shareHistoryModel();
        _currentContactHistory = _historyModel.getContactHistoryByContactName(_currentContact.getName());
        if(_currentContactHistory == null){
            _currentContactHistory = new ContactHistory(_currentContact);
        }

        _ui_contactName = ((TextView)view.findViewById(R.id.f_data_tv_contactName));
        _ui_contactPreference = ((TextView)view.findViewById(R.id.f_data_tv_contactPreference));
        _ui_messages = ((TextView)view.findViewById(R.id.f_data_linear_tv_messageData));

        _ui_contactName.setText(_currentContact.getName());
        _ui_messages.setText(_currentContactHistory.printFormattedMessages());
        _ui_contactPreference.setText(_contactPreference);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to send SMS message?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            SmsSender s = new SmsSender();
                            Fact f;
                            String factText;
                            _contactPreference = _model.getContactPreference(_currentContact);
                            if(_contactPreference.equals("")){ //first message (didn't choose preference yet)
                                s.sendPreferencesRequestSms(_currentContact.getPhoneNumber());
                                Toast.makeText(view.getContext(),"SMS sent", Toast.LENGTH_LONG).show();
                                return;
                            }else if(_contactPreference.equals("DOG")){
                                f = _fd.getRandomFact(Fact.FACT_TYPE_DOG);
                                factText = f.getFactText();
                            } else if(_contactPreference.equals("CAT")){
                                f = _fd.getRandomFact(Fact.FACT_TYPE_CAT);
                                factText = f.getFactText();
                            }else
                                return;
                            s.sendSms(_currentContact.getPhoneNumber(),factText);
                            _currentContactHistory.getMessagesArray().add(factText);
                            _currentContactHistory.getFactIDsArray().add(f.getFactID());
                            Toast.makeText(view.getContext(),"SMS sent", Toast.LENGTH_LONG).show();
                            _historyModel.saveContactHistory(_currentContactHistory);
                            _historyModel.saveContactHistories();
                            if(_model.checkFlagAutoSend()){ //work manager
                                if(_contactPreference.equals("DOG")){
                                    f = _fd.getRandomFact(Fact.FACT_TYPE_DOG);
                                } else{
                                    f = _fd.getRandomFact(Fact.FACT_TYPE_CAT);
                                }
                                enqueue(_currentContact.getPhoneNumber(),f.getFactText());
                            }

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

    private void enqueue(String phone, String fact){
        Data inputData = new Data.Builder()
                .putString("phone",phone).putString("fact",fact)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(20, TimeUnit.SECONDS)
                .setInputData(inputData)
                .build();
//        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 30, TimeUnit.SECONDS)
//                .setInitialDelay(30, TimeUnit.SECONDS)
//                .setInputData(inputData)
//                .build();
        WorkManager.getInstance(_view.getContext()).enqueue(workRequest);//Enqueue the work request using WorkManager
    }
}