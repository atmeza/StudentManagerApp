package com.example.des.studentmanagerredux;

//import android.support.v4.app.Fragment;

import java.util.GregorianCalendar;
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

/* This class serves as the fragment that contains a specific date's list of events */
public class List_Fragment extends ListFragment {

    private List_Fragment self = this;
    private EventDbHelper helper;
    TaskAdapter adapter;

    @Override
    public ViewGroup onCreateView(LayoutInflater inflator, final ViewGroup container,
                                  Bundle savedInstanceState){
        final ViewGroup rview = (ViewGroup)inflator.inflate(R.layout.calendar_fragment1, container, false);

        // Instantiates the database helper and the TaskAdapter
        helper = new EventDbHelper(this.getContext());
        adapter = new TaskAdapter(this.getContext(), helper.getEventsOnDay(((Calendar_Page) getActivity()).getCurrentDate()),0);

        setListAdapter(adapter);
        setRetainInstance(true);

        TextView dateTitle = (TextView)(rview.findViewById(R.id.date_title));
        // A copy of the calendar to assist with creation of the dateTitle
        GregorianCalendar thisDate = ((Calendar_Page)getActivity()).getCurrentDate();
        int day = thisDate.get(Calendar.DAY_OF_MONTH);
        int month = thisDate.get(Calendar.MONTH) + 1; // The +1 accounts for the fact that the calendar uses months from 0-11
        int year = thisDate.get(Calendar.YEAR);
        String date = Integer.toString(month) + "-" + Integer.toString(day) + "-" + Integer.toString(year);
        // Sets a relative title for the given date
        dateTitle.setText(date);

        // Sets an OnClickListener for the add_event button that will open up the CreateEventPage fragment
        final View addEventButton = rview.findViewById(R.id.add_event);
        addEventButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateEventPage create_event_page = (CreateEventPage)getFragmentManager().findFragmentByTag("CreateEventPage");

                        // If there is no create_event_page instantiate a new one
                        if(create_event_page==null) {
                            create_event_page  = new CreateEventPage();
                            // Bundle to transfer the helper and adapter to the create_event_page
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

        // Sets an OnClickListener for the closeEventList button that will close this instance of the view
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

    // Currently not in view
    public void onListItemClick(ListView l, ViewGroup view, int position, long id) {
        TextView text = (TextView) view.findViewById(R.id.txtitem);
        Toast.makeText(getActivity(), text.getText().toString(), Toast.LENGTH_LONG).show();
    }

    // Method to refresh the list view for this fragment
    public void refreshAdapter() {
        adapter = new TaskAdapter(this.getContext(), helper.getEventsOnDay(((Calendar_Page) getActivity()).getCurrentDate()),0);;
        setListAdapter(adapter);
    }



}

