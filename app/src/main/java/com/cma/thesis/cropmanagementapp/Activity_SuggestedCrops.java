package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_SuggestedCrops extends AppCompatActivity {

    private Spinner monthSpinner;
    private ListView cropsListView;
    private ProgressBar loadingProgress;
    private LinearLayout emptyState;
    private TextView emptyStateText;

    private Adapter_SuggestedCrop adapter;
    private List<Class_SuggestedCrop> allCropsList;
    private List<Class_SuggestedCrop> filteredCropsList;

    private FirestoreSuggestedCropsHelper firestoreHelper;

    // Month filter options
    private final String[] months = {
            "All",
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_crops);

        // Initialize views
        initViews();

        // Initialize Firestore helper
        firestoreHelper = new FirestoreSuggestedCropsHelper();

        // Initialize lists
        allCropsList = new ArrayList<>();
        filteredCropsList = new ArrayList<>();

        // Setup spinner
        setupMonthSpinner();

        // Load crops data
        loadSuggestedCrops();
    }

    private void initViews() {
        monthSpinner = findViewById(R.id.month_spinner);
        cropsListView = findViewById(R.id.crops_listview);
        loadingProgress = findViewById(R.id.loading_progress);
        emptyState = findViewById(R.id.empty_state);
        emptyStateText = findViewById(R.id.empty_state_text);
    }

    private void setupMonthSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                months
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(spinnerAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = months[position];
                filterCropsByMonth(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadSuggestedCrops() {
        showLoading(true);

        firestoreHelper.getAllSuggestedCrops(new FirestoreSuggestedCropsHelper.CropsCallback() {
            @Override
            public void onSuccess(List<Class_SuggestedCrop> crops) {
                allCropsList = crops;
                filteredCropsList = new ArrayList<>(crops);
                
                runOnUiThread(() -> {
                    showLoading(false);
                    
                    if (crops.isEmpty()) {
                        showEmptyState(true, "No suggested crops available");
                    } else {
                        showEmptyState(false, null);
                        setupListView();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showEmptyState(true, "Error loading crops");
                    Toast.makeText(Activity_SuggestedCrops.this,
                            "Failed to load suggested crops: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setupListView() {
        adapter = new Adapter_SuggestedCrop(this, filteredCropsList);
        cropsListView.setAdapter(adapter);
    }

    private void filterCropsByMonth(String selectedMonth) {
        if (allCropsList == null || allCropsList.isEmpty()) {
            return;
        }

        filteredCropsList.clear();

        if (selectedMonth.equals("All")) {
            // Show all crops
            filteredCropsList.addAll(allCropsList);
        } else {
            // Filter by selected month
            for (Class_SuggestedCrop crop : allCropsList) {
                if (crop.getMonth() != null && crop.getMonth().equals(selectedMonth)) {
                    filteredCropsList.add(crop);
                }
            }
        }

        // Update UI
        if (filteredCropsList.isEmpty()) {
            showEmptyState(true, "No crops found for " + selectedMonth);
        } else {
            showEmptyState(false, null);
        }

        // Update adapter
        if (adapter != null) {
            adapter.updateCropsList(filteredCropsList);
        }
    }

    private void showLoading(boolean show) {
        loadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        cropsListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show, String message) {
        emptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        cropsListView.setVisibility(show ? View.GONE : View.VISIBLE);
        
        if (show && message != null) {
            emptyStateText.setText(message);
        }
    }
}
