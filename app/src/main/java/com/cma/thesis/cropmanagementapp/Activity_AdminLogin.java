package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

/**
 * Admin Login Activity
 * Handles authentication for admin panel access
 */
public class Activity_AdminLogin extends AppCompatActivity {
    
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvErrorMessage;
    
    private AdminAuthManager authManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        
        // Initialize views
        initViews();
        
        // Initialize auth manager
        authManager = new AdminAuthManager(this);
        
        // Check if already authenticated
        if (authManager.isAuthenticated()) {
            navigateToDashboard();
            return;
        }
        
        // Set up login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }
    
    private void initViews() {
        etEmail = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
        btnLogin = findViewById(R.id.btnAdminLogin);
        progressBar = findViewById(R.id.progressBar);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
    }
    
    private void handleLogin() {
        // Hide error message
        tvErrorMessage.setVisibility(View.GONE);
        
        // Get input values
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        
        // Validate input
        if (email.isEmpty()) {
            showError("Please enter your email");
            etEmail.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        
        // Check internet connection
        if (!isNetworkAvailable()) {
            showError("No internet connection. Please check your network settings.");
            return;
        }
        
        // Show loading state
        setLoading(true);
        
        // Attempt login
        authManager.signIn(email, password, new AdminAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                setLoading(false);
                Toast.makeText(Activity_AdminLogin.this, 
                    "Welcome, " + user.getEmail(), 
                    Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }
            
            @Override
            public void onFailure(String errorMessage) {
                setLoading(false);
                showError(errorMessage);
            }
        });
    }
    
    private void setLoading(boolean loading) {
        if (loading) {
            btnLogin.setEnabled(false);
            btnLogin.setAlpha(0.6f);
            progressBar.setVisibility(View.VISIBLE);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);
        } else {
            btnLogin.setEnabled(true);
            btnLogin.setAlpha(1.0f);
            progressBar.setVisibility(View.GONE);
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
        }
    }
    
    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    
    private void navigateToDashboard() {
        Intent intent = new Intent(Activity_AdminLogin.this, Activity_AdminDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // Allow back navigation to return to home screen
        super.onBackPressed();
    }
}
