package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
 * Activity for displaying and managing pests
 */
public class Activity_AdminPestList extends AppCompatActivity implements AdapterAdminPest.OnPestActionListener {
    
    private static final int REQUEST_ADD_PEST = 1001;
    private static final int REQUEST_EDIT_PEST = 1002;
    
    private RecyclerView recyclerView;
    private AdapterAdminPest adapter;
    private EditText etSearch;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private FloatingActionButton fabAdd;
    
    private FirestorePestHelper firestoreHelper;
    
    private List<DocumentSnapshot> allPests;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pest_list);
        
        // Initialize helper
        firestoreHelper = new FirestorePestHelper();
        allPests = new ArrayList<>();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pest Management");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        recyclerView = findViewById(R.id.rvPests);
        etSearch = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddPest);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterAdminPest(this, this);
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
        fabAdd.setOnClickListener(v -> openPestForm(null));
        
        // Load pests
        loadPests();
    }
    
    private void loadPests() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        
        firestoreHelper.getPestsCollection()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allPests.clear();
                    allPests.addAll(queryDocumentSnapshots.getDocuments());
                    adapter.setPestList(allPests);
                    
                    progressBar.setVisibility(View.GONE);
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error loading pests: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    
    private void openPestForm(String pestId) {
        Intent intent = new Intent(this, Activity_AdminPestForm.class);
        
        if (pestId != null) {
            intent.putExtra("pestId", pestId);
            startActivityForResult(intent, REQUEST_EDIT_PEST);
        } else {
            startActivityForResult(intent, REQUEST_ADD_PEST);
        }
    }
    
    @Override
    public void onEditClick(DocumentSnapshot pest) {
        openPestForm(pest.getId());
    }
    
    @Override
    public void onDeleteClick(DocumentSnapshot pest) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Pest")
                .setMessage("Are you sure you want to delete \"" + pest.getString("pestName") + "\"? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deletePest(pest))
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void deletePest(DocumentSnapshot pest) {
        String pestId = pest.getId();
        
        progressBar.setVisibility(View.VISIBLE);
        
        firestoreHelper.deletePest(pestId, task -> {
            progressBar.setVisibility(View.GONE);
            
            if (task.isSuccessful()) {
                Toast.makeText(this, "Pest deleted successfully", Toast.LENGTH_SHORT).show();
                loadPests(); // Reload list
            } else {
                Toast.makeText(this, "Error deleting pest: " + task.getException().getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_PEST || requestCode == REQUEST_EDIT_PEST) {
                // Reload pests after add/edit
                loadPests();
            }
        }
    }
}
