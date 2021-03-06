package com.example.des.studentmanagerredux.task;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.des.studentmanagerredux.R;
import com.example.des.studentmanagerredux.db.EventDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
TODO: Document functionality
*/

/**
 * Created by Matt on 10/25/16.
 *
 * Compound control class for displaying tasks in to-do list
 */

public class TaskItemView extends LinearLayout {

    private TextView mTitle;
    private Button mDeleteButton;
    private SeekBar mProgress;

    private TaskItem task;

    public TaskItemView(Context context)
    {
        super(context);
        initializeViews(context);
    }

    public TaskItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context);
    }

    public TaskItemView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout and sets up instance variables
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setOrientation(LinearLayout.VERTICAL);
        inflater.inflate(R.layout.task_item_view, this);

        mProgress = (SeekBar) this
                .findViewById(R.id.task_item_view_progress);

        mTitle = (TextView) this
                .findViewById(R.id.task_item_view_title);

        mDeleteButton = (Button) this
                .findViewById(R.id.task_item_view_delete);

        //Delete button event listener - removes task from database
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view != null)
                {
                    EventDbHelper dbHelper = new EventDbHelper(view.getContext());
                    dbHelper.removeEvent(task);
                }
            }
        });
    }

    /**
     * Refreshes the view with data from the task
     */

    public void updateTaskView()
    {
        DateFormat df = new SimpleDateFormat("H:mm");
        String date = task.getTitle() + ": " + df.format(task.getStart().getTime());
        mTitle.setText(date);
        mProgress.setMax(TaskItem.PROGRESS_MAX);
        mProgress.setProgress(task.getProgress());
    }

    /**
     * Sets the TaskItem associated with this View to the given TaskItem, and refreshes the display
     * @param task - TaskItem to bind View to
     */

    public void setTask(TaskItem task)
    {
        this.task = task;
        updateTaskView();
    }

    /**
     *
     * @return TaskItem associated with this View
     */

    public TaskItem getTask()
    {
        return task;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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