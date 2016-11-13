package com.example.des.studentmanagerredux;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.des.studentmanagerredux.db.ToDoDbHelper;
import com.example.des.studentmanagerredux.todo.ToDoAdapter;
import com.example.des.studentmanagerredux.todo.ToDoListItem;

/**
 *  Written by Matt and Nikhil
 */

public class ToDoList extends AppCompatActivity {
    private ToDoDbHelper dbHelper;
    private ListView mTaskListView;
    private ToDoAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ToDoDbHelper(this);

        setContentView(R.layout.activity_to_do_list);

        mTaskListView = (ListView) this
                .findViewById(R.id.list_todo);

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                mAdapter = new ToDoAdapter(ToDoList.this, dbHelper.getAllEvents(), 0);
                mTaskListView.setAdapter(mAdapter);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(fab);
            }
        });
    }

    private void addItem(FloatingActionButton fab) {
        final EditText taskEditText = new EditText(fab.getContext());
        AlertDialog dialog = new AlertDialog.Builder(fab.getContext())
                .setTitle("Add a new task")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemName = String.valueOf(taskEditText.getText());
                        ToDoListItem taskItem = new ToDoListItem(itemName, 0 , false);
                        dbHelper.addEvent(taskItem);
                        refresh();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void refresh() {
        onRestart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

}
