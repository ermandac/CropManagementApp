package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Activity_CropIntro extends AppCompatActivity
{
    TextView txtIntro;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_intro);

        txtIntro = (TextView) findViewById(R.id.txtIntroducation);
        String cropID = getIntent().getStringExtra("passedID");
        Ipaddress address = new Ipaddress();
        String cropLink = address.ipaddress + "model/api_description.php?field=description&id='"+cropID+"'";
        api.loadCrops(cropLink,cropID,txtIntro,this);
    }
}