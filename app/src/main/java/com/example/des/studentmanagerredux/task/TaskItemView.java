package com.example.des.studentmanagerredux.task;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.des.studentmanagerredux.R;

/*
TODO: Document functionality
*/

/**
 * Created by Matt on 10/25/16.
 *
 * Compound control class for displaying tasks in to-do list
 */

public class TaskItemView extends LinearLayout {

    private CheckedTextView mTitle;
    private Button mDeleteButton;
    private SeekBar mProgress;

    private TaskItem task;

    public TaskItemView(Context context)
    {
        super(context);
    }

    public TaskItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public TaskItemView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public TaskItemView(Context context, TaskItem task)
    {
        this(context);
        this.task = task;
        initializeViews(context);
    }

    public TaskItemView(Context context, AttributeSet attrs, TaskItem task)
    {
        this(context, attrs);
        this.task = task;
        initializeViews(context);
    }

    public TaskItemView(Context context, AttributeSet attrs, int defStyle, TaskItem task)
    {
        this(context, attrs, defStyle);
        this.task = task;
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
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

        mTitle = (CheckedTextView) this
                .findViewById(R.id.task_item_view_title);

        mDeleteButton = (Button) this
                .findViewById(R.id.task_item_view_delete);
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view != null)
                {
                    ((ViewGroup)view.getParent().getParent().getParent()).removeView((View)(view.getParent()).getParent());
                }
            }
        });

        updateTaskView();
    }

    public void updateTaskView()
    {
        mTitle.setText(task.getTitle());
        mProgress.setMax(TaskItem.PROGRESS_MAX);
        mProgress.setProgress(task.getProgress());
    }

    public void setTask(TaskItem task)
    {
        this.task = task;
        updateTaskView();
    }

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