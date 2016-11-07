package com.example.des.studentmanagerredux;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.task.TaskAdapter;
import com.example.des.studentmanagerredux.task.TaskItem;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ToDoList extends AppCompatActivity {
    private EventDbHelper dbHelper;
    private ListView mTaskListView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new EventDbHelper(this);

        setContentView(R.layout.activity_to_do_list);

        mTaskListView = (ListView) this
                .findViewById(R.id.list_todo);

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                mAdapter = new TaskAdapter(ToDoList.this, dbHelper.getAllEvents(), 0);
                mTaskListView.setAdapter(mAdapter);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Eventually will have a modal window here to input task info and create user-specified task
                //This is where Nikhil can put his dialogalert code
                TaskItem taskItem = new TaskItem(new GregorianCalendar(), "Example");
                taskItem.getStart().setTimeInMillis(500);
                taskItem.getEnd().setTimeInMillis(500);
                dbHelper.addEvent(taskItem);
                mAdapter = new TaskAdapter(ToDoList.this, dbHelper.getAllEvents(),0);
                mTaskListView.setAdapter(mAdapter);
            }
        });
    }

}
