package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_fetilizer extends AppCompatActivity {

    ListView lv;
    ArrayList<Class_Fertilizer> allFertilizers;
    ArrayList<Class_Fertilizer> filteredFertilizers;
    Adapter_Fertilizer adapterfertilizer;
    FirestoreFertilizerHelper firestoreHelper;
    ProgressBar progressBar;
    LinearLayout emptyStateLayout;
    Spinner categorySpinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetilizer);

        // Set up ActionBar with title and back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Fertilizer Information");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Firestore helper
        firestoreHelper = new FirestoreFertilizerHelper();

        lv = findViewById(R.id.listfertilizer);
        allFertilizers = new ArrayList<>();
        filteredFertilizers = new ArrayList<>();
        adapterfertilizer = new Adapter_Fertilizer(this, R.layout.list_fertilizer_item_details, filteredFertilizers);
        lv.setAdapter(adapterfertilizer);

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        categorySpinner = findViewById(R.id.spinnerMonth);

        // Set up category filter spinner
        String[] arraySpinner = new String[] {
                "All", "Nitrogen", "Complete", "Organic", "Phosphorus", "Potassium", 
                "Foliar", "Secondary", "Micronutrient", "Specialty"
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = adapterView.getSelectedItem().toString();
                filterFertilizersByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        
        // Load all fertilizers once
        loadAllFertilizers();
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
     * Load all fertilizers from Firestore (called once)
     */
    private void loadAllFertilizers() {
        progressBar.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);

        firestoreHelper.getAllFertilizers(new FirestoreFertilizerHelper.FertilizerCallback() {
            @Override
            public void onSuccess(List<Class_Fertilizer> fertilizers) {
                progressBar.setVisibility(View.GONE);
                allFertilizers.clear();
                allFertilizers.addAll(fertilizers);
                
                // Apply current filter
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                filterFertilizersByCategory(selectedCategory);
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
                Toast.makeText(activity_fetilizer.this, 
                        "Error loading fertilizers: " + error, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Filter fertilizers by category (client-side filtering)
     */
    private void filterFertilizersByCategory(String category) {
        filteredFertilizers.clear();
        
        if (category.equals("All")) {
            filteredFertilizers.addAll(allFertilizers);
        } else {
            for (Class_Fertilizer fertilizer : allFertilizers) {
                if (fertilizer.getCategory() != null && 
                    fertilizer.getCategory().equalsIgnoreCase(category)) {
                    filteredFertilizers.add(fertilizer);
                }
            }
        }
        
        adapterfertilizer.notifyDataSetChanged();
        
        // Show/hide empty state
        if (filteredFertilizers.isEmpty()) {
            lv.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            lv.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

}
