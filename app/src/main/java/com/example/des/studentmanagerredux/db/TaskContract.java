package com.example.des.studentmanagerredux.db;

import android.provider.BaseColumns;

/**
 * Created by Nikhil on 10/21/2016.
 */

public class TaskContract {
    public static final String DB_NAME = "com.aziflaj.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
    }
}
