package com.example.afinal;

import static com.example.afinal.MainActivity.REQUEST_PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.example.afinal.MainActivity.REQUEST_PERMISSIONS_REQUEST_SEND_SMS;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DataFragment extends Fragment {

    FactDispenser _fd;

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

        view.findViewById(R.id.f_data_btn_sendSms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    SmsSender s = new SmsSender();
//                    s.sendSms(null,null);
                    s.sendSms("0545477901", _fd.getRandomFact(Fact.FACT_TYPE_CAT).getFactText());
                    Toast.makeText(view.getContext(),"SMS sent", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.SEND_SMS},REQUEST_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
        });
    }
}