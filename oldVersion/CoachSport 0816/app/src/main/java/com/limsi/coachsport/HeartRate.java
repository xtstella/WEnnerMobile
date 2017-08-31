package com.limsi.coachsport;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class HeartRate extends AppCompatActivity {

    TextView date1;
    private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.listView);
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_history_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);

        InputStream inputStream = getResources().openRawResource(R.raw.heartrate);
        CSVFile csvFile = new CSVFile(inputStream);
        List scoreList = csvFile.read();

        for(Object scoreData : scoreList ) {
            itemArrayAdapter.add((String[]) scoreData);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home = new Intent(HeartRate.this, MainActivity.class);
                    startActivity(home);
                case R.id.navigation_profile:
                    return true;
                case R.id.navigation_history:
                    Intent questionaryActivity = new Intent(HeartRate.this, QuestionaryActivity.class);
                    startActivity(questionaryActivity);
                    break;
            }
            return false;
        }

    };
}