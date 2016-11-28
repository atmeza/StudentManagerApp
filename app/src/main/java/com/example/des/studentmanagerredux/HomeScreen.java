package com.example.des.studentmanagerredux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.db.GPADbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HomeScreen extends AppCompatActivity {

    private DatabaseReference mDataRef;
    private Object firebaseTime = new Object();
    private GPADbHelper GPAHelper = new GPADbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        updateUpcomingEvents();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        updateUpcomingEvents();
    }

    public void sendMessageToDo(View view)
    {
        Intent intent = new Intent(this, ToDoList.class);
        startActivity(intent);
    }

    public void sendMessageCalendar(View view)
    {
        Intent intent = new Intent(this, Calendar_Page.class);
        startActivity(intent);
    }

    public void sendMessageGPACalculator(View view)
    {
        Intent intent = new Intent(this, GPACalculator.class);
        startActivity(intent);
    }

    public void sendMessagePasswordManager(View view)
    {
        Intent intent = new Intent(this, PasswordManager.class);
        startActivity(intent);
    }

    private void updateUpcomingEvents()
    {
        EventDbHelper db = new EventDbHelper(this);
        Cursor res = db.getEventsOnDay(Calendar.getInstance());

        res.moveToFirst();

        DateFormat df = new SimpleDateFormat("H:mm");

        String[] ary = new String[3];

        for(int i = 0; i < 3; i++)
        {
            if(res.getCount() <= i)
            {
                ary[i] = "";
                continue;
            }
            long curTimeMillis = res.getLong(2);

            String title = res.getString(1);
            String date = df.format(new Date(curTimeMillis));

            ary[i] = title + " - " + date;
            res.moveToNext();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ary);
        ListView view = (ListView)findViewById(R.id.recent_events);
        view.setAdapter(adapter);
    }
}
