package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_SoilClimate extends AppCompatActivity {

    TextView soilClimate;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_climate);

        soilClimate = (TextView) findViewById(R.id.txtsoilClimate);
        String cropID = getIntent().getStringExtra("passedID");

        String local = api.getCropFieldById(cropID, "soil_climate");
        soilClimate.setText(local == null ? "No data available" : local);
    }
}
