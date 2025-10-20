package com.cma.thesis.cropmanagementapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_CreatePlannerActivity extends AppCompatActivity {


    ListView lv;
    ArrayList<Class_Planner> planlist;
    AdapterPlanner adapterplan;
    String cropID;
    TextView txtpassedID;
    Button btnCreatePlan;

    RequestQueue requestQueue;

    Ipaddress address = new Ipaddress();
    String insertLink = address.ipaddress + "model/insert_plan.php";

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_planner);
        lv = (ListView) findViewById(R.id.listProcedure);
        planlist = new ArrayList<>();
        adapterplan = new AdapterPlanner(this, R.layout.activity_plan_item, planlist);

        btnCreatePlan = (Button) findViewById(R.id.btnCreatePlan);
        cropID = getIntent().getStringExtra("passedID");
        lv.setAdapter(adapterplan);

        btnCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartPlanDialog(Activity_CreatePlannerActivity.this);
            }
        });


        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(Activity_CreatePlannerActivity.this);

        loadPlans(cropID);

        lv.setOnItemClickListener(onListViewClick);


    }

    private AdapterView.OnItemClickListener onListViewClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {
            Class_Planner plannerset = planlist.get(position);

            Intent intent = new Intent(getApplicationContext(),Activity_Crop_PlanDetails.class);
            intent.putExtra("planid",String.valueOf(plannerset.getCropid()));
            intent.putExtra("startdate",String.valueOf(plannerset.getStartdate()));
            startActivity(intent);
        }
    };

    private void loadPlans(String cropid) {
        planlist.clear();

        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress +"model/plannerlist_api.php?id="+cropid;

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
                                String cropid = plan.getString("crop_id");
                                String startdate = plan.getString("start_date");
                                planlist.add(new Class_Planner(id, cropid, startdate));
                            }
                            adapterplan.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Activity_CreatePlannerActivity.this, "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showStartPlanDialog(Activity activity) {
        final String[] dates = {""};
        final AlertDialog.Builder dialogCreatePlan = new AlertDialog.Builder(Activity_CreatePlannerActivity.this);
        dialogCreatePlan.setTitle("Start Plan");
        dialogCreatePlan.setMessage("Are you sure you want to start this plan?");

        //set positive button
        dialogCreatePlan.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog dialogdate = new DatePickerDialog(
                            Activity_CreatePlannerActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year, month, day);
                    dialogdate.getDatePicker().clearFocus();
                    dialogdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogdate.show();

                    mDateSetListener = new DatePickerDialog.OnDateSetListener() {


                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                            month = month + 1;
                            Log.d("TAG", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                            dates[0] = month + "/" + day + "/" + year;


                            // Creating string request with post method.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, insertLink,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String ServerResponse) {
                                            // Showing response message coming from server.
                                            //Toast.makeText(Activity_CreatePlannerActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                            recreate();
                                            SetNotification();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {


                                            //Toast.makeText(Activity_CreatePlannerActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    // Creating Map String Params.
                                    Map<String, String> params = new HashMap<String, String>();

                                    // Adding All values to Params.
                                    params.put("cropid", cropID);
                                    params.put("date", dates[0]);
                                    return params;
                                }

                            };

                            // Creating RequestQueue.
                            RequestQueue requestQueue = Volley.newRequestQueue(Activity_CreatePlannerActivity.this);

                            // Adding the StringRequest object into requestQueue.
                            requestQueue.add(stringRequest);
                            loadPlans(cropID);

                            //call notif for created plan


                        }
                    };

                    lv.setEnabled(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //set negative button
        dialogCreatePlan.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogCreatePlan.show();
    }

    public void SetNotification()
    {
        NotificationCompat.Builder notificationbuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Sucess")
                .setContentText("You have successfully created a plan");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationbuilder.build());
    }
}