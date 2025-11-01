package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.List;

/**
 * Activity to display FAQ (Frequently Asked Questions)
 * for farmers using the app
 */
public class Activity_FAQ extends AppCompatActivity {
    
    private ExpandableListView expandableListView;
    private LinearLayout emptyView;
    private AdapterFAQ adapter;
    
    private List<String> categories;
    private HashMap<String, List<Class_FAQ>> faqData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Help & FAQ");
        }
        
        // Initialize views
        expandableListView = findViewById(R.id.expandable_list_faq);
        emptyView = findViewById(R.id.empty_view);
        
        // Load FAQ data
        loadFAQData();
        
        // Setup adapter
        setupAdapter();
        
        // Expand first category by default
        if (categories != null && !categories.isEmpty()) {
            expandableListView.expandGroup(0);
        }
    }
    
    private void loadFAQData() {
        categories = FAQDataProvider.getCategories();
        faqData = FAQDataProvider.getAllFAQs();
        
        // Show empty view if no data
        if (categories == null || categories.isEmpty()) {
            expandableListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            expandableListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
    
    private void setupAdapter() {
        if (categories != null && faqData != null) {
            adapter = new AdapterFAQ(this, categories, faqData);
            expandableListView.setAdapter(adapter);
            
            // Set group click listener (optional - for logging or analytics)
            expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                // Default behavior - expand/collapse
                return false;
            });
            
            // Set child click listener (optional - for future interactions)
            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                // Could add functionality like copying answer text, sharing, etc.
                return false;
            });
        }
    }
}
