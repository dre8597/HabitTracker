package com.example.demondrelivingston.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.demondrelivingston.habittracker.data.HabitContract.HabitEntry;

/**
 * Created by demondrelivingston on 12/22/17.
 */

public class HabitDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "tracker.db";

    /**
     * Database version. If you change the database schema, you increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a string that contains the SQL statement to create the habits table
        String SQL_CREATE_HABIT_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL);";
        //Execute the SQL statement
        db.execSQL(SQL_CREATE_HABIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
