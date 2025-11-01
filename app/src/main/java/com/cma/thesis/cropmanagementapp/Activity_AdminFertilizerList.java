package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying and managing fertilizers
 */
public class Activity_AdminFertilizerList extends AppCompatActivity implements AdapterAdminFertilizer.OnFertilizerActionListener {
    
    private static final int REQUEST_ADD_FERTILIZER = 1001;
    private static final int REQUEST_EDIT_FERTILIZER = 1002;
    
    private RecyclerView recyclerView;
    private AdapterAdminFertilizer adapter;
    private EditText etSearch;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private FloatingActionButton fabAdd;
    private Button btnFilterAll, btnFilterOrganic, btnFilterChemical;
    
    private FirestoreFertilizerHelper firestoreHelper;
    
    private List<DocumentSnapshot> allFertilizers;
    private String currentFilter = "All";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fertilizer_list);
        
        // Initialize helper
        firestoreHelper = new FirestoreFertilizerHelper();
        allFertilizers = new ArrayList<>();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Fertilizer Management");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        recyclerView = findViewById(R.id.rvFertilizers);
        etSearch = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddFertilizer);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterOrganic = findViewById(R.id.btnFilterOrganic);
        btnFilterChemical = findViewById(R.id.btnFilterChemical);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterAdminFertilizer(this, this);
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
        
        // Setup filter buttons
        btnFilterAll.setOnClickListener(v -> applyFilter("All"));
        btnFilterOrganic.setOnClickListener(v -> applyFilter("Organic"));
        btnFilterChemical.setOnClickListener(v -> applyFilter("Chemical"));
        
        // Setup FAB
        fabAdd.setOnClickListener(v -> openFertilizerForm(null));
        
        // Load fertilizers
        loadFertilizers();
    }
    
    private void applyFilter(String filterType) {
        currentFilter = filterType;
        
        // Update button states
        btnFilterAll.setBackgroundColor(getResources().getColor(
                filterType.equals("All") ? R.color.colorPrimary : android.R.color.darker_gray));
        btnFilterOrganic.setBackgroundColor(getResources().getColor(
                filterType.equals("Organic") ? R.color.colorPrimary : android.R.color.darker_gray));
        btnFilterChemical.setBackgroundColor(getResources().getColor(
                filterType.equals("Chemical") ? R.color.colorPrimary : android.R.color.darker_gray));
        
        // Apply filter
        adapter.filterByType(filterType);
        updateEmptyState();
    }
    
    private void loadFertilizers() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        
        firestoreHelper.getFertilizersCollection()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allFertilizers.clear();
                    allFertilizers.addAll(queryDocumentSnapshots.getDocuments());
                    adapter.setFertilizerList(allFertilizers);
                    
                    // Reapply current filter
                    applyFilter(currentFilter);
                    
                    progressBar.setVisibility(View.GONE);
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error loading fertilizers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    
    private void openFertilizerForm(String fertilizerId) {
        Intent intent = new Intent(this, Activity_AdminFertilizerForm.class);
        
        if (fertilizerId != null) {
            intent.putExtra("fertilizerId", fertilizerId);
            startActivityForResult(intent, REQUEST_EDIT_FERTILIZER);
        } else {
            startActivityForResult(intent, REQUEST_ADD_FERTILIZER);
        }
    }
    
    @Override
    public void onEditClick(DocumentSnapshot fertilizer) {
        openFertilizerForm(fertilizer.getId());
    }
    
    @Override
    public void onDeleteClick(DocumentSnapshot fertilizer) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Fertilizer")
                .setMessage("Are you sure you want to delete \"" + fertilizer.getString("name") + "\"? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteFertilizer(fertilizer))
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void deleteFertilizer(DocumentSnapshot fertilizer) {
        String fertilizerId = fertilizer.getId();
        
        progressBar.setVisibility(View.VISIBLE);
        
        firestoreHelper.deleteFertilizer(fertilizerId, task -> {
            progressBar.setVisibility(View.GONE);
            
            if (task.isSuccessful()) {
                Toast.makeText(this, "Fertilizer deleted successfully", Toast.LENGTH_SHORT).show();
                loadFertilizers(); // Reload list
            } else {
                Toast.makeText(this, "Error deleting fertilizer: " + task.getException().getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_FERTILIZER || requestCode == REQUEST_EDIT_FERTILIZER) {
                // Reload fertilizers after add/edit
                loadFertilizers();
            }
        }
    }
}
