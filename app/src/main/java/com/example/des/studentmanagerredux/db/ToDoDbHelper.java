package com.example.des.studentmanagerredux.db;

/**
 * Created by Matt and Nikhil on 10/21/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.studentmanagerredux.ToDoList;
import com.example.des.studentmanagerredux.todo.ToDoAdapter;
import com.example.des.studentmanagerredux.todo.ToDoListItem;

public class ToDoDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "com.aziflaj.todolist.db";
    public static final int DB_VERSION = 2;

    private static final String TABLE = "tasks";

    private static final String KEY_ID = "_id";

    private static final String COL_TASK_TITLE = "title";
    private static final String KEY_PROGRESS = "progress"; // event progress (int)
    private static final String KEY_COMPLETE = "complete"; // is event complete (either "true" or "false")

    public ToDoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK_TITLE + " TEXT NOT NULL, " +
                KEY_COMPLETE + " TEXT NOT NULL, " +
                KEY_PROGRESS + " INTEGER NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addEvent(ToDoListItem task) {

        // get this database, value thingy
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_TASK_TITLE, task.getTitle()); // get title
        values.put(KEY_PROGRESS, task.getProgress()); // get progress

        // if task is complete, ues "true", else use false
        if (task.isComplete()) {
            values.put(KEY_COMPLETE, "true");
        }
        else values.put(KEY_COMPLETE, "false");

        // insert the tuple into the table
        db.insert(TABLE, null, values);

    }

    /* Checks if name is already in database to prevent duplicates */
    private boolean hasEvent(SQLiteDatabase db, String eventName) {

        String query = "SELECT * FROM " + TABLE + " WHERE " + COL_TASK_TITLE + "='" +
                eventName + "';";
        Cursor rows = db.rawQuery(query, null);

        return (rows.getCount() != 0);

    }

    /*
    * Matt wrote this; Desmond, feel free to modify it as necessary; it's just supposed to
    * return a cursor pointing to the entire table of events
    */

    public Cursor getAllEvents()
    {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        String selectQuery = "SELECT * FROM " + TABLE;

        return db.rawQuery(selectQuery, null);
    }

    // removes all elements in the table that share all characteristics with the input TaskItem
    // **IMPORTANT** If there are any events that are exact copies, all of them will be deleted as
    // of right now
    public void removeEvent(ToDoListItem task) {

        SQLiteDatabase db = this.getWritableDatabase(); // get database

        // delete all results such that the title, start date, end date, and progress are the same
        // as the one passed in
        db.execSQL("DELETE FROM " + TABLE + " WHERE " +
                COL_TASK_TITLE + " = \"" + task.getTitle() + "\" AND " +
                KEY_PROGRESS + " = " + task.getProgress() + " AND " +
                KEY_COMPLETE + " = \"" + String.valueOf(task.isComplete()) + "\";");

    }

    public boolean changeEventName(ToDoListItem task, String newName) {

        SQLiteDatabase db = this.getWritableDatabase(); // get database

        if (!hasEvent(db, newName)) {
            db.execSQL("UPDATE " + TABLE + " SET " +
                COL_TASK_TITLE + " = \"" + newName + "\" WHERE " +
                COL_TASK_TITLE + " = \"" + task.getTitle() + "\";");
            return true;
        }

        return false; // if name is already present
    }

    // TODO: SQL Command to update Progress int (not sure if works)
    public void updateProgress(ToDoListItem task, int newProgressValue) {
        SQLiteDatabase db = this.getWritableDatabase(); // get database
        db.execSQL("UPDATE " + TABLE + " SET " +
                KEY_PROGRESS + " = \"" + newProgressValue + "\" WHERE " +
                COL_TASK_TITLE + " = \"" + task.getTitle() + "\";");
    }
}