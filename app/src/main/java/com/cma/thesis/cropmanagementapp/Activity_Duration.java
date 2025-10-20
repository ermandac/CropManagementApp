package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Duration extends AppCompatActivity {

    TextView duration;
    String cropID= "";
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration);

        duration = (TextView) findViewById(R.id.txtduration);
        cropID = getIntent().getStringExtra("passedID");

        //Toast.makeText(this, cropID, Toast.LENGTH_SHORT).show();

        String local = api.getCropFieldById(cropID, "duration");
        duration.setText(local == null ? "No data available" : local);
    }
}
