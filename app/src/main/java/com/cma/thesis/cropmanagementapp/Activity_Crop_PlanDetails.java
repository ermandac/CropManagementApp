package com.cma.thesis.cropmanagementapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cma.thesis.cropmanagementapp.Adapter_Procedure.counter;
import static com.cma.thesis.cropmanagementapp.Adapter_Procedure.donecounter;

/**
 * Created by Mimoy on 10/14/2018.
 */

public class Activity_Crop_PlanDetails extends AppCompatActivity {

    ListView lv;
    ArrayList<Class_Procedure> procedureList;
    Adapter_Procedure adapterProcedure;
    String cropID;
    String startdate;
    TextView txtpassedID;
    Button btnCreatePlan;
    PendingIntent broadcast;
    String title = "";
    public static String passString = "";
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_planner_details);
        lv = (ListView) findViewById(R.id.listProcedure);
        procedureList = new ArrayList<>();
        adapterProcedure = new Adapter_Procedure(this, R.layout.list_procedure_item_details, procedureList);
        cropID = getIntent().getStringExtra("planid");
        startdate = getIntent().getStringExtra("startdate");
        lv.setAdapter(adapterProcedure);
        generateProcedureList(cropID);

        progressbar = (ProgressBar)findViewById(R.id.planprogressbar);

        Intent notificationIntent = new Intent(this, AlarmReciever.class);



        int counterP = 0;
        int itemsCount = lv.getChildCount();
        for (int i = 0; i < itemsCount; i++) {
            View view = lv.getChildAt(i);
            String procedure = ((Button) view.findViewById(R.id.btnsetAlarm)).getText().toString();

            if(procedure.equalsIgnoreCase("done"))
            {
                counterP++;
            }
        }

        if(counter!=0 && getTotalDone(cropID)!=0)
        {
            progressbar.setProgress(getTotalDone(cropID)/counter * 100);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //setContentView(R.layout.activity_planner_details);
    }
    Intent intent = new Intent();
    private void generateProcedureList(String cropID) {
        intent.putExtra("titless",title);
        procedureList.clear();

        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/plan_details_list_api.php?id="+cropID;

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
                                int cropid = plan.getInt("crop_id");
                                int days_notif = plan.getInt("notif_days");
                                int step = plan.getInt("stepno");
                                String procedure = plan.getString("step_procedure");
                                String status = plan.getString("status");
                                passString = procedure;
                                title = procedure;

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar c = Calendar.getInstance();
                                try {
                                    c.setTime(dateFormat.parse(startdate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                c.add(Calendar.DAY_OF_YEAR,days_notif);
                                dateFormat=new SimpleDateFormat("MM-dd-yyyy");
                                Date newDate=new Date(c.getTimeInMillis());
                                String resultDate=dateFormat.format(newDate);

                                String datenotifaction = resultDate;

                                procedureList.add(new Class_Procedure(id, cropid, step, days_notif, procedure,datenotifaction,status));





                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, 24);
                                cal.set(Calendar.MINUTE,00);
                                cal.set(Calendar.SECOND, 0);
                                if (cal.getTime().compareTo(new Date()) < 0) cal.add(Calendar.DAY_OF_MONTH, 1);
                                Intent intent = new Intent(getApplicationContext(),AlarmReciever.class);
                                PendingIntent pendingintent =  PendingIntent.getBroadcast(getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingintent);


                            }
                            adapterProcedure.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Activity_Crop_PlanDetails.this, "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void createNotification()
    {
        AlarmManager alarms = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Receiver receiver = new Receiver();
        IntentFilter filter = new IntentFilter("ALARM_ACTION");
        registerReceiver(receiver, filter);

        Intent intent = new Intent("ALARM_ACTION");
        intent.putExtra("param", "My scheduled action");
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
        // I choose 3s after the launch of my application
        alarms.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+3000, operation) ;
    }

    public void SetNotification(String reminder) {
        NotificationCompat.Builder notificationbuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(reminder)
                .setContentText("Notification");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationbuilder.build());
    }


        public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //Toast.makeText(context, intent.getStringExtra("param"),Toast.LENGTH_SHORT).show();
        }

    }

    public void onCLickPlanProcedure(View v)
    {
        Ipaddress address = new Ipaddress();
        String insertLink = address.ipaddress + "model/update_procedure.php";
        int position = lv.getPositionForView(v);


        //get the row the clicked button is in
        RelativeLayout vwParentRow = (RelativeLayout) v.getParent();

        TextView child = (TextView)vwParentRow.getChildAt(0);
        TextView procedureid = (TextView)vwParentRow.getChildAt(2);
        TextView cropid = (TextView) vwParentRow.getChildAt(3);
        Button btnChild = (Button)vwParentRow.getChildAt(4);
        //btnChild.setText(child.getText());
        btnChild.setText("DONE");

        StartAnimation(vwParentRow);


        //Toast.makeText(this, "" + child.getText(), Toast.LENGTH_SHORT).show();
        int c = Color.GREEN;
        //vwParentRow.setBackgroundColor(c);
        updateProcedureStatus(insertLink,procedureid.getText().toString(),cropid.getText().toString());


        int counterP = 0;
        int itemsCount = lv.getChildCount();
        for (int i = 0; i < itemsCount; i++) {
            View view = lv.getChildAt(i);
            String procedure = ((Button) view.findViewById(R.id.btnsetAlarm)).getText().toString();

            if(procedure.equalsIgnoreCase("done"))
            {
               counterP++;
            }
        }



        int formula = (counterP/itemsCount) * 100;
        Log.d( "formulasss", String.valueOf(formula));

        ///Toast.makeText(this, "co" + formula, Toast.LENGTH_SHORT).show();
        progressbar.setProgress(formula);

    }


    public void updateProcedureStatus(String insertLink,final String id,final String cropid)
    {
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(context, volleyError.networkResponse.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("cropid",cropid);
                params.put("id", id);
                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Crop_PlanDetails.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);



    }


    public void StartAnimation(View v)
    {
        int colorstart = Color.GRAY;
        int colorEnd = Color.CYAN;

        ValueAnimator coloranimation = ObjectAnimator.ofInt(v,"backgroundColor",colorstart,colorEnd);

        coloranimation.setDuration(1000);
        coloranimation.setEvaluator(new ArgbEvaluator());
        coloranimation.setRepeatCount(1);
        coloranimation.setRepeatMode(ValueAnimator.RESTART);
        coloranimation.start();
    }

    static int totaldone = 0;
    private int getTotalDone(String cropID) {


        intent.putExtra("titless",title);
        procedureList.clear();

        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/totaldone_api.php?id="+cropID;

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

                                totaldone = Integer.valueOf(plan.getString("totaldone"));


                            }
                            adapterProcedure.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Activity_Crop_PlanDetails.this, "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);

        return totaldone;
    }


}


