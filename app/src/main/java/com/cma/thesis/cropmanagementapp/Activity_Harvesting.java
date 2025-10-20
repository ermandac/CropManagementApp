package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_Harvesting extends AppCompatActivity {

    TextView harvesting;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvesting);

        harvesting = (TextView) findViewById(R.id.txtharvesting);
        String cropID = getIntent().getStringExtra("passedID");

        String local = api.getCropFieldById(cropID, "harvesting");
        harvesting.setText(local == null ? "No data available" : local);
    }
}
