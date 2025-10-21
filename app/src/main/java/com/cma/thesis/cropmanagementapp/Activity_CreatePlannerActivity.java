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

    private LocalPlannerHelper localPlannerHelper;
    private FirestoreCropHelper firestoreCropHelper;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    
    private String createPlanStartDate;
    private String createPlanEndDate;
    private boolean isSelectingStartDate;

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
        adapterplan = new AdapterPlanner(this, R.layout.activity_plan_item, planlist, 
            new AdapterPlanner.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(Class_Planner plan, int position) {
                    showDeleteConfirmation(plan, position);
                }
            },
            new AdapterPlanner.OnEditClickListener() {
                @Override
                public void onEditClick(Class_Planner plan, int position) {
                    showEditPlanDialog(plan, position);
                }
            });
        lv.setAdapter(adapterplan);

        // Get crop ID from intent
        cropID = getIntent().getStringExtra("passedID");

        // Initialize helpers
        localPlannerHelper = new LocalPlannerHelper(this);
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
        
        localPlannerHelper.loadPlansByCropId(cropId, new LocalPlannerHelper.PlanLoadCallback() {
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
        isSelectingStartDate = true;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day, 0, 0, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);
                
                if (isSelectingStartDate) {
                    createPlanStartDate = sdf.format(selectedDate.getTime());
                    showCreateEndDateDialog();
                } else {
                    createPlanEndDate = sdf.format(selectedDate.getTime());
                    createPlanWithDates();
                }
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

    private void showCreateEndDateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select End Date");
        builder.setMessage("Start date: " + createPlanStartDate + "\n\nNow select the end date for your crop plan");
        
        builder.setPositiveButton("SELECT END DATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isSelectingStartDate = false;
                showCreateEndDatePicker();
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

    private void showCreateEndDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day, 0, 0, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);
                
                createPlanEndDate = sdf.format(selectedDate.getTime());
                createPlanWithDates();
            }
        };

        DatePickerDialog dialogdate = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        dialogdate.getDatePicker().clearFocus();
        dialogdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogdate.show();
    }

    private void createPlanWithDates() {
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

        // Get crop info for crop name
        firestoreCropHelper.getCropById(cropID, new FirestoreCropHelper.CropCallback() {
            @Override
            public void onSuccess(Class_Crops crop) {
                if (crop != null) {
                    // Use manually selected dates (no calculation needed)
                    final String startDateStr = createPlanStartDate;
                    final String endDateStr = createPlanEndDate;

                    // Save to local database
                    localPlannerHelper.createPlan(cropID, crop.getCropname(), startDateStr, endDateStr,
                            plantingMethod, notificationsEnabled, new LocalPlannerHelper.PlanCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(Activity_CreatePlannerActivity.this, 
                                "Plan created successfully", Toast.LENGTH_SHORT).show();
                            
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

    private void scheduleNotificationsForPlan(String cropId, String startDate) {
        try {
            int cropIdInt = Integer.parseInt(cropId);
            
            // Load procedure steps for the crop from Firestore (crop data still synced)
            FirestorePlannerHelper firestorePlannerHelper = new FirestorePlannerHelper();
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
                Toast.makeText(Activity_CreatePlannerActivity.this, 
                    "Plan deleted successfully", Toast.LENGTH_SHORT).show();
                
                // Remove from list and update adapter
                planlist.remove(position);
                adapterplan.notifyDataSetChanged();
                
                Log.d(TAG, "Plan deleted: " + plan.getId());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error deleting plan: " + error);
                Toast.makeText(Activity_CreatePlannerActivity.this, 
                    "Failed to delete plan: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String tempStartDate;
    private String tempEndDate;
    private boolean isEditingStartDate;

    private void showEditPlanDialog(final Class_Planner plan, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Plan");
        builder.setMessage("Select what you want to edit:\n\n" +
                          "Current:\n" +
                          "Start: " + plan.getStartDate() + "\n" +
                          "End: " + plan.getEndDate() + "\n" +
                          "Method: " + (plan.getPlantingMethod().equals("SABONG_TANIM") ? "Sabong Tanim" : "Lipat Tanim"));
        
        builder.setPositiveButton("EDIT DATES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditDatesDialog(plan, position);
            }
        });
        
        builder.setNeutralButton("CHANGE METHOD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showChangePlantingMethodDialog(plan, position);
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

    private void showEditDatesDialog(final Class_Planner plan, final int position) {
        tempStartDate = plan.getStartDate();
        tempEndDate = plan.getEndDate();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Dates");
        builder.setMessage("First, select the new start date");
        
        builder.setPositiveButton("SELECT START DATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isEditingStartDate = true;
                showEditDatePicker(plan, position);
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

    private void showEditDatePicker(final Class_Planner plan, final int position) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);
                
                if (isEditingStartDate) {
                    tempStartDate = sdf.format(selectedDate.getTime());
                    // Now ask for end date
                    showEndDatePicker(plan, position);
                } else {
                    tempEndDate = sdf.format(selectedDate.getTime());
                    // Save the updated dates
                    updatePlanDates(plan, position);
                }
            }
        };

        DatePickerDialog dialogdate = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        dialogdate.getDatePicker().clearFocus();
        dialogdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogdate.show();
    }

    private void showEndDatePicker(final Class_Planner plan, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Dates");
        builder.setMessage("New start date: " + tempStartDate + "\n\nNow select the end date");
        
        builder.setPositiveButton("SELECT END DATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isEditingStartDate = false;
                showEditDatePicker(plan, position);
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

    private void updatePlanDates(final Class_Planner plan, final int position) {
        localPlannerHelper.updatePlanDates(plan.getId(), tempStartDate, tempEndDate, 
            new LocalPlannerHelper.PlanCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(Activity_CreatePlannerActivity.this, 
                        "Plan dates updated successfully", Toast.LENGTH_SHORT).show();
                    
                    // Update the local object
                    plan.setStartDate(tempStartDate);
                    plan.setEndDate(tempEndDate);
                    adapterplan.notifyDataSetChanged();
                    
                    Log.d(TAG, "Plan dates updated: " + plan.getId());
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error updating plan dates: " + error);
                    Toast.makeText(Activity_CreatePlannerActivity.this, 
                        "Failed to update plan: " + error, Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showChangePlantingMethodDialog(final Class_Planner plan, final int position) {
        final String[] methods = {"Sabong Tanim (Direct Seeding)", "Lipat Tanim (Transplanting)"};
        int currentSelection = plan.getPlantingMethod().equals("SABONG_TANIM") ? 0 : 1;
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Planting Method");
        builder.setSingleChoiceItems(methods, currentSelection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newMethod = (which == 0) ? "SABONG_TANIM" : "LIPAT_TANIM";
                updatePlantingMethod(plan, position, newMethod);
                dialog.dismiss();
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

    private void updatePlantingMethod(final Class_Planner plan, final int position, final String newMethod) {
        localPlannerHelper.updatePlantingMethod(plan.getId(), newMethod, 
            new LocalPlannerHelper.PlanCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(Activity_CreatePlannerActivity.this, 
                        "Planting method updated successfully", Toast.LENGTH_SHORT).show();
                    
                    // Update the local object
                    plan.setPlantingMethod(newMethod);
                    adapterplan.notifyDataSetChanged();
                    
                    Log.d(TAG, "Planting method updated: " + plan.getId());
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error updating planting method: " + error);
                    Toast.makeText(Activity_CreatePlannerActivity.this, 
                        "Failed to update planting method: " + error, Toast.LENGTH_LONG).show();
                }
            });
    }
}
