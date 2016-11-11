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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
                Snackbar.make(view, "Add action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Eventually will have a modal window here to input task info and create user-specified task
                TaskItem taskItem = new TaskItem(new GregorianCalendar(5, 5, 5), "Example");
                dbHelper.addEvent(taskItem);
                //mAdapter.
            }
        });
    }

}
