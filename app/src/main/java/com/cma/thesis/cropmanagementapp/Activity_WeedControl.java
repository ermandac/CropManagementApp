package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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

public class Activity_WeedControl extends AppCompatActivity {
    TextView weeds;
    ListView lv;
    ArrayList<Class_Weeds> weedslist;
    Adapter_WeedControl adapterweedcontrol;

    Class_DatabaseHelper api = new Class_DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weed_control);

        //Toast.makeText(this, "Welcome to weed control", Toast.LENGTH_SHORT).show();
        lv = (ListView) findViewById(R.id.listweedcontrol);
        weedslist = new ArrayList<>();
        adapterweedcontrol = new Adapter_WeedControl(this, R.layout.list_weed_control, weedslist);
        lv.setAdapter(adapterweedcontrol);
        String cropID = getIntent().getStringExtra("passedID");
        populateWeedsFromLocal(cropID);

    }

    private void populateWeedsFromLocal(String cropId) {
        weedslist.clear();
        Class_DatabaseHelper db = new Class_DatabaseHelper(this);
        String weeds = db.getCropFieldById(cropId, "weed_control");
        if (weeds != null && !weeds.trim().isEmpty()) {
            // Split by newlines or commas
            for (String v : weeds.split("\\r?\\n|,")) {
                String name = v.trim();
                if (!name.isEmpty()) {
                    weedslist.add(new Class_Weeds(0, name));
                }
            }
        }
        adapterweedcontrol.notifyDataSetChanged();
    }

    /*private void generateTopCrop(int cropid) {
        weedslist.clear();

        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/crop_weed_api.php?id="+cropid;

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
                                JSONObject weeds = array.getJSONObject(i);

                                int id = weeds.getInt("id");
                                String weedname = weeds.getString("weed_control");

                                weedslist.add(new Class_Weeds(id, weedname));
                            }
                            adapterweedcontrol.notifyDataSetChanged();
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
    }*/
}
