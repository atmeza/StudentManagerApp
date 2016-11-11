package com.example.des.studentmanagerredux;

import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.task.TaskAdapter;

/**
 * Created by alexm on 11/6/2016.
 */

public class List_Fragment extends ListFragment {

    ViewGroup view;
    TaskAdapter taskAdapter;
    private EventDbHelper dbHelper;
    Calendar cal;

    @Override
    public ViewGroup onCreateView(LayoutInflater inflator, ViewGroup containter,
                             Bundle savedInstanceState){

        dbHelper = new EventDbHelper(getContext());
        cal = new GregorianCalendar()
        view = (ViewGroup)inflator.inflate(R.layout.calendar_fragment1, containter, false);


        taskAdapter = new TaskAdapter(getActivity(), dbHelper.getEventsOnDay(cal),R.id.txtitem, 0);

        setListAdapter(adapter);
        setRetainInstance(true);
        Button newEventButton = (Button)view.findViewById(R.id.add_event);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment create_event_page =  getChildFragmentManager().findFragmentByTag("create_event_fragment_calendar");

                if (create_event_page == null) {
                    /*ERROR HERE:
                    create_event_page = new Fragment();

                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(android.R.id.content, create_event_page, "create_event_calendar_fragment");
                    transaction.commit();
                    */
                }
            }
        });
        return view;


    }

    public void onListItemClick(ListView l, ViewGroup view, int position, long id){
        TextView text = (TextView)view.findViewById(R.id.txtitem);
        Toast.makeText(getActivity(),text.getText().toString(),Toast.LENGTH_LONG).show();





    }



}
