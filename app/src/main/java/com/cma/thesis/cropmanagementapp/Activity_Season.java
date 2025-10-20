package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_Season extends AppCompatActivity {

    TextView season;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        season = (TextView) findViewById(R.id.txtseason);
        String cropID = getIntent().getStringExtra("passedID");

        String local = api.getCropFieldById(cropID, "season");
        season.setText(local == null ? "No data available" : local);
    }
}
