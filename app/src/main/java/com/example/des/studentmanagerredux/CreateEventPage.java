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

public class CreateEventPage extends Fragment {

    private CreateEventPage self = this;
    private EventDbHelper helper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup)inflater.inflate(R.layout.create_event_page_fragment, container, false);

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

        final View createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText title = (EditText)view.findViewById(R.id.eventNameInputBox);
                        final TimePicker time = (TimePicker)view.findViewById(R.id.eventTimePicker);
                        final EditText info = (EditText)view.findViewById(R.id.editEventInfoInputBox);
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
                        GregorianCalendar thisDate = ((Calendar_Page)getActivity()).getCurrentDate();
                        thisDate.set(Calendar.HOUR,hourI);
                        thisDate.set(Calendar.MINUTE,minuteI);
                        String titleS = title.getText().toString();
                        String infoS = info.getText().toString();
                        TaskItem newTask = new TaskItem(thisDate,thisDate,0,false,titleS);
                        helper = (EventDbHelper)getArguments().getSerializable("helper");
                        helper.addEvent(newTask);
                        TaskAdapter adapter = (TaskAdapter)getArguments().get("adapter");
                        adapter.notifyDataSetChanged();

                    }
                }
        );
        return view;
    }



}
