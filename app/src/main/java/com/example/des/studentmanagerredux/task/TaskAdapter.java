package com.example.des.studentmanagerredux.task;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Matt on 11/3/16.
 *
 * Extension of CursorAdapter for displaying database queries using a TaskItemView control
 */

public class TaskAdapter extends CursorAdapter implements Serializable{

    public TaskAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    /**
     * Internal use - returns a new view to add to the Container
     * @param context - Context of adapter
     * @param cursor - Cursor to linked database
     * @param parent - Parent of the Container
     * @return created TaskItemView
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        TaskItemView newTaskView = new TaskItemView(context);
        return newTaskView;
    }

    /**
     * Internal use - updates view with relevant data from cursor
     * @param view - View to bind
     * @param context - Context of adapter
     * @param cursor - Cursor from which to pull data
     */

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        //Database empty
        if(cursor.getCount() == 0) {
            return;
        }

        //Reject non-TaskItemViews
        if(view instanceof TaskItemView)
        {
            //Reconstruct TaskItem from DB data
            Calendar taskStart = new GregorianCalendar(0, 0, 0);
            taskStart.setTimeInMillis(cursor.getLong(2));
            Calendar taskEnd = new GregorianCalendar(0, 0, 0);
            taskEnd.setTimeInMillis(cursor.getLong(3));
            int progress = cursor.getInt(4);
            boolean complete = Boolean.parseBoolean(cursor.getString(5));
            String title = cursor.getString(1);

            TaskItem newTask = new TaskItem(taskStart, taskEnd, progress, complete, title);
            //Push TaskItem into TaskView
            ((TaskItemView) view).setTask(newTask);
        }
        else
        {
            throw new IllegalArgumentException("Can't bind TaskItem to control other than TaskItemView");
        }
    }
}
