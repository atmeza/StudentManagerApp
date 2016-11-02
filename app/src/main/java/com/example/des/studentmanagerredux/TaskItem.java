package com.example.des.studentmanagerredux;

import java.util.Calendar;

/**
 * Created by Matt on 10/28/16.
 */

public class TaskItem {
    public final static int PROGRESS_MAX = 100;

    private Calendar taskStart;
    private Calendar taskEnd;
    private int progress;
    private boolean complete;
    private String title;

    public TaskItem(Calendar start, Calendar end, int progress, boolean complete, String title)
    {
        this.taskStart = start;
        this.taskEnd = end;
        this.progress = progress;
        this.complete = complete;
        this.title = title;
    }

    public TaskItem(Calendar start, Calendar end, int progress, String title)
    {
        this(start, end, progress, progress == PROGRESS_MAX, title);
    }

    public TaskItem(Calendar start, Calendar end, boolean complete, String title)
    {
        this(start, end, complete ? PROGRESS_MAX : 0, complete, title);
    }

    public TaskItem(Calendar start, Calendar end, String title)
    {
        this(start, end, false, title);
    }

    public TaskItem(Calendar end, int progress, String title)
    {
        this(end, end, progress, title);
    }

    public TaskItem(Calendar end, boolean complete, String title)
    {
        this(end, end, complete, title);
    }

    public TaskItem(Calendar end, String title)
    {
        this(end, end, false, title);
    }

    public int getProgress()
    {
        return progress;
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

    public boolean isComplete()
    {
        return complete;
    }

    public void setProgress(int progress)
    {
        if(progress > PROGRESS_MAX || progress < 0)
        {
            throw new IllegalArgumentException("Invalid progress value assigned");
        }
        else {
            this.progress = progress;
            this.complete = progress == PROGRESS_MAX;
        }
    }

    public void markComplete(boolean complete)
    {
        this.complete = complete;
        this.progress = complete ? PROGRESS_MAX : progress;
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
