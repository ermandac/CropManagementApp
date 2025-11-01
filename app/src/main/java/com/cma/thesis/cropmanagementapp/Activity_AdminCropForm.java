package com.cma.thesis.cropmanagementapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for adding or editing a crop
 */
public class Activity_AdminCropForm extends AppCompatActivity {
    
    private static final int REQUEST_IMAGE_PICK = 2001;
    private static final int REQUEST_PERMISSION_READ_MEDIA = 2002;
    
    // UI Components
    private ImageView ivCropPreview;
    private ProgressBar progressImageUpload;
    private Button btnSelectImage;
    private TextInputEditText etCropName;
    private TextInputEditText etScienceName;
    private TextInputEditText etDuration;
    private TextInputEditText etDescription;
    private TextInputEditText etVarieties;
    private TextInputEditText etSoilClimate;
    private TextInputEditText etSeason;
    private TextInputEditText etMainField;
    private TextInputEditText etIrrigation;
    private TextInputEditText etGrowthManagement;
    private TextInputEditText etHarvesting;
    private Button btnSave;
    private FrameLayout loadingOverlay;
    
    // Helpers
    private FirestoreAdminHelper firestoreHelper;
    private ImageHelper imageHelper;
    
    // Data
    private String categoryId;
    private String categoryName;
    private String cropId; // null for new crop, set for editing
    private Uri selectedImageUri;
    private String existingImageUrl; // For edit mode
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crop_form);
        
        // Get intent extras
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        cropId = getIntent().getStringExtra("cropId");
        isEditMode = (cropId != null);
        
        // Initialize helpers
        firestoreHelper = new FirestoreAdminHelper();
        imageHelper = new ImageHelper();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Edit Crop" : "Add Crop");
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
        ivCropPreview = findViewById(R.id.iv_crop_preview);
        progressImageUpload = findViewById(R.id.progress_image_upload);
        btnSelectImage = findViewById(R.id.btn_select_image);
        etCropName = findViewById(R.id.et_crop_name);
        etScienceName = findViewById(R.id.et_science_name);
        etDuration = findViewById(R.id.et_duration);
        etDescription = findViewById(R.id.et_description);
        etVarieties = findViewById(R.id.et_varieties);
        etSoilClimate = findViewById(R.id.et_soil_climate);
        etSeason = findViewById(R.id.et_season);
        etMainField = findViewById(R.id.et_main_field);
        etIrrigation = findViewById(R.id.et_irrigation);
        etGrowthManagement = findViewById(R.id.et_growth_management);
        etHarvesting = findViewById(R.id.et_harvesting);
        btnSave = findViewById(R.id.btn_save);
        loadingOverlay = findViewById(R.id.loading_overlay);
    }
    
    private void setupListeners() {
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveCrop());
    }
    
    private void openImagePicker() {
        // Check and request permissions based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+) - Use READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_PERMISSION_READ_MEDIA);
                return;
            }
        } else {
            // Android 12 and below - Use READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ_MEDIA);
                return;
            }
        }
        
        // Permission granted, open image picker
        launchImagePicker();
    }
    
    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_PERMISSION_READ_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image picker
                launchImagePicker();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Cannot access images.", 
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                // Display selected image
                Glide.with(this)
                        .load(selectedImageUri)
                        .into(ivCropPreview);
            }
        }
    }
    
    private void loadCropData() {
        loadingOverlay.setVisibility(View.VISIBLE);
        
        firestoreHelper.getCropById(cropId, task -> {
            runOnUiThread(() -> {
                loadingOverlay.setVisibility(View.GONE);
                
                if (task.isSuccessful() && task.getResult().exists()) {
                    try {
                        DocumentSnapshot document = task.getResult();
                        
                        // Populate form with existing data
                        etCropName.setText(document.getString("cropName"));
                        etScienceName.setText(document.getString("scienceName"));
                        etDuration.setText(document.getString("duration"));
                        etDescription.setText(document.getString("description"));
                        
                        // Handle varieties array - convert to comma-separated string
                        Object varietiesObj = document.get("varieties");
                        if (varietiesObj instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<String> varietiesList = (List<String>) varietiesObj;
                            String varietiesString = String.join(", ", varietiesList);
                            etVarieties.setText(varietiesString);
                        } else if (varietiesObj instanceof String) {
                            // Fallback if it's already stored as string
                            etVarieties.setText((String) varietiesObj);
                        }
                        
                        etSoilClimate.setText(document.getString("soilClimate"));
                        etSeason.setText(document.getString("season"));
                        etMainField.setText(document.getString("mainField"));
                        etIrrigation.setText(document.getString("irrigation"));
                        etGrowthManagement.setText(document.getString("growthManagement"));
                        etHarvesting.setText(document.getString("harvesting"));
                        
                        // Load existing image
                        existingImageUrl = document.getString("image");
                        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(existingImageUrl)
                                    .into(ivCropPreview);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing crop data: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        Log.e("AdminCropForm", "Error parsing crop data", e);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Error loading crop data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }
    
    private void saveCrop() {
        // Validate required fields
        String cropName = etCropName.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();
        
        if (cropName.isEmpty()) {
            etCropName.setError("Crop name is required");
            etCropName.requestFocus();
            return;
        }
        
        if (duration.isEmpty()) {
            etDuration.setError("Duration is required");
            etDuration.requestFocus();
            return;
        }
        
        // Show loading
        loadingOverlay.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        
        // Check if new image is selected
        if (selectedImageUri != null) {
            // Compress and upload new image
            uploadImageAndSaveCrop(cropName);
        } else {
            // No new image, save with existing image URL (or empty if none)
            saveCropData(isEditMode ? existingImageUrl : "");
        }
    }
    
    private void uploadImageAndSaveCrop(String cropName) {
        // Compress image
        File compressedFile = imageHelper.compressImage(this, selectedImageUri);
        
        if (compressedFile == null) {
            loadingOverlay.setVisibility(View.GONE);
            btnSave.setEnabled(true);
            Toast.makeText(this, "Error compressing image", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Upload to Firebase Storage
        imageHelper.uploadToFirebaseStorage(compressedFile, categoryId, cropName)
                .addOnSuccessListener(downloadUri -> {
                    // If editing and had old image, delete it
                    if (isEditMode && existingImageUrl != null && !existingImageUrl.isEmpty()) {
                        imageHelper.deleteFromFirebaseStorage(existingImageUrl);
                    }
                    
                    // Save crop data with new image URL
                    saveCropData(downloadUri.toString());
                })
                .addOnFailureListener(e -> {
                    loadingOverlay.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Error uploading image: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
    
    private void saveCropData(String imageUrl) {
        // Prepare crop data
        Map<String, Object> cropData = new HashMap<>();
        // Convert categoryId to integer for Firestore
        try {
            cropData.put("categoryId", Integer.parseInt(categoryId));
        } catch (NumberFormatException e) {
            Log.e("AdminCropForm", "Invalid categoryId: " + categoryId, e);
            cropData.put("categoryId", categoryId); // Fallback to string if parse fails
        }
        cropData.put("cropName", etCropName.getText().toString().trim());
        cropData.put("scienceName", etScienceName.getText().toString().trim());
        cropData.put("duration", etDuration.getText().toString().trim());
        cropData.put("description", etDescription.getText().toString().trim());
        
        // Convert varieties from comma-separated string to array
        String varietiesInput = etVarieties.getText().toString().trim();
        List<String> varietiesList = new ArrayList<>();
        if (!varietiesInput.isEmpty()) {
            // Split by comma and trim each variety
            String[] varietiesArray = varietiesInput.split(",");
            for (String variety : varietiesArray) {
                String trimmed = variety.trim();
                if (!trimmed.isEmpty()) {
                    varietiesList.add(trimmed);
                }
            }
        }
        cropData.put("varieties", varietiesList);
        
        cropData.put("soilClimate", etSoilClimate.getText().toString().trim());
        cropData.put("season", etSeason.getText().toString().trim());
        cropData.put("mainField", etMainField.getText().toString().trim());
        cropData.put("irrigation", etIrrigation.getText().toString().trim());
        cropData.put("growthManagement", etGrowthManagement.getText().toString().trim());
        cropData.put("harvesting", etHarvesting.getText().toString().trim());
        cropData.put("image", imageUrl);
        
        if (isEditMode) {
            // Update existing crop
            firestoreHelper.updateCrop(cropId, cropData, task -> {
                loadingOverlay.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Crop updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error updating crop: " + task.getException().getMessage(), 
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Add new crop
            firestoreHelper.addCrop(cropData, task -> {
                loadingOverlay.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Crop added successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error adding crop: " + task.getException().getMessage(), 
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
