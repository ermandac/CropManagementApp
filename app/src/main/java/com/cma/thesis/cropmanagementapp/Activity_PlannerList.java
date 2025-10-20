package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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


public class Activity_PlannerList extends AppCompatActivity
{
    ListView lv;
    ArrayList<Class_Planner> plannerList;
    AdapterPlanner plannerlistadapter;
    public static Class_Functions function;
    String cropid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planlist);

        lv =(ListView) findViewById(R.id.cropGridViewPlanners);
        plannerList = new ArrayList<>();
        plannerlistadapter = new AdapterPlanner(this,R.layout.activity_plan_item,plannerList);

        lv.setAdapter(plannerlistadapter);

        cropid = getIntent().getStringExtra("passedID");

        loadPlans(cropid);

        // action when clicking specific item on gridview
        lv.setOnItemClickListener(onListViewClick);

    }

    private AdapterView.OnItemClickListener onListViewClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {
            Class_Planner planset = plannerList.get(position);

            Intent intent = new Intent(getApplicationContext(),Activity_Crop_PlanDetails.class);
            intent.putExtra("planid",String.valueOf(planset.getId()));
            startActivity(intent);
        }
    };

    private void loadPlans(String cropid) {
        plannerList.clear();
        Ipaddress address = new Ipaddress();

        String pestLink = address.ipaddress +"model/plannerlist_api.php?id="+cropid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, pestLink,
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
                                String cropid = plan.getString("crop_id");
                                String startdate = plan.getString("start_date");

                                plannerList.add(new Class_Planner(id,cropid,startdate));
                            }
                            plannerlistadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Activity_PlannerList.this, "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
