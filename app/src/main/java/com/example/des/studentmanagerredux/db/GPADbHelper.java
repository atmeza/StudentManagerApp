package com.example.des.studentmanagerredux.db;

/**
 * Created by Des on 11/13/2016.
 *
 * database for GPA objects, designed to be compatible with the current Calculator without
 * much change
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Calendar;
import java.util.*;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GPADbHelper extends SQLiteOpenHelper {

    // static variables so that database knows whether to sync to firebase
    private static boolean loggedIn; // whether user is logged in
    private static String username; // username of logged in user

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

    // sync this database with firebase, overwriting the older database with the newer one
    public void firebaseSync() {

    }

    // Database Version (not important to project)
    private static final int DATABASE_VERSION = 6;

    // Database Name (name of entire database)
    private static final String DATABASE_NAME = "classes";

    // Event table name (name of table of classes)
    private static final String TABLE_NAME = "class_table";

    // class_table Table Columns names (how to refer to each component in the database)
    // unique id of task in database (keeps duplicates distinct, not used to organize events)
    // not sure how mandatory this thing is, if it causes problems, can probably remove it
    private static final String KEY_ID = "_id";

    private static final String KEY_TITLE = "title"; // name of event (string)
    private static final String KEY_UNITS = "units"; // number of units of object
    private static final String KEY_GRADE = "grade"; // number corresponding to spinner grade options

    // Constructor for the Database helper, utilizes constants defined above to function
    public GPADbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // definition of the table, format of: _i, title, units, grade option
    @Override
    public void onCreate(SQLiteDatabase db) {
        String eventTable = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT NOT NULL, " +
                KEY_UNITS + " TEXT NOT NULL, " +
                KEY_GRADE + " INTEGER NOT NULL);";

        db.execSQL(eventTable); // table is created
    }

    // default thing for upgrading the database, ignore this
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    // pass in the individual parameters of the class (string title, string units, int grade)
    // and they will be combined into one row in the table
    public void addClass(String title, String units, int grade) {

        // get this database, value thingy
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // get the title, start, end, progress, and completion status
        values.put(KEY_TITLE, title); // get title
        values.put(KEY_UNITS, units); // get units
        values.put(KEY_GRADE, grade); //get spinner option corresponding to a grade

        // insert the tuple into the table
        db.insert(TABLE_NAME, null, values);

    }

    // gets all classes in the database, used to recreate the database
    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        return db.rawQuery(selectQuery, null);
    }

    // removes all classes that meet the criteria of the class passed in from the database
    public void removeEvent(String title, String units, int grade) {

        SQLiteDatabase db = this.getWritableDatabase(); // get database

        // delete all results such that the title, units, and grade match that in the database
        // **WARNING** this will delete perfect duplicates as well
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                KEY_TITLE + " = \"" + title + "\" AND " +
                KEY_UNITS + " = \"" + units + "\" AND " +
                KEY_GRADE + " = " + grade + ";");

    }

    // removes everything in the database, used to overwrite database whenever the page is left
    // so that the classes are correct
    public void removeAllClasses() {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        db.execSQL("DELETE FROM " + TABLE_NAME + ";"); // remove everything from

    }
}
