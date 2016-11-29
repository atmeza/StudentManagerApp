package com.example.des.studentmanagerredux.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.des.studentmanagerredux.pwmanager.PWItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikhil on 11/26/2016.
 */

public class PMDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "com.aziflaj.passwordmanager.db";
    public static final int DB_VERSION = 1;

    private static final String TABLE = "items";

    private static final String KEY_ID = "_id";
    private static final String COL_ENTRY_TITLE = "title"; // name of service (e.g. Facebook, TritonEd)
    private static final String ENTRY_USERNAME = "username";
    private static final String ENTRY_PASSWORD = "password";

    public PMDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // static variables so that database knows whether to sync to firebase
    private static boolean loggedIn; // whether user is logged in
    private static String username; // username of logged in user
    private static long lastAccess; // last time of access
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

    public static String getUsername() {
        return username;
    }

    // overwrite the firebase database with the local database
    public void firebaseOverwrite() {

        System.out.println("overwriting firebase");

        // reset the firebase database and fill it with the rows from the local database
        mDataRef = FirebaseDatabase.getInstance().getReference("users");
        mDataRef = mDataRef.child(username);
        mDataRef = mDataRef.child("Passwords");

        System.out.println(mDataRef.toString());

        mDataRef.removeValue();

        // get cursor of all events locally
        Cursor cursor = getAllEvents();

        //loop through all the database events
        cursor.moveToFirst();
        Map<String, String> gradeMap = new HashMap<String, String>();
        while (!cursor.isAfterLast()) {
            String pwName = cursor.getString(1);
            String pwUsername = cursor.getString(2);
            String pwPassword = cursor.getString(3);

            System.out.println(pwName + ", " + pwUsername + ", " + pwPassword);

            // get a unique id for this event
            DatabaseReference uniqueKey = mDataRef.push();
            uniqueKey.setValue(pwName + pwUsername + pwPassword);

            String uniqueString = uniqueKey.getKey();

            //Use the new reference to add the data
            mDataRef = mDataRef.child(uniqueString);

            System.out.println(mDataRef.toString());

            // map of data being added
            gradeMap.clear();

            gradeMap.put("name", pwName);
            gradeMap.put("username", pwUsername);
            gradeMap.put("password", pwPassword);

            mDataRef.setValue(gradeMap);
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
        mDataRef = mDataRef.child("Passwords");

        System.out.println(mDataRef.toString());

        // clear the local database
        this.removeAllPWs();

        // parse through the firebase, inserting each set of three elements into the local
        // database as one row
        mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    int count = 0;
                    String newName = "";
                    String newUsername = "";
                    String newPassword = "";
                    for (DataSnapshot postPostSnapshot: postSnapshot.getChildren()) {
                        Object data = postPostSnapshot.getValue();
                        System.out.println(data.toString());
                        count++;

                        // if count = 1, then the next value is the class name
                        if (count == 1) {
                            newName = data.toString();
                        }

                        // if count = 2, then the next value is the grade name
                        if (count == 2) {
                            newUsername = data.toString();
                        }

                        // if count = 3, then next value is the units, read it, then store all three
                        // values and reset the count
                        if (count == 3) {
                            newPassword = data.toString();

                            PWItem newEntry = new PWItem(newName, newUsername, newPassword);
                            addEntry(newEntry);
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ENTRY_TITLE + " TEXT NOT NULL, " +
                ENTRY_USERNAME + " TEXT NOT NULL, " +
                ENTRY_PASSWORD + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addEntry(PWItem entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ENTRY_TITLE, entry.getTitle());
        values.put(ENTRY_USERNAME, entry.getUserName());
        values.put(ENTRY_PASSWORD, entry.getPassword());

        db.insert(TABLE, null, values);

        firebaseOverwrite();
    }

    public void removeEntry(PWItem entry) {
        SQLiteDatabase db = this.getWritableDatabase(); // get database

        db.execSQL("DELETE FROM " + TABLE + " WHERE " +
                COL_ENTRY_TITLE + " = \"" + entry.getTitle() + "\" AND " +
                ENTRY_USERNAME + " = \"" + entry.getUserName() + "\" AND " +
                ENTRY_PASSWORD + " = \"" + entry.getPassword() + "\";");

        firebaseOverwrite();
    }

    public boolean hasEvent(SQLiteDatabase db, String entryTitle, String entryUserName) {

        String query = "SELECT * FROM " + TABLE + " WHERE " +
                COL_ENTRY_TITLE + "='" + entryTitle + "' AND " +
                ENTRY_USERNAME + "='" + entryUserName + "';";
        Cursor rows = db.rawQuery(query, null);

        return (rows.getCount() != 0);

    }

    public boolean changeUserNameAndPassword(PWItem entry, String newUserName, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase(); // get database

        // Same service and UserName info is already present
        if (hasEvent(db, entry.getTitle(), newUserName) && !newUserName.equals(entry.getUserName()))
            return false;

        db.execSQL("UPDATE " + TABLE + " SET " +
                ENTRY_PASSWORD + " = \"" + newPassword + "\" WHERE " +
                COL_ENTRY_TITLE + " = \"" + entry.getTitle() + "\" AND " +
                ENTRY_USERNAME + " = \"" + entry.getUserName() +"\";");

        db.execSQL("UPDATE " + TABLE + " SET " +
                ENTRY_USERNAME + " = \"" + newUserName + "\" WHERE " +
                COL_ENTRY_TITLE + " = \"" + entry.getTitle() + "\" AND " +
                ENTRY_USERNAME + " = \"" + entry.getUserName() + "\";");

        firebaseOverwrite();
        return true;
    }

    // Used to update List of Items
    public Cursor getAllEvents()
    {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        String selectQuery = "SELECT * FROM " + TABLE + " ORDER BY " + KEY_ID;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }

    // removes everything in the database, used to overwrite database whenever the page is left
    // so that the classes are correct
    public void removeAllPWs() {
        SQLiteDatabase db = this.getWritableDatabase(); // database to work with

        db.execSQL("DELETE FROM " + TABLE + ";"); // remove everything from

    }
}
