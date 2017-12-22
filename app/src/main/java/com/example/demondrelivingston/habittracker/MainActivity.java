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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new HabitDbHelper(this);
        mHabitName = (EditText) findViewById(R.id.EditText_habit);
        displayDatabaseInfo();

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPet();
                displayDatabaseInfo();
                mHabitName.setText("");
            }
        });
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of the Habit database.
     */
    private void displayDatabaseInfo() {

        //Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME
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
        try {
            //Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_habit);
            displayView.setText("Number of rows in habits database table: " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " + HabitEntry.COLUMN_HABIT_NAME);

            //Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);

            //Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                //Use that index to extract the String or Int value of the word
                //at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabitName = cursor.getString(nameColumnIndex);
                //Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " + currentHabitName));
            }
        } finally {
            //Always close the cursor when you're done reading from it. This releases all its makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert
     */
    private void insertPet() {
        HabitDbHelper mDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String name = mHabitName.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, name);
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
        Toast.makeText(getApplicationContext(), "The id for this Habit is " + newRowId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
}
