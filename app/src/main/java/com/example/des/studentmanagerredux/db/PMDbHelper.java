package com.example.des.studentmanagerredux.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.studentmanagerredux.pwmanager.PWItem;

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
    }

    public void removeEntry(PWItem entry) {
        SQLiteDatabase db = this.getWritableDatabase(); // get database

        db.execSQL("DELETE FROM " + TABLE + " WHERE " +
                COL_ENTRY_TITLE + " = \"" + entry.getTitle() + "\" AND " +
                ENTRY_USERNAME + " = \"" + entry.getUserName() + "\" AND " +
                ENTRY_PASSWORD + " = \"" + entry.getPassword() + "\";");
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
}
