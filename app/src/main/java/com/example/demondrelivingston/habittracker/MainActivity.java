package com.example.demondrelivingston.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demondrelivingston.habittracker.data.HabitContract.HabitEntry;
import com.example.demondrelivingston.habittracker.data.HabitDbHelper;


public class MainActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;
    /**
     * EditText field to enter the habit name
     */
    private EditText mHabitName;
    /**
     * EditText field to enter the frequency of this habit
     */
    private EditText mHabitFreq;

    /**
     * variables used to hold values for habits
     */
    String habit = "";
    int freqHabit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new HabitDbHelper(this);
        mHabitName = (EditText) findViewById(R.id.EditText_habit);
        mHabitFreq = (EditText) findViewById(R.id.EditText_freq);
        displayDatabaseInfo();
        final Button next = (Button) findViewById(R.id.next);
        final Button add = (Button) findViewById(R.id.add);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habit = mHabitName.getText().toString();
                mHabitName.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                mHabitFreq.setVisibility(View.VISIBLE);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freqHabit = Integer.parseInt(mHabitFreq.getText().toString());
                insertHabit();
                displayDatabaseInfo();
                mHabitName.setText("");
                mHabitFreq.setText("");
                add.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                mHabitName.setVisibility(View.VISIBLE);
                mHabitFreq.setVisibility(View.GONE);
            }
        });
    }

    public Cursor readHabits() {
        //Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_FREQ_HABIT
        };
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of the Habit database.
     */
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = readHabits();
        try {
            //Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_habit);
            displayView.setText("Number of rows in habits database table: " + readHabits().getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " + HabitEntry.COLUMN_HABIT_NAME+" - "+HabitEntry.COLUMN_FREQ_HABIT);

            //Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int freqColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_FREQ_HABIT);

            //Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                //Use that index to extract the String or Int value of the word
                //at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabitName = cursor.getString(nameColumnIndex);
                int currentFreq = cursor.getInt(freqColumnIndex);
                //Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " + currentHabitName + " - " + currentFreq));
            }
        } finally {
            //Always close the cursor when you're done reading from it. This releases all its makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert
     */
    private void insertHabit() {
        HabitDbHelper mDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //String name = mHabitName.getText().toString().trim();
        //int freq = Integer.parseInt(mHabitFreq.getText().toString().trim());
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, habit);
        values.put(HabitEntry.COLUMN_FREQ_HABIT, freqHabit);
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
        Toast.makeText(getApplicationContext(), "The id for this Habit is " + newRowId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
}
