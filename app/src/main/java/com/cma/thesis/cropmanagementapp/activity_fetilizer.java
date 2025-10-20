package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

public class activity_fetilizer extends AppCompatActivity {

    ListView lv;
    ArrayList<Class_Fertilizer> fertilizerlist;
    Adapter_Fertilizer adapterfertilizer;
    String cropID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetilizer);


        lv = (ListView) findViewById(R.id.listfertilizer);
        fertilizerlist = new ArrayList<>();
        adapterfertilizer = new Adapter_Fertilizer(this, R.layout.list_fertilizer_item_details, fertilizerlist);
        lv.setAdapter(adapterfertilizer);

        String[] arraySpinner = new String[] {
                "Fruits", "Medicinal", "Plantation", "Pulses", "Spices", "Vegetables", "Organic"
        };
        Spinner s = (Spinner) findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(activity_fetilizer.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                generateTopCrop(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void generateTopCrop(String category) {
        fertilizerlist.clear();



        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/fertilizer_api.php?category="+category;

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
                                JSONObject fertilize = array.getJSONObject(i);

                                String chemical = fertilize.getString("chemical");
                                String category = fertilize.getString("category");

                                fertilizerlist.add(new Class_Fertilizer(category,chemical));

                            }
                            adapterfertilizer.notifyDataSetChanged();
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
