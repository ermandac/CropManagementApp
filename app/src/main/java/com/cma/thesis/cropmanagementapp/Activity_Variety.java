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

public class Activity_Variety extends AppCompatActivity {

    TextView variety;
    ListView lv;
    ArrayList<Class_Variety> varietylist;
    Adapter_Variety adaptervariety;

    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity__variety);

    lv = findViewById(R.id.listvariety);
    varietylist = new ArrayList<>();
    adaptervariety = new Adapter_Variety(this, R.layout.list_variety, varietylist);
    lv.setAdapter(adaptervariety);

    String cropID = getIntent().getStringExtra("passedID");
    populateVarietiesFromLocal(cropID);
}

private void populateVarietiesFromLocal(String cropId) {
    varietylist.clear();
    Class_DatabaseHelper db = new Class_DatabaseHelper(this);
    String varieties = db.getCropFieldById(cropId, "varieties");
    if (varieties != null && !varieties.trim().isEmpty()) {
        // Split by newlines or commas
        for (String v : varieties.split("\\r?\\n|,")) {
            String name = v.trim();
            if (!name.isEmpty()) {
                varietylist.add(new Class_Variety(0, name));
            }
        }
    }
    adaptervariety.notifyDataSetChanged();
}

    /*
    private void generateVarityCrop(int cropid) {
        varietylist.clear();

        //Ipaddress address = new Ipaddress();
        //String planList = address.ipaddress + "model/crop_variety_api.php?id="+cropid;

        
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
                                String varietyname = weeds.getString("variety");

                                varietylist.add(new Class_Variety(id, varietyname));
                            }
                            adaptervariety.notifyDataSetChanged();
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
    */
}
