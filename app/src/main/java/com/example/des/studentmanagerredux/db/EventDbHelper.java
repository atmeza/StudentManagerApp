package com.example.des.studentmanagerredux.db;

/**
 * Created by Des on 11/1/2016.
 *
 * database for events in the planner/calendar, accepts and returns data as TaskItems
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Calendar;
import java.util.*;

import com.example.des.studentmanagerredux.TaskItem;

public class EventDbHelper extends SQLiteOpenHelper {

    // Database Version (not important to project)
    private static final int DATABASE_VERSION = 1;

    // Database Name (name of entire database)
    private static final String DATABASE_NAME = "events";

    // Event table name (name of table of events)
    private static final String TABLE_NAME = "event_table";

    // event_table Table Columns names (how to refer to each component in the database)
    // unique id of task in database (keeps duplicates distinct, not used to organize events)
    // not sure how mandatory this thing is, if it causes problems, can probably remove it
    private static final String KEY_ID = "id";

    private static final String KEY_TITLE = "title"; // name of event (string)
    private static final String KEY_START_DATE = "start"; // start date (milliseconds stored as int)
    private static final String KEY_END_DATE = "end"; // end date (milliseconds stored as int)
    private static final String KEY_PROGRESS = "progress"; // event progress (int)
    private static final String KEY_COMPLETE = "complete"; // is event complete (either "true" or "false")

    // Constructor for the Database helper, utilizes constants defined above to function
    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // definition of the table, format of: id, title, start_date, end_date, progress, complete
    @Override
    public void onCreate(SQLiteDatabase db) {
        String eventTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT NOT NULL, " +
                KEY_START_DATE + "INTEGER NOT NULL, " +
                KEY_END_DATE  + "INTEGER, " +
                KEY_PROGRESS + "INTEGER NOT NULL, " +
                KEY_COMPLETE + "TEXT NOT NULL);";

        db.execSQL(eventTable); // table is created
    }

    // default thing for upgrading the database, ignore this
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    public void addEvent(TaskItem task) {

        // get this database, value thingy
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // get the title, start, end, progress, and completion status
        values.put(KEY_TITLE, task.getTitle()); // get title
        values.put(KEY_START_DATE, task.getStart().getTimeInMillis()); // get start in milliseconds
        values.put(KEY_END_DATE, task.getEnd().getTimeInMillis()); //get end in miliseconds
        values.put(KEY_PROGRESS, task.getProgress()); // get progress

        // if task is complete, ues "true", else use false
        if (task.isComplete()) {
            values.put(KEY_COMPLETE, "true");
        }
        else values.put(KEY_COMPLETE, "false");

        // insert the tuple into the table
        db.insert(TABLE_NAME, null, values);

    }

    // Takes in a Calendar object and returns an ArrayList of all TaskItems in the database that
    // have a start time within a day of it, sorted by start time
    // (make calendar time = 0:00 for all events exactly on that date)
    public List<TaskItem> getEventsOnDay(Calendar cal) {

        // ArrayList of TaskItems that will be returned
        ArrayList<TaskItem> events = new ArrayList<TaskItem>();

        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        // get all events such that the start time is greater than or equal to cal and less than
        // cal plus another day
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE " + KEY_START_DATE + " >= " + cal.getTimeInMillis() + " " +
                "AND " + KEY_START_DATE + " < " + (cal.getTimeInMillis() +
                java.util.concurrent.TimeUnit.MILLISECONDS.convert(1, java.util.concurrent.TimeUnit.DAYS)
                + " ORDER BY " + KEY_START_DATE + " asc;");

        // loop through all of the results of the above query
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {

            // for each result
            do {

                // get fields from the result
                String title = cursor.getString(1);

                Calendar start = new GregorianCalendar();
                start.setTimeInMillis(cursor.getLong(2));

                Calendar end = new GregorianCalendar();
                end.setTimeInMillis(cursor.getLong(3));

                int progress = cursor.getInt(4);

                boolean complete = false;
                if (cursor.getString(5) == "true") {
                    complete = true;
                }

                // create the TaskItem from the parameters in the result
                TaskItem event = new TaskItem(start, end, progress, complete, title);

                events.add(event); // insert event into list of events
            } while (cursor.moveToNext()); // move to next result

        }

        return events; // return the list of events

    }

    // removes all elements in the table that share all characteristics with the input TaskItem
    // **IMPORTANT** If there are any events that are exact copies, all of them will be deleted as
    // of right now
    public void removeEvent(TaskItem task) {

        SQLiteDatabase db = this.getWritableDatabase(); // get database

        // delete all results such that the title, start date, end date, and progress are the same
        // as the one passed in
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                KEY_TITLE + " = \"" + task.getTitle() + "\" AND " +
                KEY_START_DATE + " = " + task.getStart().getTimeInMillis() + " AND " +
                KEY_END_DATE + " = " + task.getEnd().getTimeInMillis() + " AND " +
                KEY_PROGRESS + " = " + task.getProgress() + " AND " +
                KEY_COMPLETE + " = " + String.valueOf(task.isComplete()) + ";");

    }
}