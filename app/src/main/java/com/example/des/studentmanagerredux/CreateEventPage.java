package com.example.des.studentmanagerredux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.example.des.studentmanagerredux.task.TaskItem;


public class CreateEventPage extends Fragment {

    private CreateEventPage self = this;

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
                        final EditText hour = (EditText)view.findViewById(R.id.eventTimeTextInputBox);
                        final EditText info = (EditText)view.findViewById(R.id.editEventInfoInputBox);
                        int hourI = Integer.parseInt(hour.getText().toString());
                        GregorianCalendar thisDate = ((Calendar_Page)getActivity()).getCurrentDate();
                        thisDate.set(Calendar.HOUR,hourI);
                        String titleS = title.getText().toString();
                        String infoS = info.getText().toString();
                        TaskItem newTask = new TaskItem(thisDate,thisDate,0,false,titleS);
                    }
                }
        );
        return view;
    }



}
