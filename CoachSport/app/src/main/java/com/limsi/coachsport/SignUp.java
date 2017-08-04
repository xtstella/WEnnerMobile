package com.limsi.coachsport;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private SQLiteDatabase db;
    private int count = 0;

    private EditText email;
    private EditText name;
    private TextView gender;
    private RadioGroup genderGroup;
    private RadioButton maleButton,femaleButton;
    private EditText age;
    private EditText height;
    private EditText weight;
    private EditText duration;
    private EditText hobbies;
    private EditText objective;
    private Button save;

    private final String TABLE_NAME = "USERS";
    private final String COLUMN_ID = "INDEX";
    private final String COLUMN_NAME = "USER_NAME";
    private final String COLUMN_EMAIL_ID = "USER_MAIL";
    private final String COLUMN_GENDER = "Gender";
    private final String COLUMN_AGE = "AGE";
    private final String COLUMN_HEIGHT = "HEIGHT";
    private final String COLUMN_WEIGHT = "WEIGHT";
    private final String COLUMN_DURATION = "DURATION";
    private final String COLUMN_HOBBIES = "HOBBIES";
    private final String COLUMN_OBJECTIVE = "OBJECTIVE";

    private String query =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + "INTEGER_PRIMARY_KEY_AUTOINCREMENT,"
                    + COLUMN_EMAIL_ID + "TEXT,"
                    + COLUMN_NAME + "TEXT,"
                    + COLUMN_GENDER + "TEXT,"
                    + COLUMN_AGE + "TEXT,"
                    + COLUMN_HEIGHT + "TEXT,"
                    + COLUMN_WEIGHT + "TEXT,"
                    + COLUMN_DURATION + "TEXT,"
                    + COLUMN_HOBBIES + "TEXT,"
                    + COLUMN_OBJECTIVE + "TEXT);";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.UserName);
        email = (EditText) findViewById(R.id.UserEmail);
        gender = (TextView) findViewById(R.id.gender);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        maleButton = (RadioButton) findViewById( R.id.maleButton);
        femaleButton = (RadioButton) findViewById(R.id.femaleButton);
        age = (EditText) findViewById(R.id.age);
        height = (EditText) findViewById( R.id.height);
        weight = (EditText) findViewById( R.id.weight);
        duration = (EditText) findViewById( R.id.duration);
        hobbies = (EditText) findViewById( R.id.hobbies);
        objective = (EditText) findViewById(R.id.objective);
        save = (Button) findViewById( R.id.save);

        db = openOrCreateDatabase("UserInformation.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.execSQL(query);
        query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            count++;
            c.moveToNext();
        }
    }

    public void saveInDB(View v) {

        String iGender = "";
        String iName = name.getText().toString();
        String iEmail = email.getText().toString();
        String iAge = age.getText().toString();
        String iHeight = height.getText().toString();
        String iWeight = weight.getText().toString();
        String iDuration = duration.getText().toString();
        String iHobbies = hobbies.getText().toString();
        String iObjective = objective.getText().toString();

        if( maleButton.isChecked()) {
            iGender = "Male";
        } else if ( femaleButton.isChecked() ) {
            iGender = "Female";
        }

        if(
                iName.equalsIgnoreCase("") ||
                        iEmail.equalsIgnoreCase("") ||
                        iAge.equalsIgnoreCase("") ||
                        iGender.equalsIgnoreCase("") ||
                        iHeight.equalsIgnoreCase("") ||
                        iWeight.equalsIgnoreCase("") ||
                        iDuration.equalsIgnoreCase("") ||
                        iHobbies.equalsIgnoreCase("") ||
                        iObjective.equalsIgnoreCase("")
                ) {
            Toast.makeText(getApplicationContext(), "Please fill in all the fields !!", Toast.LENGTH_LONG).show();
            return;
        }

        String query = "INSERT INTO " + TABLE_NAME + " VALUES("
                + count++  + ", '"
                + iEmail + "' , '"
                + iName + "' , '"
                + iGender + "' , '"
                + iAge + "' , '"
                + iHeight + "' , '"
                + iWeight + "' , '"
                + iDuration + "' , '"
                + iHobbies + "' , '"
                + iObjective + "')";

        db.execSQL(query);

        Toast.makeText(getApplicationContext(),"Saved in DB !!",Toast.LENGTH_LONG).show();
    }



}

