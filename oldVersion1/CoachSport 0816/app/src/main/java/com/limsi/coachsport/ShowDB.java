package com.limsi.coachsport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ShowDB extends AppCompatActivity {

    private String[] UserDetails;
    private int[] UserPics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        UserDetails = getIntent().getExtras().getStringArray("All Details");
        UserPics = getIntent().getExtras().getIntArray("All Pics");

        ListView myListView = (ListView) findViewById( R.id.myListView);
        ListAdapter adapter = new CustomAdapter(this,UserDetails,UserPics);

        myListView.setAdapter(adapter);
    }
}
