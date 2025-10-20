package com.cma.thesis.cropmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class sample_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_details);

        String stringI = getIntent().getStringExtra("cropid");

        androidx.appcompat.widget.AppCompatTextView  etsample = (androidx.appcompat.widget.AppCompatTextView ) findViewById(R.id.txtsample);


        etsample.setText(stringI);


    }
}
