package com.example.des.studentmanagerredux.todo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.des.studentmanagerredux.ToDoList;

/**
 * Created by Nikhil on 11/11/2016.
 */

public class ToDoAdapter extends CursorAdapter {

    private ToDoList list;

    public ToDoAdapter(ToDoList context, Cursor c, int flags)
    {
        super(context, c, flags);
        list = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        ToDoListItemView newTaskView = new ToDoListItemView(context, list);
        return newTaskView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        if(cursor.getCount() == 0) {
            return;
        }

        if(view instanceof ToDoListItemView)
        {
            int progress = cursor.getInt(3);
            boolean complete = Boolean.parseBoolean(cursor.getString(2));
            String title = cursor.getString(1);

            ToDoListItem newTask = new ToDoListItem(title, progress, complete);
            ((ToDoListItemView) view).setTask(newTask);
        }
        else
        {
            throw new IllegalArgumentException("Can't bind TaskItem to control other than TaskItemView");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (((ToDoListItemView) view).isComplete())
            view.setBackgroundColor(Color.rgb(102,255,153));
        else {
            if (position % 2 == 1) {
                view.setBackgroundColor(Color.LTGRAY);
            } else {
                view.setBackgroundColor(Color.WHITE);
            }
        }

        return view;
    }
}