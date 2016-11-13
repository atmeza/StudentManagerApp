package com.example.des.studentmanagerredux.task;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.des.studentmanagerredux.R;

/**
 * Created by alexm on 11/7/2016.
 */

public class create_event_fragment_calendar extends Fragment {


    public ViewGroup onCreateView(LayoutInflater inflator, ViewGroup containter,
                                  Bundle savedInstanceState){
        ViewGroup view = (ViewGroup)inflator.inflate(R.layout.create_event_page_fragment, containter, false);



        return view;
    }
}
