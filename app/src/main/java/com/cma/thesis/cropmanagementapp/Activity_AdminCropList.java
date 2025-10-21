package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying and managing crops in a category
 */
public class Activity_AdminCropList extends AppCompatActivity implements AdapterAdminCrop.OnCropActionListener {
    
    private static final String EXTRA_CATEGORY_ID = "categoryId";
    private static final String EXTRA_CATEGORY_NAME = "categoryName";
    private static final int REQUEST_ADD_CROP = 1001;
    private static final int REQUEST_EDIT_CROP = 1002;
    
    private RecyclerView recyclerView;
    private AdapterAdminCrop adapter;
    private EditText etSearch;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private FloatingActionButton fabAdd;
    
    private FirestoreAdminHelper firestoreHelper;
    private ImageHelper imageHelper;
    
    private String categoryId;
    private String categoryName;
    private List<DocumentSnapshot> allCrops;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crop_list);
        
        // Get category info from intent
        categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        
        // Initialize helpers
        firestoreHelper = new FirestoreAdminHelper();
        imageHelper = new ImageHelper();
        allCrops = new ArrayList<>();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        recyclerView = findViewById(R.id.rvCrops);
        etSearch = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddCrop);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterAdminCrop(this, this);
        recyclerView.setAdapter(adapter);
        
        // Setup search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
                updateEmptyState();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Setup FAB
        fabAdd.setOnClickListener(v -> openCropForm(null));
        
        // Load crops
        loadCrops();
    }
    
    private void loadCrops() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        
        // Convert categoryId to integer for Firestore query
        int categoryIdInt = Integer.parseInt(categoryId);
        
        firestoreHelper.getCropsCollection()
                .whereEqualTo("categoryId", categoryIdInt)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allCrops.clear();
                    allCrops.addAll(queryDocumentSnapshots.getDocuments());
                    adapter.setCropList(allCrops);
                    
                    progressBar.setVisibility(View.GONE);
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error loading crops: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    updateEmptyState();
                });
    }
    
    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }
    
    private void openCropForm(String cropId) {
        Intent intent = new Intent(this, Activity_AdminCropForm.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categoryName", categoryName);
        
        if (cropId != null) {
            intent.putExtra("cropId", cropId);
            startActivityForResult(intent, REQUEST_EDIT_CROP);
        } else {
            startActivityForResult(intent, REQUEST_ADD_CROP);
        }
    }
    
    @Override
    public void onEditClick(DocumentSnapshot crop) {
        openCropForm(crop.getId());
    }
    
    @Override
    public void onDeleteClick(DocumentSnapshot crop) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Crop")
                .setMessage("Are you sure you want to delete \"" + crop.getString("cropName") + "\"? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteCrop(crop))
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void deleteCrop(DocumentSnapshot crop) {
        String cropId = crop.getId();
        String imageUrl = crop.getString("image");
        
        progressBar.setVisibility(View.VISIBLE);
        
        // First delete image from Storage (if exists)
        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageHelper.deleteFromFirebaseStorage(imageUrl)
                    .addOnCompleteListener(task -> {
                        // Then delete Firestore document (regardless of image deletion result)
                        deleteCropFromFirestore(cropId);
                    });
        } else {
            // No image, just delete Firestore document
            deleteCropFromFirestore(cropId);
        }
    }
    
    private void deleteCropFromFirestore(String cropId) {
        firestoreHelper.deleteCrop(cropId, task -> {
            progressBar.setVisibility(View.GONE);
            
            if (task.isSuccessful()) {
                Toast.makeText(this, "Crop deleted successfully", Toast.LENGTH_SHORT).show();
                loadCrops(); // Reload list
            } else {
                Toast.makeText(this, "Error deleting crop: " + task.getException().getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_CROP || requestCode == REQUEST_EDIT_CROP) {
                // Reload crops after add/edit
                loadCrops();
            }
        }
    }
}
