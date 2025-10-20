package com.cma.thesis.cropmanagementapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity_CreatePlannerActivity extends AppCompatActivity {

    private static final String TAG = "CreatePlannerActivity";

    ListView lv;
    ArrayList<Class_Planner> planlist;
    AdapterPlanner adapterplan;
    String cropID;
    Button btnCreatePlan;
    RadioGroup radioGroupPlantingMethod;
    RadioButton radioSabongTanim;
    RadioButton radioLipatTanim;
    CheckBox checkboxNotifications;

    private FirestorePlannerHelper firestorePlannerHelper;
    private FirestoreCropHelper firestoreCropHelper;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_planner);
        
        // Initialize views
        lv = findViewById(R.id.listProcedure);
        btnCreatePlan = findViewById(R.id.btnCreatePlan);
        radioGroupPlantingMethod = findViewById(R.id.radioGroupPlantingMethod);
        radioSabongTanim = findViewById(R.id.radioSabongTanim);
        radioLipatTanim = findViewById(R.id.radioLipatTanim);
        checkboxNotifications = findViewById(R.id.checkboxNotifications);
        
        // Initialize data structures
        planlist = new ArrayList<>();
        adapterplan = new AdapterPlanner(this, R.layout.activity_plan_item, planlist);
        lv.setAdapter(adapterplan);

        // Get crop ID from intent
        cropID = getIntent().getStringExtra("passedID");

        // Initialize Firestore helpers
        firestorePlannerHelper = new FirestorePlannerHelper();
        firestoreCropHelper = new FirestoreCropHelper();

        // Set up create plan button
        btnCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePlanDialog();
            }
        });

        // Load existing plans
        loadPlans(cropID);

        // Set up list item click listener
        lv.setOnItemClickListener(onListViewClick);
    }

    private AdapterView.OnItemClickListener onListViewClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Class_Planner planner = planlist.get(position);
            
            // TODO: Navigate to plan details activity
            // Intent intent = new Intent(getApplicationContext(), Activity_Crop_PlanDetails.class);
            // intent.putExtra("planid", planner.getId());
            // intent.putExtra("startdate", planner.getStartDate());
            // startActivity(intent);
            
            Toast.makeText(Activity_CreatePlannerActivity.this, 
                "Plan: " + planner.getStartDate() + " - " + planner.getEndDate(), 
                Toast.LENGTH_SHORT).show();
        }
    };

    private void loadPlans(String cropId) {
        planlist.clear();
        
        firestorePlannerHelper.loadPlansByCropId(cropId, new FirestorePlannerHelper.PlanLoadCallback() {
            @Override
            public void onSuccess(ArrayList<Class_Planner> plans) {
                planlist.clear();
                planlist.addAll(plans);
                adapterplan.notifyDataSetChanged();
                Log.d(TAG, "Loaded " + plans.size() + " plans");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading plans: " + error);
                Toast.makeText(Activity_CreatePlannerActivity.this, 
                    "Failed to load plans", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCreatePlanDialog() {
        final AlertDialog.Builder dialogCreatePlan = new AlertDialog.Builder(this);
        dialogCreatePlan.setTitle("Create Plan");
        dialogCreatePlan.setMessage("Select the start date for your crop plan");

        dialogCreatePlan.setPositiveButton("SELECT DATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDatePicker();
            }
        });

        dialogCreatePlan.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        
        dialogCreatePlan.show();
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day, 0, 0, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);
                
                createPlan(selectedDate.getTime());
            }
        };

        final DatePickerDialog dialogdate = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialogdate.getDatePicker().clearFocus();
        dialogdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogdate.show();
    }

    private void createPlan(final Date startDate) {
        // Get planting method
        int selectedMethodId = radioGroupPlantingMethod.getCheckedRadioButtonId();
        final String plantingMethod;
        if (selectedMethodId == R.id.radioSabongTanim) {
            plantingMethod = "SABONG_TANIM";
        } else {
            plantingMethod = "LIPAT_TANIM";
        }

        // Get notifications enabled
        final boolean notificationsEnabled = checkboxNotifications.isChecked();

        // First, get crop info to calculate end date and get crop name
        firestoreCropHelper.getCropById(cropID, new FirestoreCropHelper.CropCallback() {
            @Override
            public void onSuccess(Class_Crops crop) {
                if (crop != null) {
                    // Calculate end date from crop duration
                    Date endDate = calculateEndDate(startDate, crop.getDuration());
                    
                    // Format dates to strings
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    String startDateStr = sdf.format(startDate);
                    String endDateStr = sdf.format(endDate);

                    // Save to Firestore
                    firestorePlannerHelper.createPlan(cropID, crop.getCropname(), startDateStr, endDateStr,
                            plantingMethod, notificationsEnabled, new FirestorePlannerHelper.PlanCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(Activity_CreatePlannerActivity.this, 
                                message, Toast.LENGTH_SHORT).show();
                            
                            // Reload plans
                            loadPlans(cropID);
                            
                            // Schedule notifications if enabled
                            if (notificationsEnabled) {
                                scheduleNotificationsForPlan(cropID, startDateStr);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "Error creating plan: " + error);
                            Toast.makeText(Activity_CreatePlannerActivity.this, 
                                "Failed to create plan: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(Activity_CreatePlannerActivity.this, 
                        "Crop not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading crop: " + error);
                Toast.makeText(Activity_CreatePlannerActivity.this, 
                    "Failed to load crop info: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Date calculateEndDate(Date startDate, String durationStr) {
        try {
            // Parse duration string (e.g., "90 days", "3 months")
            // For simplicity, assume it's in days format or extract number
            int durationDays = parseDurationToDays(durationStr);
            
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(startDate);
            endCal.add(Calendar.DAY_OF_MONTH, durationDays);
            
            return endCal.getTime();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating end date: " + e.getMessage());
            // Default to 90 days if parsing fails
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(startDate);
            endCal.add(Calendar.DAY_OF_MONTH, 90);
            return endCal.getTime();
        }
    }

    private int parseDurationToDays(String durationStr) {
        if (durationStr == null || durationStr.trim().isEmpty()) {
            return 90; // Default
        }
        
        try {
            // Try to extract number from string
            String numStr = durationStr.replaceAll("[^0-9]", "");
            if (!numStr.isEmpty()) {
                int value = Integer.parseInt(numStr);
                
                // Check if it mentions months
                if (durationStr.toLowerCase().contains("month")) {
                    return value * 30; // Convert months to days
                } else if (durationStr.toLowerCase().contains("week")) {
                    return value * 7; // Convert weeks to days
                }
                
                return value; // Assume days
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing duration: " + e.getMessage());
        }
        
        return 90; // Default to 90 days
    }

    private void scheduleNotificationsForPlan(String cropId, String startDate) {
        try {
            int cropIdInt = Integer.parseInt(cropId);
            
            // Load procedure steps for the crop
            firestorePlannerHelper.getPlanSteps(cropIdInt, startDate, new FirestorePlannerHelper.StepsCallback() {
                @Override
                public void onSuccess(ArrayList<Class_Procedure> steps) {
                    if (steps != null && !steps.isEmpty()) {
                        PlannerNotificationManager notificationManager = new PlannerNotificationManager(Activity_CreatePlannerActivity.this);
                        notificationManager.scheduleNotificationsForPlan(cropId, cropIdInt, startDate, steps);
                        Toast.makeText(Activity_CreatePlannerActivity.this, 
                            "Notifications scheduled for " + steps.size() + " steps", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error loading steps for notifications: " + error);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error scheduling notifications: " + e.getMessage());
        }
    }
}
