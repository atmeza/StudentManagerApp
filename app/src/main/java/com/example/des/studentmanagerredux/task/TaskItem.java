package com.example.des.studentmanagerredux.task;

import com.example.des.studentmanagerredux.db.EventDbHelper;

import java.util.Calendar;

/**
 * Created by Matt on 10/28/16.
 */

public class TaskItem {
    public final static int PROGRESS_MAX = 100;

    private Calendar taskStart;
    private Calendar taskEnd;
    private String title;

    private EventDbHelper holdingDb;

    public TaskItem(Calendar start, Calendar end, String title)
    {
        this.taskStart = start;
        this.taskEnd = end;
        this.title = title;
    }


    public TaskItem(Calendar end, String title)
    {
        this(end, end, title);
    }

    public String getTitle()
    {
        return title;
    }

    public Calendar getStart()
    {
        return taskStart;
    }

    public Calendar getEnd()
    {
        return taskEnd;
    }

    public void setTaskStart(Calendar start)
    {
        if(start.after(taskEnd))
        {
            throw new IllegalArgumentException("Start time was after end time");
        }
        else
        {
            this.taskStart = start;
        }
    }

    public void setTaskEnd(Calendar end)
    {
        if(end.before(taskStart))
        {
            throw new IllegalArgumentException("End time was before start time");
        }
        else
        {
            this.taskEnd = end;
        }
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
