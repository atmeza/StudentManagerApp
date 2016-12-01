package com.example.des.studentmanagerredux;

//Created by Tyger Yang 10-30-16

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import java.util.GregorianCalendar;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


import com.example.des.studentmanagerredux.task.TaskAdapter;

import java.util.ArrayList;

/* This is the main class for the Calendar Activity */
public class Calendar_Page extends AppCompatActivity {

    final Context context = this;
    private Button button;
    // A calendar variable to ease the transfer of calender between fragments
    public GregorianCalendar currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);




        // The below function allows us to listen to when a new date has been clicked
        // on the calendar view
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {

                currentDate = new GregorianCalendar(year,month,dayOfMonth);
                List_Fragment list_fragment = (List_Fragment)getSupportFragmentManager().findFragmentByTag("List_Fragment");

                // If there is not list_fragment instantiated create a new one
                if(list_fragment==null){
                    list_fragment = new List_Fragment();

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.add(android.R.id.content, list_fragment, "List_Fragment");
                    transaction.commit();
                }




            }
        });
    }

    // Getter method for the currentDate variable
    public GregorianCalendar getCurrentDate() {
        return currentDate;
    }


}
