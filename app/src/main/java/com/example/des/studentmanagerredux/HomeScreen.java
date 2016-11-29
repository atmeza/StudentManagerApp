package com.example.des.studentmanagerredux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.des.samplehomescreen.Login;
import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.db.GPADbHelper;
import com.example.des.studentmanagerredux.db.ToDoDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.GregorianCalendar;

public class HomeScreen extends AppCompatActivity {

    private DatabaseReference mDataRef;
    private Object firebaseTime = new Object();
    private GPADbHelper GPAHelper = new GPADbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
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
    public void sendMessageLogout(View view){
        GPADbHelper.logout();
        EventDbHelper.logout();
        ToDoDbHelper.logout();

        Intent  intent= new Intent(this, Login.class);
        startActivity(intent);
    }
}
