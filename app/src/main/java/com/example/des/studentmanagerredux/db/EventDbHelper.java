package com.example.des.studentmanagerredux.db;

/**
 * Created by Des on 11/1/2016.
 *
 * database for events in the planner/calendar, accepts and returns data as TaskItems
 */

//TODO: Can you make the get by date method return a Cursor instead? Also, attempting to insert into the SQL database causes an error
//Test the above by running, going to the todo list, and pushing the add button

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.*;

import com.example.des.studentmanagerredux.task.TaskItem;
import com.example.des.studentmanagerredux.todo.ToDoListItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDbHelper extends SQLiteOpenHelper implements Serializable {
    // Database Version (not important to project)
    private static final int DATABASE_VERSION = 11;

    // Database Name (name of entire database)
    private static final String DATABASE_NAME = "events";

    // Event table name (name of table of events)
    private static final String TABLE_NAME = "event_table";

    // event_table Table Columns names (how to refer to each component in the database)
    // unique id of task in database (keeps duplicates distinct, not used to organize events)
    // not sure how mandatory this thing is, if it causes problems, can probably remove it
    private static final String KEY_ID = "_id";

    private static final String KEY_TITLE = "title"; // name of event (string)
    private static final String KEY_START_DATE = "start"; // start date (milliseconds stored as int)
    private static final String KEY_END_DATE = "end"; // end date (milliseconds stored as int)
    private static final String KEY_PROGRESS = "progress"; // event progress (int)
    private static final String KEY_COMPLETE = "complete"; // is event complete (either "true" or "false")

    // static variables so that database knows whether to sync to firebase
    private static boolean loggedIn; // whether user is logged in
    private static String username; // username of logged in user

    private DatabaseReference mDataRef;

    // tell database that user is logged in
    public static void login(String un) {
        loggedIn = true;
        username = un;
    }

    // tell database that user is logged out
    public static void logout() {
        loggedIn = false;
    }

    // notify whether user is currently logged in
    public static boolean loggedIn() {return loggedIn;}

    // Constructor for the Database helper, utilizes constants defined above to function
    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // overwrite the firebase database with the local database
    public void firebaseOverwrite() {

        System.out.println("overwriting firebase");

        // reset the firebase database and fill it with the rows from the local database
        mDataRef = FirebaseDatabase.getInstance().getReference("users");
        mDataRef = mDataRef.child(username);
        mDataRef = mDataRef.child("Events");

        System.out.println(mDataRef.toString());

        mDataRef.removeValue();

        // get cursor of all events locally
        Cursor cursor = getAllEvents();

        //loop through all the database events
        cursor.moveToFirst();
        Map<String, String> eventMap = new HashMap<String, String>();
        while (!cursor.isAfterLast()) {
            String eventName = cursor.getString(1);
            Long eventStart = cursor.getLong(2);
            Long eventEnd = cursor.getLong(3);

            System.out.println(eventName + ", " + eventStart + ", " + eventEnd);

            // get a unique id for this event
            DatabaseReference uniqueKey = mDataRef.push();
            uniqueKey.setValue(eventName + eventStart + eventEnd);

            String uniqueString = uniqueKey.getKey();

            //Use the new reference to add the data
            mDataRef = mDataRef.child(uniqueString);

            System.out.println(mDataRef.toString());

            // map of data being added
            eventMap.clear();

            eventMap.put("name", eventName);
            eventMap.put("start", "" + eventStart);
            eventMap.put("end", "" + eventEnd);

            mDataRef.setValue(eventMap);
            mDataRef = mDataRef.getParent();
            System.out.println(mDataRef.toString());

            cursor.moveToNext();
        }


    }

    // overwrite the local database with hte data from firebase
    public void localOverwrite() {

        System.out.println("overwriting local");

        // move firebase to the user's grades
        mDataRef = FirebaseDatabase.getInstance().getReference("users");
        mDataRef = mDataRef.child(username);
        mDataRef = mDataRef.child("Events");

        System.out.println(mDataRef.toString());

        // clear the local database
        this.removeAllEvents();

        // parse through the firebase, inserting each set of three elements into the local
        // database as one row
        mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    int count = 0;
                    String newTitle = "";
                    String newStart = "";
                    String newEnd = "";
                    for (DataSnapshot postPostSnapshot: postSnapshot.getChildren()) {
                        Object data = postPostSnapshot.getValue();
                        System.out.println(data.toString());
                        count++;

                        // if count = 1, then the next value is the end
                        if (count == 1) {
                            System.out.println(data.toString());
                            newEnd = data.toString();
                        }

                        // if count = 2, then the next value is the name
                        if (count == 2) {
                            System.out.println(data.toString());
                            newTitle = data.toString();
                        }

                        // if count = 3, then next value is the start, read it, then store all three
                        // values and reset the count
                        if (count == 3) {
                            System.out.println(data.toString());
                            newStart = data.toString();

                            GregorianCalendar startCal = new GregorianCalendar();
                            GregorianCalendar endCal = new GregorianCalendar();
                            startCal.setTimeInMillis(Long.parseLong(newStart));
                            endCal.setTimeInMillis(Long.parseLong(newEnd));

                            addEvent(new TaskItem(startCal, endCal, newTitle));
                            count = 0;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // definition of the table, format of: _id, title, start, end, progress, complete
    @Override
    public void onCreate(SQLiteDatabase db) {
        String eventTable = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT NOT NULL, " +
                KEY_START_DATE + " INTEGER NOT NULL, " +
                KEY_END_DATE  + " INTEGER, " +
                KEY_PROGRESS + " INTEGER NOT NULL, " +
                KEY_COMPLETE + " TEXT NOT NULL);";

        db.execSQL(eventTable); // table is created
    }

    // default thing for upgrading the database, ignore this
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
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

        firebaseOverwrite();

    }

    // Takes in a Calendar object and returns an ArrayList of all TaskItems in the database that
    // have a start time within a day of it, sorted by start time
    // (make calendar time = 0:00 for all events exactly on that date)
    public Cursor getEventsOnDay(Calendar cal) {

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
        return cursor;
        /*
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
*/
    }

    /*
    * Matt wrote this; Desmond, feel free to modify it as necessary; it's just supposed to
    * return a cursor pointing to the entire table of events
    */

    public Cursor getAllEvents()
    {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        return db.rawQuery(selectQuery, null);
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
                KEY_COMPLETE + " = \"" + String.valueOf(task.isComplete()) + "\";");

        firebaseOverwrite();

    }

    public void removeAllEvents()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}