package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_Pest extends AppCompatActivity {

    private Spinner severitySpinner;
    private ListView pestsListView;
    private ProgressBar loadingProgress;
    private LinearLayout emptyState;
    private TextView emptyStateText;

    private Adapter_Pest adapter;
    private List<Class_Pest> allPestsList;
    private List<Class_Pest> filteredPestsList;

    private FirestorePestHelper firestoreHelper;

    // Severity filter options
    private final String[] severityLevels = {
            "All",
            "Very High",
            "High",
            "Medium"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest);

        // Initialize views
        initViews();

        // Initialize Firestore helper
        firestoreHelper = new FirestorePestHelper();

        // Initialize lists
        allPestsList = new ArrayList<>();
        filteredPestsList = new ArrayList<>();

        // Setup spinner
        setupSeveritySpinner();

        // Load pests data
        loadPestsFromFirestore();
    }

    private void initViews() {
        severitySpinner = findViewById(R.id.severity_spinner);
        pestsListView = findViewById(R.id.pests_listview);
        loadingProgress = findViewById(R.id.loading_progress);
        emptyState = findViewById(R.id.empty_state);
        emptyStateText = findViewById(R.id.empty_state_text);
    }

    private void setupSeveritySpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                severityLevels
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        severitySpinner.setAdapter(spinnerAdapter);

        severitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSeverity = severityLevels[position];
                filterPestsBySeverity(selectedSeverity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadPestsFromFirestore() {
        showLoading(true);

        firestoreHelper.getAllPests(new FirestorePestHelper.PestsCallback() {
            @Override
            public void onSuccess(List<Class_Pest> pests) {
                allPestsList = pests;
                filteredPestsList = new ArrayList<>(pests);
                
                runOnUiThread(() -> {
                    showLoading(false);
                    
                    if (pests.isEmpty()) {
                        showEmptyState(true, "No pest information available");
                    } else {
                        showEmptyState(false, null);
                        setupListView();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showEmptyState(true, "Error loading pests");
                    Toast.makeText(Activity_Pest.this,
                            "Failed to load pest information: " + error,
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setupListView() {
        adapter = new Adapter_Pest(this, filteredPestsList);
        pestsListView.setAdapter(adapter);
    }

    private void filterPestsBySeverity(String selectedSeverity) {
        if (allPestsList == null || allPestsList.isEmpty()) {
            return;
        }

        filteredPestsList.clear();

        if (selectedSeverity.equals("All")) {
            // Show all pests
            filteredPestsList.addAll(allPestsList);
        } else {
            // Filter by selected severity
            for (Class_Pest pest : allPestsList) {
                if (pest.getSeverity() != null && pest.getSeverity().equals(selectedSeverity)) {
                    filteredPestsList.add(pest);
                }
            }
        }

        // Update UI
        if (filteredPestsList.isEmpty()) {
            showEmptyState(true, "No pests found with " + selectedSeverity + " severity");
        } else {
            showEmptyState(false, null);
        }

        // Update adapter
        if (adapter != null) {
            adapter.updatePestsList(filteredPestsList);
        }
    }

    private void showLoading(boolean show) {
        loadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        pestsListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show, String message) {
        emptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        pestsListView.setVisibility(show ? View.GONE : View.VISIBLE);
        
        if (show && message != null) {
            emptyStateText.setText(message);
        }
    }
}
