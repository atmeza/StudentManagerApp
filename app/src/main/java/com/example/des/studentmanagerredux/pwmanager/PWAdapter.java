package com.example.des.studentmanagerredux.pwmanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.des.studentmanagerredux.PasswordManager;
import com.example.des.studentmanagerredux.ToDoList;
import com.example.des.studentmanagerredux.pwmanager.PWItemView;

/**
 * Created by Nikhil on 11/11/2016.
 *
 * Adapter used to display PWItem on PasswordManager
 *
 */

public class PWAdapter extends CursorAdapter {

    // The list to display PWItems on
    private PasswordManager list;

    public PWAdapter(PasswordManager context, Cursor c, int flags)
    {
        super(context, c, flags);
        list = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        PWItemView newTaskView = new PWItemView(context, list);
        return newTaskView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        if(cursor.getCount() == 0) {
            return;
        }

        if(view instanceof PWItemView)
        {
            String entryTitle = cursor.getString(1);
            String userName = cursor.getString(2);
            String password = cursor.getString(3);

            PWItem newItem = new PWItem(entryTitle, userName, password);
            ((PWItemView) view).setTask(newItem);
        }
        else
        {
            throw new IllegalArgumentException("Can't bind TaskItem to control other than TaskItemView");
        }
    }

    // Alternates gray and white backgrounds on display
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.LTGRAY);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        return view;
    }
}