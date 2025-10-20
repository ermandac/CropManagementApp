package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_MainField extends AppCompatActivity {

    TextView mainfield;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_field);

        mainfield = (TextView) findViewById(R.id.txtmainfield);
        String cropID = getIntent().getStringExtra("passedID");
        String local = api.getCropFieldById(cropID, "main_field");
        mainfield.setText(local == null ? "No data available" : local);

    }
}
