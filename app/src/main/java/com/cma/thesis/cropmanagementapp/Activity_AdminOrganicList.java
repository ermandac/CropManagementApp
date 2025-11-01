package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin activity to list and manage organic farming items
 */
public class Activity_AdminOrganicList extends AppCompatActivity implements AdapterAdminOrganic.OnItemActionListener {
    
    private ListView listView;
    private LinearLayout emptyView;
    private EditText searchField;
    private TextView tvItemCount;
    private Button btnFilterAll, btnFilterMethod, btnFilterMaterial;
    private FloatingActionButton fabAdd;
    
    private AdapterAdminOrganic adapter;
    private FirestoreOrganicFarmingHelper firestoreHelper;
    private List<Class_OrganicFarming> organicList;
    
    private String currentFilter = "All";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_organic_list);
        
        // Initialize Firestore helper
        firestoreHelper = new FirestoreOrganicFarmingHelper();
        organicList = new ArrayList<>();
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        initViews();
        
        // Load data
        loadOrganicItems();
    }
    
    private void initViews() {
        listView = findViewById(R.id.list_organic);
        emptyView = findViewById(R.id.empty_view);
        searchField = findViewById(R.id.search_organic);
        tvItemCount = findViewById(R.id.tv_item_count);
        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterMethod = findViewById(R.id.btn_filter_method);
        btnFilterMaterial = findViewById(R.id.btn_filter_material);
        fabAdd = findViewById(R.id.fab_add_organic);
        
        // Setup search
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                    updateItemCount();
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Setup filter buttons
        btnFilterAll.setOnClickListener(v -> applyFilter("All", btnFilterAll));
        btnFilterMethod.setOnClickListener(v -> applyFilter("Farming Method", btnFilterMethod));
        btnFilterMaterial.setOnClickListener(v -> applyFilter("Material", btnFilterMaterial));
        
        // Setup FAB
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_AdminOrganicForm.class);
            startActivity(intent);
        });
    }
    
    private void loadOrganicItems() {
        firestoreHelper.getAllOrganicFarming(new FirestoreOrganicFarmingHelper.OrganicFarmingCallback() {
            @Override
            public void onSuccess(List<Class_OrganicFarming> items) {
                organicList = items;
                setupAdapter();
                updateItemCount();
                
                if (items.isEmpty()) {
                    showEmptyView();
                } else {
                    showListView();
                }
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(Activity_AdminOrganicList.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                showEmptyView();
            }
        });
    }
    
    private void setupAdapter() {
        adapter = new AdapterAdminOrganic(this, organicList, this);
        listView.setAdapter(adapter);
    }
    
    private void applyFilter(String filterType, Button selectedButton) {
        currentFilter = filterType;
        
        // Update button styles
        resetFilterButtons();
        selectedButton.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        selectedButton.setTextColor(0xFFFFFFFF);
        
        // Apply filter
        if (adapter != null) {
            adapter.filterByType(filterType);
            updateItemCount();
        }
    }
    
    private void resetFilterButtons() {
        btnFilterAll.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        btnFilterAll.setTextColor(0xFF666666);
        btnFilterMethod.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        btnFilterMethod.setTextColor(0xFF666666);
        btnFilterMaterial.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        btnFilterMaterial.setTextColor(0xFF666666);
    }
    
    private void updateItemCount() {
        if (adapter != null) {
            int count = adapter.getCount();
            tvItemCount.setText(count + (count == 1 ? " item" : " items"));
        }
    }
    
    private void showListView() {
        listView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
    
    private void showEmptyView() {
        listView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onEdit(Class_OrganicFarming item) {
        Intent intent = new Intent(this, Activity_AdminOrganicForm.class);
        intent.putExtra("ORGANIC_ID", item.getId());
        intent.putExtra("MODE", "EDIT");
        startActivity(intent);
    }
    
    @Override
    public void onDelete(Class_OrganicFarming item) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Organic Item")
            .setMessage("Are you sure you want to delete '" + item.getName() + "'?")
            .setPositiveButton("Delete", (dialog, which) -> {
                deleteItem(item);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void deleteItem(Class_OrganicFarming item) {
        firestoreHelper.deleteOrganicFarming(item.getId(), new FirestoreOrganicFarmingHelper.SingleItemCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(Activity_AdminOrganicList.this, message, Toast.LENGTH_SHORT).show();
                loadOrganicItems(); // Reload list
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(Activity_AdminOrganicList.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadOrganicItems(); // Reload when returning from form
    }
}
