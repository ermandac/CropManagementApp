package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for adding or editing a fertilizer
 */
public class Activity_AdminFertilizerForm extends AppCompatActivity {
    
    // UI Components
    private TextInputEditText etFertilizerName;
    private TextInputEditText etNPK;
    private TextInputEditText etCategory;
    private RadioGroup rgType;
    private RadioButton rbOrganic;
    private RadioButton rbChemical;
    private TextInputEditText etDescription;
    private TextInputEditText etApplicationRate;
    private TextInputEditText etApplicationMethod;
    private TextInputEditText etSuitableCrops;
    private TextInputEditText etSafetyPrecautions;
    private TextInputEditText etStorageInstructions;
    private Button btnSave;
    private FrameLayout loadingOverlay;
    
    // Helper
    private FirestoreFertilizerHelper firestoreHelper;
    
    // Data
    private String fertilizerId; // null for new fertilizer, set for editing
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fertilizer_form);
        
        // Get intent extras
        fertilizerId = getIntent().getStringExtra("fertilizerId");
        isEditMode = (fertilizerId != null);
        
        // Initialize helper
        firestoreHelper = new FirestoreFertilizerHelper();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Edit Fertilizer" : "Add Fertilizer");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        initializeViews();
        
        // Setup listeners
        setupListeners();
        
        // Load existing fertilizer data if in edit mode
        if (isEditMode) {
            loadFertilizerData();
        }
    }
    
    private void initializeViews() {
        etFertilizerName = findViewById(R.id.et_fertilizer_name);
        etNPK = findViewById(R.id.et_npk);
        etCategory = findViewById(R.id.et_category);
        rgType = findViewById(R.id.rg_type);
        rbOrganic = findViewById(R.id.rb_organic);
        rbChemical = findViewById(R.id.rb_chemical);
        etDescription = findViewById(R.id.et_description);
        etApplicationRate = findViewById(R.id.et_application_rate);
        etApplicationMethod = findViewById(R.id.et_application_method);
        etSuitableCrops = findViewById(R.id.et_suitable_crops);
        etSafetyPrecautions = findViewById(R.id.et_safety_precautions);
        etStorageInstructions = findViewById(R.id.et_storage_instructions);
        btnSave = findViewById(R.id.btn_save);
        loadingOverlay = findViewById(R.id.loading_overlay);
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveFertilizer());
    }
    
    private void loadFertilizerData() {
        loadingOverlay.setVisibility(View.VISIBLE);
        
        firestoreHelper.getFertilizersCollection()
                .document(fertilizerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        
                        if (documentSnapshot.exists()) {
                            // Populate form with existing data
                            etFertilizerName.setText(documentSnapshot.getString("name"));
                            etNPK.setText(documentSnapshot.getString("npk"));
                            etCategory.setText(documentSnapshot.getString("category"));
                            
                            // Set organic/chemical radio button
                            Boolean isOrganic = documentSnapshot.getBoolean("isOrganic");
                            if (isOrganic != null && isOrganic) {
                                rbOrganic.setChecked(true);
                            } else {
                                rbChemical.setChecked(true);
                            }
                            
                            etDescription.setText(documentSnapshot.getString("description"));
                            etApplicationRate.setText(documentSnapshot.getString("applicationRate"));
                            etApplicationMethod.setText(documentSnapshot.getString("applicationMethod"));
                            etSuitableCrops.setText(documentSnapshot.getString("suitableCrops"));
                            etSafetyPrecautions.setText(documentSnapshot.getString("safetyPrecautions"));
                            etStorageInstructions.setText(documentSnapshot.getString("storageInstructions"));
                        } else {
                            Toast.makeText(this, "Error loading fertilizer data", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        Toast.makeText(this, "Error loading fertilizer data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
    }
    
    private void saveFertilizer() {
        // Validate required fields
        String name = etFertilizerName.getText().toString().trim();
        String npk = etNPK.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        
        if (name.isEmpty()) {
            etFertilizerName.setError("Fertilizer name is required");
            etFertilizerName.requestFocus();
            return;
        }
        
        if (npk.isEmpty()) {
            etNPK.setError("NPK ratio is required");
            etNPK.requestFocus();
            return;
        }
        
        if (category.isEmpty()) {
            etCategory.setError("Category is required");
            etCategory.requestFocus();
            return;
        }
        
        // Show loading
        loadingOverlay.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        
        // Prepare fertilizer data
        Map<String, Object> fertilizerData = new HashMap<>();
        fertilizerData.put("name", name);
        fertilizerData.put("npk", npk);
        fertilizerData.put("category", category);
        
        // Set isOrganic based on radio button selection
        boolean isOrganic = rbOrganic.isChecked();
        fertilizerData.put("isOrganic", isOrganic);
        
        fertilizerData.put("description", etDescription.getText().toString().trim());
        fertilizerData.put("applicationRate", etApplicationRate.getText().toString().trim());
        fertilizerData.put("applicationMethod", etApplicationMethod.getText().toString().trim());
        fertilizerData.put("suitableCrops", etSuitableCrops.getText().toString().trim());
        fertilizerData.put("safetyPrecautions", etSafetyPrecautions.getText().toString().trim());
        fertilizerData.put("storageInstructions", etStorageInstructions.getText().toString().trim());
        
        if (isEditMode) {
            // Update existing fertilizer
            firestoreHelper.updateFertilizer(fertilizerId, fertilizerData, task -> {
                runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Fertilizer updated successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Error updating fertilizer: " + task.getException().getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            // Add new fertilizer
            firestoreHelper.addFertilizer(fertilizerData, task -> {
                runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Fertilizer added successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Error adding fertilizer: " + task.getException().getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
