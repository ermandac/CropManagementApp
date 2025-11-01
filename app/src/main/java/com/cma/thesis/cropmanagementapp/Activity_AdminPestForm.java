package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for adding or editing a pest
 */
public class Activity_AdminPestForm extends AppCompatActivity {
    
    // UI Components
    private TextInputEditText etPestName;
    private TextInputEditText etScientificName;
    private TextInputEditText etCategory;
    private TextInputEditText etAffectedCrops;
    private TextInputEditText etDescription;
    private TextInputEditText etSymptoms;
    private TextInputEditText etControlMethods;
    private TextInputEditText etPreventionTips;
    private TextInputEditText etSeverity;
    private TextInputEditText etCommonSeason;
    private Button btnSave;
    private FrameLayout loadingOverlay;
    
    // Helper
    private FirestorePestHelper firestoreHelper;
    
    // Data
    private String pestId; // null for new pest, set for editing
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pest_form);
        
        // Get intent extras
        pestId = getIntent().getStringExtra("pestId");
        isEditMode = (pestId != null);
        
        // Initialize helper
        firestoreHelper = new FirestorePestHelper();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Edit Pest" : "Add Pest");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        initializeViews();
        
        // Setup listeners
        setupListeners();
        
        // Load existing pest data if in edit mode
        if (isEditMode) {
            loadPestData();
        }
    }
    
    private void initializeViews() {
        etPestName = findViewById(R.id.et_pest_name);
        etScientificName = findViewById(R.id.et_scientific_name);
        etCategory = findViewById(R.id.et_category);
        etAffectedCrops = findViewById(R.id.et_affected_crops);
        etDescription = findViewById(R.id.et_description);
        etSymptoms = findViewById(R.id.et_symptoms);
        etControlMethods = findViewById(R.id.et_control_methods);
        etPreventionTips = findViewById(R.id.et_prevention_tips);
        etSeverity = findViewById(R.id.et_severity);
        etCommonSeason = findViewById(R.id.et_common_season);
        btnSave = findViewById(R.id.btn_save);
        loadingOverlay = findViewById(R.id.loading_overlay);
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> savePest());
    }
    
    private void loadPestData() {
        loadingOverlay.setVisibility(View.VISIBLE);
        
        firestoreHelper.getPestsCollection()
                .document(pestId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        
                        if (documentSnapshot.exists()) {
                    
                            // Populate form with existing data
                            etPestName.setText(documentSnapshot.getString("pestName"));
                            etScientificName.setText(documentSnapshot.getString("scientificName"));
                            etCategory.setText(documentSnapshot.getString("category"));
                            
                            // Handle affectedCrops array - convert to comma-separated string
                            Object affectedCropsObj = documentSnapshot.get("affectedCrops");
                            if (affectedCropsObj instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<String> affectedCropsList = (List<String>) affectedCropsObj;
                                String affectedCropsString = String.join(", ", affectedCropsList);
                                etAffectedCrops.setText(affectedCropsString);
                            }
                            
                            etDescription.setText(documentSnapshot.getString("description"));
                            etSymptoms.setText(documentSnapshot.getString("symptoms"));
                            
                            // Handle controlMethods array - convert to comma-separated string
                            Object controlMethodsObj = documentSnapshot.get("controlMethods");
                            if (controlMethodsObj instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<String> controlMethodsList = (List<String>) controlMethodsObj;
                                String controlMethodsString = String.join(", ", controlMethodsList);
                                etControlMethods.setText(controlMethodsString);
                            }
                            
                            etPreventionTips.setText(documentSnapshot.getString("preventionTips"));
                            etSeverity.setText(documentSnapshot.getString("severity"));
                            etCommonSeason.setText(documentSnapshot.getString("commonSeason"));
                        } else {
                            Toast.makeText(this, "Error loading pest data", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        Toast.makeText(this, "Error loading pest data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
    }
    
    private void savePest() {
        // Validate required fields
        String pestName = etPestName.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String severity = etSeverity.getText().toString().trim();
        
        if (pestName.isEmpty()) {
            etPestName.setError("Pest name is required");
            etPestName.requestFocus();
            return;
        }
        
        if (category.isEmpty()) {
            etCategory.setError("Category is required");
            etCategory.requestFocus();
            return;
        }
        
        if (severity.isEmpty()) {
            etSeverity.setError("Severity is required");
            etSeverity.requestFocus();
            return;
        }
        
        // Show loading
        loadingOverlay.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        
        // Prepare pest data
        Map<String, Object> pestData = new HashMap<>();
        pestData.put("pestName", pestName);
        pestData.put("scientificName", etScientificName.getText().toString().trim());
        pestData.put("category", category);
        
        // Convert affectedCrops from comma-separated string to array
        String affectedCropsInput = etAffectedCrops.getText().toString().trim();
        List<String> affectedCropsList = new ArrayList<>();
        if (!affectedCropsInput.isEmpty()) {
            String[] affectedCropsArray = affectedCropsInput.split(",");
            for (String crop : affectedCropsArray) {
                String trimmed = crop.trim();
                if (!trimmed.isEmpty()) {
                    affectedCropsList.add(trimmed);
                }
            }
        }
        pestData.put("affectedCrops", affectedCropsList);
        
        pestData.put("description", etDescription.getText().toString().trim());
        pestData.put("symptoms", etSymptoms.getText().toString().trim());
        
        // Convert controlMethods from comma-separated string to array
        String controlMethodsInput = etControlMethods.getText().toString().trim();
        List<String> controlMethodsList = new ArrayList<>();
        if (!controlMethodsInput.isEmpty()) {
            String[] controlMethodsArray = controlMethodsInput.split(",");
            for (String method : controlMethodsArray) {
                String trimmed = method.trim();
                if (!trimmed.isEmpty()) {
                    controlMethodsList.add(trimmed);
                }
            }
        }
        pestData.put("controlMethods", controlMethodsList);
        
        pestData.put("preventionTips", etPreventionTips.getText().toString().trim());
        pestData.put("severity", severity);
        pestData.put("commonSeason", etCommonSeason.getText().toString().trim());
        
        if (isEditMode) {
            // Update existing pest
            firestoreHelper.updatePest(pestId, pestData, task -> {
                runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Pest updated successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Error updating pest: " + task.getException().getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            // Add new pest
            firestoreHelper.addPest(pestData, task -> {
                runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Pest added successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Error adding pest: " + task.getException().getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
