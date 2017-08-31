package com.limsi.coachsport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private RadioGroup userType;
    private RadioButton newUser;
    private RadioButton existingUser;
    private Button submit;

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userType = (RadioGroup) findViewById(R.id.UserType);
        newUser = (RadioButton) findViewById(R.id.newUser);
        existingUser = (RadioButton) findViewById(R.id.existingUser);
        submit = (Button) findViewById(R.id.submit);

        BottomNavigationView bottomNavigationView= (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//  Show always the menu title
//  get the private BottomNavigationMenuView field
        Field f = null;
        try {
            f = bottomNavigationView.getClass().getDeclaredField("mMenuView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        BottomNavigationMenuView menuView=null;
        try {
            menuView = (BottomNavigationMenuView) f.get(bottomNavigationView);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//  get the private BottomNavigationItemView[]  field
        try {
            f=menuView.getClass().getDeclaredField("mButtons");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        BottomNavigationItemView[] mButtons=null;
        try {
            mButtons = (BottomNavigationItemView[]) f.get(menuView);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        for(int i=0;i<mButtons.length;i++){
            mButtons[i].setShiftingMode(false);
            mButtons[i].setChecked(true);
        }
//  Show always the menu title

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_profile:
                    Intent profile = new Intent(MainActivity.this, HeartRate.class);
                    startActivity(profile);
                    break;
                case R.id.navigation_history:
                    Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(historyActivity);
                    break;
                case R.id.navigation_questionary:
                    Intent questionaryActivity = new Intent(MainActivity.this, QuestionaryActivity.class);
                    startActivity(questionaryActivity);
                    break;
            }
            return false;
        }

    };

    public void submitClicked(View v) {

        if (!newUser.isChecked() && !existingUser.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please Select an Option!!", Toast.LENGTH_LONG).show();
        }

        if (newUser.isChecked()) {

            Intent signUp = new Intent(MainActivity.this, SignUp.class);
            startActivity(signUp);
        }

        if (existingUser.isChecked()) {
            Intent signIn = new Intent(MainActivity.this, LogIn.class);
            startActivity(signIn);
        }
    }
}

