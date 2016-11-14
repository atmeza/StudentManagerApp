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
                        final EditText date = (EditText)view.findViewById(R.id.eventDateTextInputBox);
                        final EditText info = (EditText)view.findViewById(R.id.editEventInfoInputBox);
                        String titleS = title.getText().toString();
                        String dateS = date.getText().toString();
                        char[] dateC = dateS.toCharArray();
                        char[] dayC = new char[2];
                        dayC[0] = dateC[0];
                        dayC[1] = dateC[1];
                        int day;
                        int month;
                        int year;
                        String infoS = info.getText().toString();

                    }
                }
        );
        return view;
    }



}
