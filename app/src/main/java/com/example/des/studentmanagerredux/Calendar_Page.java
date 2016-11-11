package com.example.des.studentmanagerredux;

//Created by Tyger 10-30-16

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;




import java.util.ArrayList;

public class Calendar_Page extends AppCompatActivity {

    final Context context = this;
    private Button button;

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


                List_Fragment list_fragment = (List_Fragment)getSupportFragmentManager().findFragmentByTag("List_Fragment");

                if(list_fragment==null){
                    list_fragment = new List_Fragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(android.R.id.content, list_fragment, "List_Fragment");
                    transaction.commit();
                }




            }
        });
    }




}
