package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Admin Dashboard Activity
 * Displays all 8 crop categories with crop counts
 * Allows navigation to category-specific crop lists
 */
public class Activity_AdminDashboard extends AppCompatActivity {
    
    private AdminAuthManager authManager;
    private FirebaseFirestore db;
    
    private TextView tvWelcomeMessage;
    private ProgressBar progressBar;
    
    private TextView tvVegetablesCount;
    private TextView tvFruitsCount;
    private TextView tvPulsesCount;
    private TextView tvSpicesCount;
    private TextView tvPlantationCount;
    private TextView tvMedicinalCount;
    private TextView tvOrganicCount;
    private TextView tvPestCount;
    
    private CardView cardVegetables;
    private CardView cardFruits;
    private CardView cardPulses;
    private CardView cardSpices;
    private CardView cardPlantation;
    private CardView cardMedicinal;
    private CardView cardOrganic;
    private CardView cardPest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        
        // Initialize auth manager
        authManager = new AdminAuthManager(this);
        
        // Check authentication
        if (!authManager.isAuthenticated()) {
            navigateToLogin();
            return;
        }
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Initialize views
        initViews();
        
        // Set welcome message
        String email = authManager.getCurrentUserEmail();
        if (email != null) {
            tvWelcomeMessage.setText("Welcome, " + email);
        }
        
        // Set up category card click listeners
        setupCategoryClickListeners();
        
        // Load crop counts
        loadCropCounts();
    }
    
    private void initViews() {
        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        progressBar = findViewById(R.id.progressBar);
        
        tvVegetablesCount = findViewById(R.id.tvVegetablesCount);
        tvFruitsCount = findViewById(R.id.tvFruitsCount);
        tvPulsesCount = findViewById(R.id.tvPulsesCount);
        tvSpicesCount = findViewById(R.id.tvSpicesCount);
        tvPlantationCount = findViewById(R.id.tvPlantationCount);
        tvMedicinalCount = findViewById(R.id.tvMedicinalCount);
        tvOrganicCount = findViewById(R.id.tvOrganicCount);
        tvPestCount = findViewById(R.id.tvPestCount);
        
        cardVegetables = findViewById(R.id.cardVegetables);
        cardFruits = findViewById(R.id.cardFruits);
        cardPulses = findViewById(R.id.cardPulses);
        cardSpices = findViewById(R.id.cardSpices);
        cardPlantation = findViewById(R.id.cardPlantation);
        cardMedicinal = findViewById(R.id.cardMedicinal);
        cardOrganic = findViewById(R.id.cardOrganic);
        cardPest = findViewById(R.id.cardPest);
    }
    
    private void setupCategoryClickListeners() {
        cardVegetables.setOnClickListener(v -> openCropList("2", "Vegetables"));
        cardFruits.setOnClickListener(v -> openCropList("1", "Fruits"));
        cardPulses.setOnClickListener(v -> openCropList("3", "Pulses"));
        cardSpices.setOnClickListener(v -> openCropList("4", "Spices"));
        cardPlantation.setOnClickListener(v -> openCropList("5", "Plantation Crops"));
        cardMedicinal.setOnClickListener(v -> openCropList("6", "Medicinal Plants"));
        cardOrganic.setOnClickListener(v -> openCropList("7", "Organic Farming"));
        cardPest.setOnClickListener(v -> openCropList("8", "Pest Control"));
    }
    
    private void loadCropCounts() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Load count for each category
        loadCategoryCount("2", tvVegetablesCount);
        loadCategoryCount("1", tvFruitsCount);
        loadCategoryCount("3", tvPulsesCount);
        loadCategoryCount("4", tvSpicesCount);
        loadCategoryCount("5", tvPlantationCount);
        loadCategoryCount("6", tvMedicinalCount);
        loadCategoryCount("7", tvOrganicCount);
        loadCategoryCount("8", tvPestCount);
    }
    
    private void loadCategoryCount(String categoryId, TextView countView) {
        // Convert categoryId to integer for Firestore query
        int categoryIdInt = Integer.parseInt(categoryId);
        
        db.collection("crops")
                .whereEqualTo("categoryId", categoryIdInt)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    String countText = count + (count == 1 ? " crop" : " crops");
                    countView.setText(countText);
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    countView.setText("Error");
                    progressBar.setVisibility(View.GONE);
                });
    }
    
    private void openCropList(String categoryId, String categoryName) {
        Intent intent = new Intent(this, Activity_AdminCropList.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_dashboard, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            navigateToHome();
            return true;
        }
        
        if (id == R.id.action_logout) {
            showLogoutConfirmation();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        navigateToHome();
    }
    
    private void navigateToHome() {
        Intent intent = new Intent(this, Activity_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    
    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    authManager.signOut();
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(this, Activity_AdminLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload counts when returning to dashboard
        loadCropCounts();
    }
}
