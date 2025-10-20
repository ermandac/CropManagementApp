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

public class activity_topcrop extends AppCompatActivity {

    ListView lv;
    ArrayList<Class_Suggested> suggestedList;
    Adapter_Suggested adapterSuggested;
    String cropID;
    TextView txtcropsuggested;
    ImageView imgCrop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topcrop);

        //Toast.makeText(this, "Welcome to topcrop", Toast.LENGTH_SHORT).show();
        lv = (ListView) findViewById(R.id.listSuggested);
        suggestedList = new ArrayList<>();
        adapterSuggested = new Adapter_Suggested(this, R.layout.list_suggested_items_details, suggestedList);
        lv.setAdapter(adapterSuggested);
        //generateTopCrop();

        lv.setOnItemClickListener(onListViewClick);




        String[] arraySpinner = new String[] {
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October","November", "December"
        };
        Spinner s = (Spinner) findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(activity_topcrop.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                generateTopCrop(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private AdapterView.OnItemClickListener onListViewClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {

            final Class_Suggested suggested = suggestedList.get(position);
            //Toast.makeText(activity_topcrop.this, String.valueOf(suggested.getId()), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),Activity_CropOptionsList.class);
            intent.putExtra("cropid",String.valueOf(suggested.getId()));
            startActivity(intent);
        }
    };


    private void generateTopCrop(String month) {
        suggestedList.clear();



        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/suggested_api.php?month="+month;

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
                                JSONObject plan = array.getJSONObject(i);

                                int id = plan.getInt("id");
                                String crop_name = plan.getString("crop_name");
                                String image = plan.getString("image");
                                String month = plan.getString("month");

                                suggestedList.add(new Class_Suggested(id, crop_name, image, month));

                            }
                            adapterSuggested.notifyDataSetChanged();
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
