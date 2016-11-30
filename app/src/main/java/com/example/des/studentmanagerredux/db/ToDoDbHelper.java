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
import com.example.des.studentmanagerredux.task.TaskItem;
import com.example.des.studentmanagerredux.todo.ToDoAdapter;
import com.example.des.studentmanagerredux.todo.ToDoListItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ToDoDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "com.aziflaj.todolist.db";
    public static final int DB_VERSION = 5;

    private static final String TABLE = "tasks";

    private static final String KEY_ID = "_id";

    private static final String COL_TASK_TITLE = "title";
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

    // overwrite the firebase database with the local database
    public void firebaseOverwrite() {

        System.out.println("overwriting firebase");

        // reset the firebase database and fill it with the rows from the local database
        mDataRef = FirebaseDatabase.getInstance().getReference("users");
        mDataRef = mDataRef.child(username);
        mDataRef = mDataRef.child("ToDo");

        System.out.println(mDataRef.toString());

        mDataRef.removeValue();

        // get cursor of all events locally
        Cursor cursor = getAllEvents();

        //loop through all the database events
        cursor.moveToFirst();
        Map<String, String> taskMap = new HashMap<String, String>();
        while (!cursor.isAfterLast()) {
            String taskName = cursor.getString(1);
            String taskDone = cursor.getString(2);
            int taskProgress = cursor.getInt(3);

            System.out.println(taskName + ", " + taskDone + ", " + taskProgress);

            // get a unique id for this event
            DatabaseReference uniqueKey = mDataRef.push();
            uniqueKey.setValue(taskName + taskDone + taskProgress);

            String uniqueString = uniqueKey.getKey();

            //Use the new reference to add the data
            mDataRef = mDataRef.child(uniqueString);

            System.out.println(mDataRef.toString());

            // map of data being added
            taskMap.clear();

            taskMap.put("name", taskName);
            taskMap.put("done", taskDone);
            taskMap.put("progress", "" + taskProgress);

            mDataRef.setValue(taskMap);
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
        mDataRef = mDataRef.child("ToDo");

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
                    boolean newDone = false;
                    String newProgress = "";
                    for (DataSnapshot postPostSnapshot: postSnapshot.getChildren()) {
                        Object data = postPostSnapshot.getValue();
                        System.out.println(data.toString());
                        count++;

                        // if count = 1, then the next value is the
                        if (count == 1) {
                            System.out.println(data.toString());
                            if (data.toString().equals("true")) {
                                newDone = true;
                            }

                            else newDone = false;
                        }

                        // if count = 2, then the next value is the
                        if (count == 2) {
                            newTitle = data.toString();
                        }

                        // if count = 3, then next value is the , read it, then store all three
                        // values and reset the count
                        if (count == 3) {
                            newProgress = data.toString();

                            addEvent(new ToDoListItem(newTitle, Integer.parseInt(newProgress), newDone));
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

        // update firebase
        firebaseOverwrite();
    }

    /* Checks if name is already in database to prevent duplicates */
    public boolean hasEvent(SQLiteDatabase db, String eventName) {

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

        String selectQuery = "SELECT * FROM " + TABLE + " ORDER BY " + KEY_COMPLETE + " asc;";
        //String selectQuery = "SELECT * FROM " + TABLE;

        Cursor c = db.rawQuery(selectQuery, null);
        return c;
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

        // update firebase
        firebaseOverwrite();

    }

    public boolean changeEventName(ToDoListItem task, String newName) {

        SQLiteDatabase db = this.getWritableDatabase(); // get database

        if (!hasEvent(db, newName)) {
            db.execSQL("UPDATE " + TABLE + " SET " +
                COL_TASK_TITLE + " = \"" + newName + "\" WHERE " +
                COL_TASK_TITLE + " = \"" + task.getTitle() + "\";");

            // update firebase
            firebaseOverwrite();

            return true;
        }

        return false; // if name is already present
    }

    public void updateProgress(ToDoListItem task, int newProgressValue) {
        SQLiteDatabase db = this.getWritableDatabase(); // get database
        db.execSQL("UPDATE " + TABLE + " SET " +
                KEY_PROGRESS + " = \"" + newProgressValue + "\" WHERE " +
                COL_TASK_TITLE + " = \"" + task.getTitle() + "\";");

        // update firebase
        firebaseOverwrite();
    }

    public void updateCheckbox(ToDoListItem task, boolean isChecked) {
        SQLiteDatabase db = this.getWritableDatabase(); // get database
        db.execSQL("UPDATE " + TABLE + " SET " +
                KEY_COMPLETE + " = \"" + isChecked + "\" WHERE " +
                COL_TASK_TITLE + " = \"" + task.getTitle() + "\";");

        // update firebase
        firebaseOverwrite();
        db.close();
    }

    // removes everything in the database, used to overwrite database when syncing with firebase
    public void removeAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        db.execSQL("DELETE FROM " + TABLE + ";"); // remove everything from

    }

    // remove all tasks and then sync with firebase
    public void removeTasksSync() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE + ";");
        firebaseOverwrite();
    }
}