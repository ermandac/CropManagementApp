package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for adding or editing a suggested crop
 */
public class Activity_AdminSuggestedCropForm extends AppCompatActivity {
    
    // UI Components
    private TextInputEditText etCropName;
    private TextInputEditText etMonth;
    private TextInputEditText etCategory;
    private TextInputEditText etPlantingReason;
    private TextInputEditText etDescription;
    private TextInputEditText etBestVarieties;
    private TextInputEditText etExpectedHarvest;
    private TextInputEditText etTips;
    private TextInputEditText etPopularity;
    private Button btnSave;
    private FrameLayout loadingOverlay;
    
    // Helper
    private FirestoreSuggestedCropsHelper firestoreHelper;
    
    // Data
    private String cropId; // null for new crop, set for editing
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_suggested_crop_form);
        
        // Get intent extras
        cropId = getIntent().getStringExtra("cropId");
        isEditMode = (cropId != null);
        
        // Initialize helper
        firestoreHelper = new FirestoreSuggestedCropsHelper();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Edit Suggested Crop" : "Add Suggested Crop");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        initializeViews();
        
        // Setup listeners
        setupListeners();
        
        // Load existing crop data if in edit mode
        if (isEditMode) {
            loadCropData();
        }
    }
    
    private void initializeViews() {
        etCropName = findViewById(R.id.et_crop_name);
        etMonth = findViewById(R.id.et_month);
        etCategory = findViewById(R.id.et_category);
        etPlantingReason = findViewById(R.id.et_planting_reason);
        etDescription = findViewById(R.id.et_description);
        etBestVarieties = findViewById(R.id.et_best_varieties);
        etExpectedHarvest = findViewById(R.id.et_expected_harvest);
        etTips = findViewById(R.id.et_tips);
        etPopularity = findViewById(R.id.et_popularity);
        btnSave = findViewById(R.id.btn_save);
        loadingOverlay = findViewById(R.id.loading_overlay);
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveCrop());
    }
    
    private void loadCropData() {
        loadingOverlay.setVisibility(View.VISIBLE);
        
        firestoreHelper.getSuggestedCropById(cropId, new FirestoreSuggestedCropsHelper.SingleCropCallback() {
            @Override
            public void onSuccess(Class_SuggestedCrop crop) {
                runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    
                    // Populate form with existing data
                    etCropName.setText(crop.getCropName());
                    etMonth.setText(crop.getMonth());
                    etCategory.setText(crop.getCategory());
                    etPlantingReason.setText(crop.getPlantingReason());
                    etDescription.setText(crop.getDescription());
                    
                    // Handle bestVarieties list - convert to comma-separated string
                    if (crop.getBestVarieties() != null && !crop.getBestVarieties().isEmpty()) {
                        String varietiesString = String.join(", ", crop.getBestVarieties());
                        etBestVarieties.setText(varietiesString);
                    }
                    
                    etExpectedHarvest.setText(crop.getExpectedHarvest());
                    etTips.setText(crop.getTips());
                    etPopularity.setText(crop.getPopularity());
                });
            }
            
            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    Toast.makeText(Activity_AdminSuggestedCropForm.this, 
                            "Error loading crop data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
    
    private void saveCrop() {
        // Validate required fields
        String cropName = etCropName.getText().toString().trim();
        String month = etMonth.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        
        if (cropName.isEmpty()) {
            etCropName.setError("Crop name is required");
            etCropName.requestFocus();
            return;
        }
        
        if (month.isEmpty()) {
            etMonth.setError("Planting month is required");
            etMonth.requestFocus();
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
        
        // Prepare crop object
        Class_SuggestedCrop crop = new Class_SuggestedCrop();
        crop.setCropName(cropName);
        crop.setMonth(month);
        crop.setCategory(category);
        crop.setPlantingReason(etPlantingReason.getText().toString().trim());
        crop.setDescription(etDescription.getText().toString().trim());
        
        // Convert bestVarieties from comma-separated string to list
        String varietiesInput = etBestVarieties.getText().toString().trim();
        List<String> varietiesList = new ArrayList<>();
        if (!varietiesInput.isEmpty()) {
            String[] varietiesArray = varietiesInput.split(",");
            for (String variety : varietiesArray) {
                String trimmed = variety.trim();
                if (!trimmed.isEmpty()) {
                    varietiesList.add(trimmed);
                }
            }
        }
        crop.setBestVarieties(varietiesList);
        
        crop.setExpectedHarvest(etExpectedHarvest.getText().toString().trim());
        crop.setTips(etTips.getText().toString().trim());
        crop.setPopularity(etPopularity.getText().toString().trim());
        
        if (isEditMode) {
            // Update existing crop
            crop.setId(cropId);
            firestoreHelper.updateSuggestedCrop(cropId, crop, new FirestoreSuggestedCropsHelper.OperationCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(Activity_AdminSuggestedCropForm.this, message, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                }
                
                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(Activity_AdminSuggestedCropForm.this, 
                                "Error updating crop: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            // Add new crop
            firestoreHelper.addSuggestedCrop(crop, new FirestoreSuggestedCropsHelper.OperationCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(Activity_AdminSuggestedCropForm.this, message, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                }
                
                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(Activity_AdminSuggestedCropForm.this, 
                                "Error adding crop: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
}
