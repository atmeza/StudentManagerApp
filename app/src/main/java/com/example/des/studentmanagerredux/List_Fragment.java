package com.example.des.studentmanagerredux;

//import android.support.v4.app.Fragment;

import android.icu.util.GregorianCalendar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.db.ToDoDbHelper;
import com.example.des.studentmanagerredux.task.TaskAdapter;

import java.util.Calendar;

/**
 * Created by alexm on 11/6/2016.
 */

public class List_Fragment extends ListFragment {

    private List_Fragment self = this;
    private EventDbHelper helper;
    TaskAdapter adapter;

    @Override
    public ViewGroup onCreateView(LayoutInflater inflator, final ViewGroup containter,
                                  Bundle savedInstanceState){
        final ViewGroup rview = (ViewGroup)inflator.inflate(R.layout.calendar_fragment1, containter, false);

        helper = new EventDbHelper(this.getContext());

        ;
        adapter = new TaskAdapter(this.getContext(), helper.getEventsOnDay(((Calendar_Page) getActivity()).getCurrentDate()),0);

        setListAdapter(adapter);
        setRetainInstance(true);

        final View addEventButton = rview.findViewById(R.id.add_event);
        addEventButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateEventPage create_event_page = (CreateEventPage)getFragmentManager().findFragmentByTag("CreateEventPage");

                        if(create_event_page==null) {
                            create_event_page  = new CreateEventPage();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("helper",helper);
                            bundle.putSerializable("adapter", adapter);

                            create_event_page.setArguments(bundle);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.add(android.R.id.content, create_event_page, "CreateEventPage");
                            transaction.commit();
                        }
                    }
                }
        );
        final View closePage = rview.findViewById(R.id.closeEventList);
        closePage.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.remove(self);
                        ft.commit();
                    }
                }
        );
        return rview;
    }

    public void onListItemClick(ListView l, ViewGroup view, int position, long id){
        TextView text = (TextView)view.findViewById(R.id.txtitem);
        Toast.makeText(getActivity(),text.getText().toString(),Toast.LENGTH_LONG).show();





    }


}
