package com.example.des.studentmanagerredux.task;

import android.content.Context;
import android.database.Cursor;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Matt on 11/3/16.
 */

public class TaskAdapter extends CursorAdapter {

    public TaskAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        TaskItemView newTaskView = new TaskItemView(context);
        return newTaskView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        if(cursor.getCount() == 0) {
            return;
        }

        if(view instanceof TaskItemView)
        {
            Calendar taskStart = new GregorianCalendar();
            taskStart.setTimeInMillis(cursor.getLong(2));
            Calendar taskEnd = new GregorianCalendar();
            taskEnd.setTimeInMillis(cursor.getLong(3));
            String title = cursor.getString(1);

            TaskItem newTask = new TaskItem(taskStart, taskEnd, title);
            ((TaskItemView) view).setTask(newTask);
        }
        else
        {
            throw new IllegalArgumentException("Can't bind TaskItem to control other than TaskItemView");
        }
    }
}
