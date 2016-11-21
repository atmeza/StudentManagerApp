package com.example.des.studentmanagerredux;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.task.TaskItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private Context appContext;
    private EventDbHelper db;

    @Before
    public void useAppContext() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        db = new EventDbHelper(appContext);
        db.removeAllEvents();
    }

    @Test
    public void testBasicAdd() throws Exception {
        TaskItem t1 = new TaskItem(Calendar.getInstance(), "Task 1");
        TaskItem t2 = new TaskItem(Calendar.getInstance(), "Task 2");
        TaskItem t3 = new TaskItem(Calendar.getInstance(), "Task 3");

        db.addEvent(t1);
        db.addEvent(t2);
        db.addEvent(t3);

        Cursor c = db.getAllEvents();

        if(c.getCount() != 3)
        {
            fail("Unexpected number of entries returned");
        }
        else
        {
            c.moveToFirst();
            assertEquals("Incorrect name returned", c.getString(1), "Task 1");
            c.moveToNext();
            assertEquals("Incorrect name returned", c.getString(1), "Task 2");
            c.moveToNext();
            assertEquals("Incorrect name returned", c.getString(1), "Task 3");
        }
    }

    @Test
    public void testDatabaseClear() throws Exception {

        TaskItem t1 = new TaskItem(Calendar.getInstance(), "Task 1");
        TaskItem t2 = new TaskItem(Calendar.getInstance(), "Task 2");
        TaskItem t3 = new TaskItem(Calendar.getInstance(), "Task 3");

        db.addEvent(t1);
        db.addEvent(t2);
        db.addEvent(t3);

        db.removeAllEvents();

        Cursor c = db.getAllEvents();

        assertEquals("Database not clear", 0, c.getCount());
    }

    @Test
    public void testAddOnDate() throws Exception {
        db.addEvent(new TaskItem(new GregorianCalendar(1997, 0, 23), "Task 1"));
        db.addEvent(new TaskItem(new GregorianCalendar(1997, 0, 22), "Task 2"));
        db.addEvent(new TaskItem(new GregorianCalendar(1997, 0, 23), "Task 3"));
        db.addEvent(new TaskItem(new GregorianCalendar(2015, 11, 7), "Task 4"));

        Cursor c = db.getEventsOnDay(new GregorianCalendar(1997, 0, 23));

        if(c.getCount() != 2)
        {
            fail("Unexpected number of entries returned");
        }
        else
        {
            c.moveToFirst();
            assertEquals("Incorrect name event returned", c.getString(1), "Task 1");
            c.moveToNext();
            assertEquals("Incorrect name event returned", c.getString(1), "Task 3");
        }

        c = db.getEventsOnDay(new GregorianCalendar(2013, 0, 0));

        if(c.getCount() != 0)
        {
            fail("Expected no results, got results");
        }
    }

    @Test
    public void testRemoveSpecific() throws Exception {

        db.addEvent(new TaskItem(new GregorianCalendar(1997, 0, 23), "Task 1"));
        db.addEvent(new TaskItem(new GregorianCalendar(1997, 0, 22), "Task 2"));
        db.addEvent(new TaskItem(new GregorianCalendar(1997, 0, 23), "Task 3"));
        db.addEvent(new TaskItem(new GregorianCalendar(2015, 11, 7), "Task 4"));

        db.removeEvent(new TaskItem(new GregorianCalendar(1997, 0, 23), "Task 1"));

        Cursor c = db.getEventsOnDay(new GregorianCalendar(1997, 0, 23));

        if(c.getCount() != 1)
        {
            fail("Item not removed: got " + c.getCount() + " results");
        }
        else
        {
            c.moveToFirst();
            assertEquals("Incorrect event removed", c.getString(1), "Task 3");
        }
    }
}
