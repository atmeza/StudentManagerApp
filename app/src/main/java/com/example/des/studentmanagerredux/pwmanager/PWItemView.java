package com.example.des.studentmanagerredux.pwmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.studentmanagerredux.PasswordManager;
import com.example.des.studentmanagerredux.R;
import com.example.des.studentmanagerredux.db.PMDbHelper;
import com.example.des.studentmanagerredux.db.ToDoDbHelper;

import java.nio.channels.AlreadyBoundException;

/**
 * Created by Nikhil on 11/26/2016.
 *
 * The component that holds PWItem, displayed on PasswordManager
 */

public class PWItemView extends LinearLayout {

    private TextView mTitle; // Display title and change properties of PWItem
    private Button mDeleteButton; // Used to delete item from list

    private PWItem item; // each item has its own view
    private PasswordManager pwManager; // the list that items are displayed on

    /* Constructors */
    public PWItemView(Context context, PasswordManager list)
    {
        super(context);
        initializeViews(context);
        this.pwManager = list;
    }

    public PWItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context);
    }

    public PWItemView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setOrientation(LinearLayout.VERTICAL);
        inflater.inflate(R.layout.pm_item_view, this);

        mTitle = (TextView) this
                .findViewById(R.id.pm_item_title_view);

        mTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(v.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView userNameText = new TextView(v.getContext());
                userNameText.setText(item.getUserName());
                userNameText.setTextSize(20);
                layout.addView(userNameText);

                final TextView passwordText = new TextView(v.getContext());
                passwordText.setText(item.getPassword());
                passwordText.setTextSize(20);
                layout.addView(passwordText);

                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("UserName and Password")
                        .setView(layout)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changeUserNameAndPassword();
                            }
                        })
                        .setNegativeButton("Close", null)
                        .create();
                dialog.show();
            }
        });

        mDeleteButton = (Button) this
                .findViewById(R.id.pm_item_view_delete);

        // Deletes task by hitting delete button
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null)
                {
                    PMDbHelper dbHelper = new PMDbHelper(view.getContext());
                    dbHelper.removeEntry(item);
                    pwManager.refresh();
                    dbHelper.close();
                }
            }
        });
    }

    public void updateView() {
        mTitle.setText(item.getTitle());
    }

    /* Dialog to change UserName and Password after entry is already created */
    public void changeUserNameAndPassword() {
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Contains username info, can be edited
        final EditText userNameText = new EditText(this.getContext());
        userNameText.setText(item.getUserName());
        layout.addView(userNameText);

        // Contains password info, can be edited
        final EditText passwordText = new EditText(this.getContext());
        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordText.setText(item.getPassword());
        layout.addView(passwordText);

        // database to make changes
        final PMDbHelper dbHelper = new PMDbHelper(this.getContext());

        // Display dialog
        AlertDialog dialog = new AlertDialog.Builder(this.getContext())
                .setTitle("New UserName and Password")
                .setView(layout)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUserName = String.valueOf(userNameText.getText());
                        String newPassword = String.valueOf(passwordText.getText());
                        if (!dbHelper.changeUserNameAndPassword(item, newUserName, newPassword)) {
                            showErrorDialog();
                            dbHelper.close();
                        }
                        else {
                            item.setUserName(newUserName);
                            item.setPassword(newPassword);
                            pwManager.refresh();
                            dbHelper.close();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    public void setTask(PWItem item)
    {
        this.item = item;
        updateView();
    }

    public PWItem getItem() { return item; }

    /* Error dialog in case of invalid input */
    public void showErrorDialog () {
        View v = this.mTitle;
        AlertDialog dialog = new AlertDialog.Builder(v.getContext()) // Dialog for new name
                .setTitle("Error: New UserName for this Service Already in Use")
                .setPositiveButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

}
