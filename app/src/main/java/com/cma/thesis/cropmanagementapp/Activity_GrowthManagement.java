package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_GrowthManagement extends AppCompatActivity {

    TextView growthmanagement;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_management);

        growthmanagement = (TextView) findViewById(R.id.txtgrowthManagement);
        String cropID = getIntent().getStringExtra("passedID");
        String local = api.getCropFieldById(cropID, "growth_management");
        growthmanagement.setText(local == null ? "No data available" : local);
    }
}
