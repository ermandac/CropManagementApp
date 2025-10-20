package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Arrays;



public class Activity_CropOptionsList extends AppCompatActivity {

    String[] items = {"Planner","Introduction","Scientific Name","Crop Duration","Crop Varieties","Planting Season","Planting Materials","Weed Control","Irrigation","Harvesting","Comment"};

    ArrayList<String> listItems;

    Adapter_CropOptions customAdapter;

    ListView listView;

    EditText editText;
    EditText passedID;
    EditText introduction;

    String cropid = "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.crop_options_list_layout);

        listView=(ListView)findViewById(R.id.listview);

        editText=(EditText)findViewById(R.id.txtsearch);
        passedID=(EditText)findViewById(R.id.txtcropIDpassed);
        introduction = (EditText)findViewById(R.id.txtcropIntroduction);

        //pass cropid here
        cropid = getIntent().getStringExtra("cropid");
        //Toast.makeText(this, "c:" + cropid, Toast.LENGTH_SHORT).show();

        initList();

        editText.addTextChangedListener(new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {

            }
            @Override

            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if(s.toString().equals("")){

                    // reset listview

                    initList();

                }

                else{

                    // perform search

                    searchItem(s.toString());

                }

            }

            @Override

            public void afterTextChanged(Editable s) {

            }

        });

    }

    public void searchItem(String textToSearch){
        // Search functionality - can be implemented later if needed
        // For now, just reinitialize the list
        initList();
    }

    public void initList(){

        customAdapter = new Adapter_CropOptions(this, items, cropid);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(onListViewClick);

    }

    private AdapterView.OnItemClickListener onListViewClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {

            Intent intent;

            switch (position)
            {
                //planner
                case 0:
                    intent = new Intent(getApplicationContext(),Activity_CreatePlannerActivity.class);
                    intent.putExtra("passedID",cropid);
                    startActivity(intent);
                    break;
                //crop details
                case 1:
                    intent = new Intent(getApplicationContext(),Activity_CropIntro.class);
                    intent.putExtra("passedID",cropid);
                   // intent.putExtra("details",)
                    startActivity(intent);
                    break;
                //science name
                case 2:
                    intent = new Intent(getApplicationContext(),Activity_ScientificName.class);
                    intent.putExtra("passedID",cropid);
                    startActivity(intent);
                    break;

                //duration
                case 3:
                    intent = new Intent(getApplicationContext(),Activity_Duration.class);
                    intent.putExtra("passedID",cropid);
                    startActivity(intent);
                    break;
                //varieties - Toggle dropdown instead of opening new activity
                case 4:
                    customAdapter.toggleExpansion(position);
                    break;

                //season - Toggle dropdown instead of opening new activity
                case 5:
                    customAdapter.toggleExpansion(position);
                    break;
                    
                //materials - Toggle dropdown instead of opening new activity
                case 6:
                    customAdapter.toggleExpansion(position);
                    break;

                //weed control - Toggle dropdown instead of opening new activity
                case 7:
                    customAdapter.toggleExpansion(position);
                    break;
                //irrigation
                case 8:
                    intent = new Intent(getApplicationContext(),Activity_Irrigation.class);
                    intent.putExtra("passedID",cropid);
                    startActivity(intent);
                    break;

                //harvesting
                case 9:
                    intent = new Intent(getApplicationContext(),Activity_Harvesting.class);
                    intent.putExtra("passedID",cropid);
                    startActivity(intent);
                    break;
                //comment
                case 10:
                    intent = new Intent(getApplicationContext(),Activity_Comments.class);
                    intent.putExtra("passedID",cropid);
                    startActivity(intent);
                    break;
            }
        }
    };
    
}
