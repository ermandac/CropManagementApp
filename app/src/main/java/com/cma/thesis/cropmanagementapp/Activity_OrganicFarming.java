package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_OrganicFarming extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private Spinner typeFilterSpinner;
    private Adapter_OrganicFarming adapter;
    private List<Class_OrganicFarming> allOrganicItems;
    private List<Class_OrganicFarming> filteredOrganicItems;
    private FirestoreOrganicFarmingHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organic_farming);

        // Set up ActionBar with title and back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Organic Farming");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Firestore helper
        firestoreHelper = new FirestoreOrganicFarmingHelper();

        // Initialize views
        listView = findViewById(R.id.organicListView);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        typeFilterSpinner = findViewById(R.id.typeFilterSpinner);

        // Initialize lists
        allOrganicItems = new ArrayList<>();
        filteredOrganicItems = new ArrayList<>();

        // Set up adapter
        adapter = new Adapter_OrganicFarming(this, R.layout.list_organic_farming_item, filteredOrganicItems);
        listView.setAdapter(adapter);

        // Set up type filter spinner
        String[] filterTypes = new String[] {
                "All", "Farming Method", "Material"
        };

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filterTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeFilterSpinner.setAdapter(spinnerAdapter);

        typeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getSelectedItem().toString();
                filterOrganicItemsByType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Load all organic farming items once
        loadAllOrganicItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button press
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load all organic farming items from Firestore (called once)
     */
    private void loadAllOrganicItems() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);

        firestoreHelper.getAllOrganicFarming(new FirestoreOrganicFarmingHelper.OrganicFarmingCallback() {
            @Override
            public void onSuccess(List<Class_OrganicFarming> items) {
                progressBar.setVisibility(View.GONE);
                allOrganicItems.clear();
                allOrganicItems.addAll(items);

                // Apply current filter
                String selectedType = typeFilterSpinner.getSelectedItem().toString();
                filterOrganicItemsByType(selectedType);
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
                Toast.makeText(Activity_OrganicFarming.this,
                        "Error loading organic farming items: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Filter organic farming items by type (client-side filtering)
     */
    private void filterOrganicItemsByType(String type) {
        filteredOrganicItems.clear();

        if (type.equals("All")) {
            filteredOrganicItems.addAll(allOrganicItems);
        } else {
            for (Class_OrganicFarming item : allOrganicItems) {
                if (item.getType() != null && item.getType().equalsIgnoreCase(type)) {
                    filteredOrganicItems.add(item);
                }
            }
        }

        adapter.notifyDataSetChanged();

        // Show/hide empty state
        if (filteredOrganicItems.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}
