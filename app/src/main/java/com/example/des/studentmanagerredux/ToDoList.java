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
 *
 *  The ToDoList shows the user basic tasks that they have to do. Unlike the items on the planner,
 *  these items do not have a time associated with it - they are basic tasks that do not have a
 *  specific deadline. Examples include getting groceries or studying for a test. For each item,
 *  there is a checkbox to track whether or not you are finished, a progress bar to track your
 *  progress, and a delete button to remove the task once you no longer want to see it.
 *
 *  Tasks that are completed are given a different colored background and moved to the bottom of the
 *  list. The user is prevented from creating two different tasks with the same title.
 *
 *  Some parts of this class were written with help from the following tutorial:
 *  https://www.sitepoint.com/starting-android-development-creating-todo-app/ 
 *
 */

public class ToDoList extends AppCompatActivity {
    private ToDoDbHelper dbHelper; // database for ToDoList items
    // Used for display
    private ListView mTaskListView;
    private ToDoAdapter mAdapter;

    /* Called when page is opened, creates display */
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

        // FloatingActionButton used to add items to ToDoList
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(fab);
            }
        });
    }

    /**
     * Adds an item to the ToDoList
     * @param fab - The button which you click to add an item
     */
    private void addItem(FloatingActionButton fab) {
        final EditText taskEditText = new EditText(fab.getContext());
        AlertDialog dialog = new AlertDialog.Builder(fab.getContext())
                .setTitle("Add a new task")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemName = String.valueOf(taskEditText.getText());
                        itemName.replace("'","\'");
                        ToDoListItem taskItem = new ToDoListItem(itemName, 0 , false);
                        if (dbHelper.hasEvent(dbHelper.getReadableDatabase(), itemName)) {
                            AlertDialog aDialog = new AlertDialog.Builder(taskEditText.getContext())
                                    .setTitle("Error: New Task Name Already Exists")
                                    .setPositiveButton("Cancel", null)
                                    .create();
                            aDialog.show();
                        }
                        else {
                            dbHelper.addEvent(taskItem);
                            refresh();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /* Refreshes table whenever there is a change */
    public void refresh() {
        mAdapter = new ToDoAdapter(this, dbHelper.getAllEvents(), 0);
        mTaskListView.setAdapter(mAdapter);
    }

    /* Refreshes the entire page */
    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

}
