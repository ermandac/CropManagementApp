package com.cma.thesis.cropmanagementapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.view.View;

public class sub_category extends AppCompatActivity {

    static String catid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        catid = getIntent().getStringExtra("category");
    }

    public void onClickTree(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category",catid);
        intent.putExtra("sub","tree");
        startActivity(intent);
    }
    public void onClickPlant(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category",catid);
        intent.putExtra("sub","plants");
        startActivity(intent);
    }
}
