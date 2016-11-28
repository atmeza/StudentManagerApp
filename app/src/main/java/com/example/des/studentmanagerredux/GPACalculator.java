package com.example.des.studentmanagerredux;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import com.example.des.studentmanagerredux.db.GPADbHelper;


public class GPACalculator extends AppCompatActivity {

    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private List<EditText> editTextListCourse = new ArrayList<EditText>();
    private List<EditText> editTextListUnits = new ArrayList<EditText>();
    private List<Spinner> spinnerList = new ArrayList<Spinner>();

    private GPADbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpa_calculator);

    }

    @Override
    protected void onStart() {

        super.onStart();

        mHelper = new GPADbHelper(this);

        Cursor cursor = mHelper.getAllClasses();

        // parse through database, for each row, create a GPACalculator row that has its parameters
        // defaulted to the values in the row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(1);
            String units = cursor.getString(2);
            int grade = cursor.getInt(3);
            addRow(title, units, grade); // new addRow method that sets initial state of row to params
            cursor.moveToNext();
        }

        // if there are no events in the database, then make an empty one
        if (editTextListUnits.size() == 0) {
            addRow();
        }

    }

    // onStop method will overwrite existing SQL database with whatever is in the GPACalculator
    // so that the database is updated before the information on the page is lost
    @Override
    protected void onStop() {

        super.onStop();

        // empty current database
        mHelper = new GPADbHelper(this);
        mHelper.removeAllClasses();

        // parse through all of the rows of the GPACalculator, creating a database entry for
        // each row
        for (int i = 0; i < spinnerList.size(); i++) {
            String title = editTextListCourse.get(i).getText().toString();
            String units = editTextListUnits.get(i).getText().toString();
            int grade = spinnerList.get(i).getSelectedItemPosition();
            mHelper.addClass(title, units, grade);
        }

        mHelper.firebaseOverwrite();

    }

    // new addRow method sets values od new row to params passed in
    private void addRow(String title, String units, int grade){
        TableLayout tableLayout  = (TableLayout)findViewById(R.id.mainTable);
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);

        // new methods accept a value and set default value to it
        tableRow.addView(editTextCourseWithValue("0", title));
        tableRow.addView(editTextUnitsWithValue("0", units));
        tableRow.addView(spinnerGradeWithValue(grade));
        tableLayout.addView(tableRow);
    }

    // makes a new course with a default value
    private EditText editTextCourseWithValue(String hint, String title) {
        EditText editText = new EditText(this);
        editText.setId(Integer.valueOf(hint));
        editText.setText(title);
        //editText.setHint(hint);
        editTextListCourse.add(editText);
        return editText;
    }

    // makes a new unit box with a default value
    private EditText editTextUnitsWithValue(String hint, String units) {
        EditText editText = new EditText(this);
        editText.setId(Integer.valueOf(hint));
        editText.setText(units);
        //editText.setHint(hint);
        editTextListUnits.add(editText);
        return editText;
    }

    // makes a new grade spinner with a default position
    private Spinner spinnerGradeWithValue(int grade) {
        Spinner spinner = new Spinner(this);
        //Spinner.setId(Integer.valueOf(hint));
        //editText.setHint(hint);
        String[] items = new String[]{"Select", "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(grade);
        spinnerList.add(spinner);

        return spinner;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    private EditText editTextCourse(String hint) {
      EditText editText = new EditText(this);
      editText.setId(Integer.valueOf(hint));
      //editText.setHint(hint);
      editTextListCourse.add(editText);
      return editText;
    }



    private EditText editTextUnits(String hint) {
        EditText editText = new EditText(this);
        editText.setId(Integer.valueOf(hint));
        //editText.setHint(hint);
        editTextListUnits.add(editText);
        return editText;
    }



    private Spinner spinnerGrade() {
        Spinner spinner = new Spinner(this);
        //Spinner.setId(Integer.valueOf(hint));
        //editText.setHint(hint);
        String[] items = new String[]{"Select", "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinnerList.add(spinner);

        return spinner;
    }

    private void addRow(){
        TableLayout tableLayout  = (TableLayout)findViewById(R.id.mainTable);
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        tableRow.addView(editTextCourse("0"));
        tableRow.addView(editTextUnits("0"));
        tableRow.addView(spinnerGrade());
        tableLayout.addView(tableRow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                addRow();

                /*String task = "ABCDE";
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                db.insertWithOnConflict(TaskContract.TaskEntry.GPATABLECOURSE,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();*/


                /*final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();*/
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendMessageCalculateGPA(View view)
    {
        double values[] = {-1, 4.0, 4.0, 3.7, 3.3, 3.0, 2.7, 2.3, 2.0, 1.7, 1.0, 0};
        int totalCredits = 0;
        double totalPoints = 0;

        for(int i = 0; i < spinnerList.size(); i++){
            Spinner dropdown = spinnerList.get(i);
            int grade = dropdown.getSelectedItemPosition();
            double points = values[grade];
            EditText eTunits = editTextListUnits.get(i);
            String unitString = eTunits.getText().toString();
            double credits=0;
            try{
                credits = Double.parseDouble(unitString);
            }
            catch(NumberFormatException e) {}


            if ((points >= 0) && (credits > 0)) {
               totalCredits += credits;
               totalPoints += points * credits;
            }
        }

        double GPA = 0;
        if(totalCredits > 0){
            GPA = totalPoints / totalCredits;
        }

        String s;
        if(GPA == 0){
                s = "Enter data!";
        }
        else{
            int g=(int)(GPA*100.0);
            GPA=(double)g;
            GPA/=100;
            s = "Your GPA is: " + Double.toString(GPA);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("GPA")
                .setMessage(s)
                .setCancelable(true)
                .create();
        dialog.show();
    }

}
