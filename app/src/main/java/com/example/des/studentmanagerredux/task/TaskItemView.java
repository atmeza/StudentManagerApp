package com.example.des.studentmanagerredux.task;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.des.studentmanagerredux.R;
import com.example.des.studentmanagerredux.db.EventDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private TextView mTime;
    private Button mDeleteButton;

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
     * Inflates the views in the layout and hooks up event handlers.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setOrientation(LinearLayout.VERTICAL);
        inflater.inflate(R.layout.task_item_view, this);

        mTitle = (TextView) this
                .findViewById(R.id.task_item_view_title);

        mTime = (TextView) this
                .findViewById(R.id.task_item_view_time);

        mDeleteButton = (Button) this
                .findViewById(R.id.task_item_view_delete);

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

        mTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog open code here
            }
        });
    }

    public void updateTaskView()
    {
        mTitle.setText(task.getTitle());
        DateFormat df = new SimpleDateFormat("HH:mm");
        mTime.setText(df.format(task.getStart().getTime()) + " - " + df.format(task.getEnd().getTime()));
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
}