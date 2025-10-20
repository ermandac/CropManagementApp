package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_ScientificName extends AppCompatActivity {

    TextView science_name;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scientific_name);

        science_name = (TextView) findViewById(R.id.txtsciencename);
        String cropID = getIntent().getStringExtra("passedID");

        String local = api.getCropFieldById(cropID, "science_name");
        science_name.setText(local == null ? "No data available" : local);
    }
}
