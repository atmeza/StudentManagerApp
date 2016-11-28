package com.example.des.studentmanagerredux.db;

/**
 * Created by Des on 11/13/2016.
 *
 * database for GPA objects, designed to be compatible with the current Calculator without
 * much change
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;


public class GPADbHelper extends SQLiteOpenHelper {

    // static variables so that database knows whether to sync to firebase
    private static boolean loggedIn; // whether user is logged in
    private static String username; // username of logged in user
    private static long lastAccess; // last time of access
    private DatabaseReference mDataRef;
    private Object firebaseTime;
    private Context context;

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

    public static String getUsername() {
        return username;
    }

    // overwrite the firebase database with the local database
    public void firebaseOverwrite() {

        System.out.println("overwriting firebase");

        // reset the firebase database and fill it with the rows from the local database
        mDataRef = FirebaseDatabase.getInstance().getReference("users");
        mDataRef = mDataRef.child(username);
        mDataRef = mDataRef.child("Grades");

        System.out.println(mDataRef.toString());

        mDataRef.removeValue();

        // get cursor of all events locally
        Cursor cursor = getAllClasses();

        //loop through all the database events
        cursor.moveToFirst();
        Map<String, String> gradeMap = new HashMap<String, String>();
        while (!cursor.isAfterLast()) {
            String className = cursor.getString(1);
            String classUnits = cursor.getString(2);
            int classGrade = cursor.getInt(3);

            System.out.println(className + ", " + classUnits + ", " + classGrade);

            // get a unique id for this event
            DatabaseReference uniqueKey = mDataRef.push();
            uniqueKey.setValue(className + classUnits + classGrade);

            String uniqueString = uniqueKey.getKey();

            //Use the new reference to add the data
            mDataRef = mDataRef.child(uniqueString);

            System.out.println(mDataRef.toString());

            // map of data being added
            gradeMap.clear();

            gradeMap.put("class", className);
            gradeMap.put("units", classUnits);
            gradeMap.put("grade", "" + classGrade);

            mDataRef.setValue(gradeMap);
            mDataRef = mDataRef.getParent();
            System.out.println(mDataRef.toString());

            cursor.moveToNext();
        }

        /* // get timestamp of most recent firebase sync
        mDataRef = FirebaseDatabase.getInstance().getReference(username + "/LastAccess");
        mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseTime = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        System.out.println(firebaseTime.toString());
        GregorianCalendar FBCal = new GregorianCalendar();
        FBCal.setTimeInMillis(Long.getLong(firebaseTime.toString()).longValue());
        System.out.println(FBCal.getTimeInMillis());

        SharedPreferences sharedPreferences = context.getSharedPreferences("timestamp", MODE_PRIVATE);
        long localTime = sharedPreferences.getLong("time", 0);

        GregorianCalendar localCal = new GregorianCalendar();
        localCal.setTimeInMillis(localTime);

        // compare the local and firebase times

        // if the local time is more recent than the firebase time
        if (localCal.compareTo(FBCal) > 0) {

            // reset the firebase database and fill it with the rows from the local database
            mDataRef = FirebaseDatabase.getInstance().getReference(username + "/Grades");

            // get cursor of all events locally
            Cursor cursor = getAllClasses();

            //loop through all the database events
            cursor.moveToFirst();
            Map<String, String> gradeMap = new HashMap<String, String>();
            while (!cursor.isAfterLast()) {
                String className = cursor.getString(1);
                String classUnits = cursor.getString(2);
                int classGrade = cursor.getInt(3);

                // get a unique id for this event
                String uniqueKey = mDataRef.push().getKey();

                //Use the new reference to add the data
                mDataRef.child(uniqueKey);

                // map of data being added
                gradeMap.clear();

                gradeMap.put("class", className);
                gradeMap.put("units", classUnits);
                gradeMap.put("grade", "" + classGrade);

                cursor.moveToNext();
            }

        }

        // otherwise, if the local time is less recent than the firebase time, then clear the local
        // database and fill it with the rows from firebase

        // update the timestamps */

    }

    // overwrite the local database with hte data from firebase
    public void localOverwrite() {

        System.out.println("overwriting local");

        // move firebase to the user's grades
        mDataRef = FirebaseDatabase.getInstance().getReference("users");
        mDataRef = mDataRef.child(username);
        mDataRef = mDataRef.child("Grades");

        System.out.println(mDataRef.toString());

        // clear the local database
        this.removeAllClasses();

        // parse through the firebase, inserting each set of three elements into the local
        // database as one row
        mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    int count = 0;
                    String newClass = "";
                    String newUnits = "";
                    String newGrade = "";
                    for (DataSnapshot postPostSnapshot: postSnapshot.getChildren()) {
                        Object data = postPostSnapshot.getValue();
                        System.out.println(data.toString());
                        count++;

                        // if count = 1, then the next value is the class name
                        if (count == 1) {
                            newClass = data.toString();
                        }

                        // if count = 2, then the next value is the grade name
                        if (count == 2) {
                            newUnits = data.toString();
                        }

                        // if count = 3, then next value is the units, read it, then store all three
                        // values and reset the count
                        if (count == 3) {
                            newGrade = data.toString();

                            addClass(newClass, newGrade, Integer.parseInt(newUnits));
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

    // Database Version (not important to project)
    private static final int DATABASE_VERSION = 8;

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
        this.context = context;
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
