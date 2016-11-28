package com.example.des.studentmanagerredux;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.des.studentmanagerredux.db.PMDbHelper;
import com.example.des.studentmanagerredux.db.ToDoDbHelper;
import com.example.des.studentmanagerredux.pwmanager.PWAdapter;
import com.example.des.studentmanagerredux.pwmanager.PWItem;
import com.example.des.studentmanagerredux.todo.ToDoAdapter;
import com.example.des.studentmanagerredux.todo.ToDoListItem;

/**
 * Created by Nikhil on 11/26/2016.
 */

public class PasswordManager extends AppCompatActivity {
    private PMDbHelper dbHelper;
    private ListView mTaskListView;
    private PWAdapter mAdapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new PMDbHelper(this);

        setContentView(R.layout.activity_password_manager);

        mTaskListView = (ListView) this
                .findViewById(R.id.list_usernames);

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                mAdapter = new PWAdapter(PasswordManager.this, dbHelper.getAllEvents(), 0);
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
        LinearLayout layout = new LinearLayout(fab.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText taskEditText = new EditText(fab.getContext());
        taskEditText.setHint("Site or Service Title");
        layout.addView(taskEditText);

        final EditText userNameText = new EditText(fab.getContext());
        userNameText.setHint("User Name");
        layout.addView(userNameText);

        final EditText passwordText = new EditText(fab.getContext());
        passwordText.setHint("Password");
        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordText);

        AlertDialog dialog = new AlertDialog.Builder(fab.getContext())
                .setTitle("Add a new Entry")
                .setView(layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String entryName = String.valueOf(taskEditText.getText());
                        String userName = String.valueOf(userNameText.getText());
                        String password = String.valueOf(passwordText.getText());
                        PWItem newItem = new PWItem(entryName, userName, password);
                        if (dbHelper.hasEvent(dbHelper.getReadableDatabase(), entryName, userName)) {
                            AlertDialog aDialog = new AlertDialog.Builder(taskEditText.getContext())
                                    .setTitle("Error: Entry with Specified Username Already Exists")
                                    .setPositiveButton("Cancel", null)
                                    .create();
                            aDialog.show();
                        }
                        else {
                            dbHelper.addEntry(newItem);
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
        mAdapter = new PWAdapter(this, dbHelper.getAllEvents(), 0);
        mTaskListView.setAdapter(mAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }
}
