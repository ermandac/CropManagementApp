package com.cma.thesis.cropmanagementapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class Activity_CropList extends AppCompatActivity
{

    ListView listView;
    ArrayList<Class_Crops> croplist;
    Adapter_CropList croplistadapter;
    String cat_id="";
    String subcat = "";
    FirestoreCropHelper firestoreHelper;
    private boolean isLoading = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croplist);

        listView = findViewById(R.id.cropListView);
        croplist = new ArrayList<>();
        croplistadapter = new Adapter_CropList(this,R.layout.list_crop_card_item,croplist);

        listView.setAdapter(croplistadapter);

        cat_id = getIntent().getStringExtra("category");
        subcat = getIntent().getStringExtra("sub");

        //Toast.makeText(this, "scat"+cat_id, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ss"+subcat, Toast.LENGTH_SHORT).show();

        // Initialize Firestore helper
        firestoreHelper = new FirestoreCropHelper();

        // Load from Firestore
        loadCrops();

        // action when clicking specific item on listview
        listView.setOnItemClickListener(onListViewClick);

    }

    private AdapterView.OnItemClickListener onListViewClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {
            Class_Crops cropset = croplist.get(position);

            Intent intent = new Intent(getApplicationContext(),Activity_CropOptionsList.class);
            intent.putExtra("cropid",String.valueOf(cropset.getId()));
            startActivity(intent);
        }
    };

    private void loadCrops() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        croplist.clear();
        
        // Load crops from Firestore
        firestoreHelper.loadCropsByCategory(cat_id, new FirestoreCropHelper.CropLoadCallback() {
            @Override
            public void onSuccess(ArrayList<Class_Crops> crops) {
                croplist.clear();
                croplist.addAll(crops);
                croplistadapter.notifyDataSetChanged();
                Toast.makeText(Activity_CropList.this, "Loaded " + croplist.size() + " crops from Firestore", Toast.LENGTH_SHORT).show();
                if (croplist.isEmpty()) {
                    Toast.makeText(Activity_CropList.this, "No crops found for this category in Firestore", Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
            }

            @Override
            public void onError(String error) {
                Toast.makeText(Activity_CropList.this, "Firestore error: " + error, Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }
}
