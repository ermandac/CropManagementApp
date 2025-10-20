package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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

import java.util.ArrayList;

public class Activity_Materials extends AppCompatActivity {

    ListView lv;
    ArrayList<Class_CropMaterials> cropMaterialList;
    Adapter_CropMaterials adaptercropmaterials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);

        String cropID = getIntent().getStringExtra("passedID");

        //Toast.makeText(this, "Welcome to mat control", Toast.LENGTH_SHORT).show();
        lv = (ListView) findViewById(R.id.listcropmaterial);
        cropMaterialList = new ArrayList<>();
        adaptercropmaterials = new Adapter_CropMaterials(this, R.layout.list_cropmaterial, cropMaterialList);
        lv.setAdapter(adaptercropmaterials);
        generateMatCrop(Integer.parseInt(cropID));
    }

    private void generateMatCrop(int cropid) {
        cropMaterialList.clear();

        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/crop_materials_api.php?id="+cropid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, planList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject cropmat = array.getJSONObject(i);

                                int id = cropmat.getInt("id");
                                String cropmaterial = cropmat.getString("materials");

                                cropMaterialList.add(new Class_CropMaterials(id, cropmaterial));
                            }
                            adaptercropmaterials.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
