package com.example.des.studentmanagerredux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.task.TaskAdapter;
import com.example.des.studentmanagerredux.task.TaskItem;

import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

/* Created by Tyger Yang 10-30-16 */

/* This class serves as the fragment for event creation in the calendar */
public class CreateEventPage extends Fragment {

    private CreateEventPage self = this;
    private EventDbHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup)inflater.inflate(R.layout.create_event_page_fragment, container, false);

        // Sets an OnCLickListener for the cancel button that will close the current fragment
        final View cancelButton = view.findViewById(R.id.cancelCreateEvent);
        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.remove(self);
                        ft.commit();
                    }
                }
        );

        // Sets an OnClickListener for createEventButton that, provided the information is inserted appropriately, will
        // Create a new task and insert it into the database
        final View createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // When the button clicks this reads from the EditText and Timepicker views
                        final EditText title = (EditText)view.findViewById(R.id.eventNameInputBox);
                        final TimePicker time = (TimePicker)view.findViewById(R.id.eventTimePicker);
                        final EditText info = (EditText)view.findViewById(R.id.editEventInfoInputBox);
                        // Will only work if there is an event title
                        if (title.getText().length() == 0) {
                            Toast.makeText(getActivity(),"Please give the event a title", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //If API is older than Android 23 use defaults
                        int hourI = 12;
                        int minuteI = 0;
                        if (Build.VERSION.SDK_INT >= 23) {
                            hourI = time.getHour();
                            minuteI = time.getMinute();
                        }
                        // Retrieves the Gregorian Calendar
                        GregorianCalendar thisDate = ((Calendar_Page)getActivity()).getCurrentDate();
                        thisDate.set(Calendar.HOUR,hourI);
                        thisDate.set(Calendar.MINUTE,minuteI);
                        String titleS = title.getText().toString();
                        String infoS = info.getText().toString();
                        // Creates the task item
                        TaskItem newTask = new TaskItem(thisDate,thisDate,0,false,titleS);
                        // Insert into the database
                        helper = (EventDbHelper)getArguments().getSerializable("helper");
                        helper.addEvent(newTask);
                        TaskAdapter adapter = (TaskAdapter)getArguments().get("adapter");
                        adapter.notifyDataSetChanged();
                        //Close the page following the event creation
                        FragmentManager fm = getFragmentManager();
                        List_Fragment lfrag = (List_Fragment) fm.findFragmentByTag("List_Fragment");
                        lfrag.refreshAdapter();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.remove(self);
                        ft.commit();
                    }
                }
        );
        return view;
    }



}
