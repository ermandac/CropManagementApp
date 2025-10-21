package com.cma.thesis.cropmanagementapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
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
    LocalPlannerHelper localPlannerHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planlist);

        lv =(ListView) findViewById(R.id.cropGridViewPlanners);
        plannerList = new ArrayList<>();
        plannerlistadapter = new AdapterPlanner(this, R.layout.activity_plan_item, plannerList, 
            new AdapterPlanner.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(Class_Planner plan, int position) {
                    showDeleteConfirmation(plan, position);
                }
            },
            new AdapterPlanner.OnEditClickListener() {
                @Override
                public void onEditClick(Class_Planner plan, int position) {
                    Toast.makeText(Activity_PlannerList.this, "Edit: " + plan.getStartDate(), Toast.LENGTH_SHORT).show();
                    // TODO: Implement edit functionality
                }
            });

        lv.setAdapter(plannerlistadapter);

        cropid = getIntent().getStringExtra("passedID");

        // Initialize local planner helper
        localPlannerHelper = new LocalPlannerHelper(this);

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
        
        localPlannerHelper.loadPlansByCropId(cropid, new LocalPlannerHelper.PlanLoadCallback() {
            @Override
            public void onSuccess(ArrayList<Class_Planner> plans) {
                plannerList.clear();
                plannerList.addAll(plans);
                plannerlistadapter.notifyDataSetChanged();
                Log.d("PlannerList", "Loaded " + plans.size() + " plans");
            }

            @Override
            public void onError(String error) {
                Log.e("PlannerList", "Error loading plans: " + error);
                Toast.makeText(Activity_PlannerList.this, 
                    "Failed to load plans", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmation(final Class_Planner plan, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Plan");
        builder.setMessage("Are you sure you want to delete this plan?\n\n" + 
                          plan.getStartDate() + " - " + plan.getEndDate());
        
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePlan(plan, position);
            }
        });
        
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        builder.show();
    }

    private void deletePlan(final Class_Planner plan, final int position) {
        localPlannerHelper.deletePlan(plan.getId(), new LocalPlannerHelper.PlanCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(Activity_PlannerList.this, 
                    "Plan deleted successfully", Toast.LENGTH_SHORT).show();
                
                // Remove from list and update adapter
                plannerList.remove(position);
                plannerlistadapter.notifyDataSetChanged();
                
                Log.d("PlannerList", "Plan deleted: " + plan.getId());
            }

            @Override
            public void onError(String error) {
                Log.e("PlannerList", "Error deleting plan: " + error);
                Toast.makeText(Activity_PlannerList.this, 
                    "Failed to delete plan: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
