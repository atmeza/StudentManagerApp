package com.example.des.studentmanagerredux.todo;

/**
 * Created by Nikhil on 11/11/2016.
 */

public class ToDoListItem {
    public final static int PROGRESS_MAX = 100;

    private String title;
    private int progress;
    private boolean complete;

    public ToDoListItem(String title, int progress, boolean complete) {
        this.title = title;
        this.progress = progress;
        this.complete = complete;
    }

    public ToDoListItem(String title, boolean complete) {
        this.title = title;
        this.progress = complete ? PROGRESS_MAX : 0;
        this.complete = complete;
    }

    public ToDoListItem(String title, int progress) {
        this.title = title;
        this.progress = progress;
        this.complete = progress == PROGRESS_MAX;
    }

    public String getTitle() { return title; }

    public int getProgress() { return progress; }

    public boolean isComplete() { return complete; }

    public void setProgress(int progress)
    {
        if(progress > PROGRESS_MAX || progress < 0)
        {
            throw new IllegalArgumentException("Invalid progress value assigned");
        }
        else {
            this.progress = progress;
            this.complete = (progress == PROGRESS_MAX);
        }
    }

    public void markComplete(boolean complete)
    {
        this.complete = complete;
        this.progress = complete ? PROGRESS_MAX : progress;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}
