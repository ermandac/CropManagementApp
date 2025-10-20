package com.cma.thesis.cropmanagementapp;

        import android.database.Cursor;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;

public class Activity_Irrigation extends AppCompatActivity {

    TextView irrigation;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irrigation);

        irrigation = (TextView) findViewById(R.id.txtirrigation);
        String cropID = getIntent().getStringExtra("passedID");


        String local = api.getCropFieldById(cropID, "irrigation");
        irrigation.setText(local == null ? "No data available" : local);
    }
}
