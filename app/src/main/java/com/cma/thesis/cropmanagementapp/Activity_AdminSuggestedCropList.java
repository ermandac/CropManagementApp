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

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying and managing suggested crops in admin panel
 */
public class Activity_AdminSuggestedCropList extends AppCompatActivity implements AdapterAdminSuggestedCrop.OnSuggestedCropActionListener {
    
    private static final int REQUEST_ADD_CROP = 1001;
    private static final int REQUEST_EDIT_CROP = 1002;
    
    private RecyclerView recyclerView;
    private AdapterAdminSuggestedCrop adapter;
    private EditText etSearch;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private FloatingActionButton fabAdd;
    private TextView tvItemCount;
    
    private FirestoreSuggestedCropsHelper firestoreHelper;
    private List<Class_SuggestedCrop> allCrops;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_suggested_crop_list);
        
        // Initialize helper
        firestoreHelper = new FirestoreSuggestedCropsHelper();
        allCrops = new ArrayList<>();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Suggested Crops Management");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        recyclerView = findViewById(R.id.rvSuggestedCrops);
        etSearch = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddSuggestedCrop);
        tvItemCount = findViewById(R.id.tvItemCount);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterAdminSuggestedCrop(this, this);
        recyclerView.setAdapter(adapter);
        
        // Setup search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
                updateEmptyState();
                updateItemCount();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Setup FAB
        fabAdd.setOnClickListener(v -> openSuggestedCropForm(null));
        
        // Load suggested crops
        loadSuggestedCrops();
    }
    
    private void loadSuggestedCrops() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        
        firestoreHelper.getAllSuggestedCrops(new FirestoreSuggestedCropsHelper.CropsCallback() {
            @Override
            public void onSuccess(List<Class_SuggestedCrop> crops) {
                allCrops.clear();
                allCrops.addAll(crops);
                adapter.setSuggestedCropList(allCrops);
                
                progressBar.setVisibility(View.GONE);
                updateEmptyState();
                updateItemCount();
            }
            
            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Activity_AdminSuggestedCropList.this, 
                        "Error loading suggested crops: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                updateEmptyState();
            }
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
    
    private void updateItemCount() {
        if (tvItemCount != null) {
            int count = adapter.getItemCount();
            tvItemCount.setText(count + (count == 1 ? " item" : " items"));
        }
    }
    
    private void openSuggestedCropForm(String cropId) {
        Intent intent = new Intent(this, Activity_AdminSuggestedCropForm.class);
        
        if (cropId != null) {
            intent.putExtra("cropId", cropId);
            startActivityForResult(intent, REQUEST_EDIT_CROP);
        } else {
            startActivityForResult(intent, REQUEST_ADD_CROP);
        }
    }
    
    @Override
    public void onEditClick(Class_SuggestedCrop crop) {
        openSuggestedCropForm(crop.getId());
    }
    
    @Override
    public void onDeleteClick(Class_SuggestedCrop crop) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Suggested Crop")
                .setMessage("Are you sure you want to delete \"" + crop.getCropName() + "\"? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteSuggestedCrop(crop))
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void deleteSuggestedCrop(Class_SuggestedCrop crop) {
        String cropId = crop.getId();
        
        progressBar.setVisibility(View.VISIBLE);
        
        firestoreHelper.deleteSuggestedCrop(cropId, new FirestoreSuggestedCropsHelper.OperationCallback() {
            @Override
            public void onSuccess(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Activity_AdminSuggestedCropList.this, message, Toast.LENGTH_SHORT).show();
                loadSuggestedCrops(); // Reload list
            }
            
            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Activity_AdminSuggestedCropList.this, 
                        "Error deleting suggested crop: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_CROP || requestCode == REQUEST_EDIT_CROP) {
                // Reload suggested crops after add/edit
                loadSuggestedCrops();
            }
        }
    }
}
