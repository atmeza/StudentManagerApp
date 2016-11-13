//package com.example.des.studentmanagerredux.task;
package com.example.des.studentmanagerredux.todo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.des.studentmanagerredux.db.ToDoDbHelper;
import com.example.des.studentmanagerredux.R;

/*
TODO: Document functionality
*/

/**
 * Created by Matt on 10/25/16.
 *
 * Compound control class for displaying tasks in to-do list
 */

public class ToDoListItemView extends LinearLayout {

    private TextView mTitle;
    private Button mDeleteButton;
    private SeekBar mProgress;

    private ToDoListItem item;

    public ToDoListItemView(Context context)
    {
        super(context);
        initializeViews(context);
    }

    public ToDoListItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context);
    }

    public ToDoListItemView(Context context, AttributeSet attrs, int defStyle)
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
        inflater.inflate(R.layout.task_item_view, this);

        mProgress = (SeekBar) this
                .findViewById(R.id.task_item_view_progress);

        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ToDoDbHelper dbHelper = new ToDoDbHelper(seekBar.getContext());
                dbHelper.updateProgress(item, progress);
                // not sure if the next two lines are neccessary
                //item.setProgress(progress);
                //mProgress.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mTitle = (CheckedTextView) this
                .findViewById(R.id.task_item_view_title);

        // Changes name by selecting on task text
        mTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null)
                {
                    final EditText taskEditText = new EditText(view.getContext());
                    final ToDoDbHelper dbHelper = new ToDoDbHelper(view.getContext());
                    AlertDialog dialog = new AlertDialog.Builder(view.getContext()) // Dialog for new name
                            .setTitle("Edit Task Name")
                            .setView(taskEditText)
                            .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String newTask = String.valueOf(taskEditText.getText());
                                    if (!dbHelper.changeEventName(item, newTask)) {
                                        showErrorDialog();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                }
            }
        });

        mDeleteButton = (Button) this
                .findViewById(R.id.task_item_view_delete);

        // Deletes task by hitting delete button
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null)
                {
                    ToDoDbHelper dbHelper = new ToDoDbHelper(view.getContext());
                    dbHelper.removeEvent(item);
                }
            }
        });
    }

    public void updateTaskView()
    {
        mTitle.setText(item.getTitle());
        mProgress.setMax(ToDoListItem.PROGRESS_MAX);
        mProgress.setProgress(item.getProgress());
    }

    public void setTask(ToDoListItem item)
    {
        this.item = item;
        updateTaskView();
    }

    public ToDoListItem getTask()
    {
        return item;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void showErrorDialog () {
        // TODO
        View v = this.mTitle;
        AlertDialog dialog = new AlertDialog.Builder(v.getContext()) // Dialog for new name
                .setTitle("Error: New Task Name Already Exists")
                .setPositiveButton("Cancel", null)
                .create();
        dialog.show();
    }

    /**
     * Use the below code to add instances of this compound control to a Linear Layout at runtime
     * Can be modified to work with other layout types
     *

     TaskItemView newTask = new TaskItemView(context);
     LinearLayout.LayoutParams params = new
     LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
     ViewGroup.LayoutParams.WRAP_CONTENT);
     ((LinearLayout)(findViewById(R.id.content_main))).addView(newTask, params);

     */
}