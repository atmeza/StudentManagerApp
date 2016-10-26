package com.example.des.studentmanagerredux;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
TODO: Add functions for manipulating the constituent controls at runtime
*/

/**
 * Created by Matt on 10/25/16.
 *
 * Compound control class for displaying tasks in to-do list
 */

public class TaskItemView extends LinearLayout {

    private TextView mTitle;
    private Button mDeleteButton;
    private ProgressBar mProgress;

    public TaskItemView(Context context) {
        super(context);
        initializeViews(context);
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
    }

    public TaskItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
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
    }

    public TaskItemView(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
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