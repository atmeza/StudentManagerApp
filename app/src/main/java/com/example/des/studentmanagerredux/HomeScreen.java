package com.example.des.studentmanagerredux;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

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
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }
}