package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Manages Firebase Authentication for Admin Panel
 * Handles login, logout, and authentication state checks
 */
public class AdminAuthManager {
    
    private static final String TAG = "AdminAuthManager";
    private final FirebaseAuth mAuth;
    private final Context context;
    
    public AdminAuthManager(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
    }
    
    /**
     * Sign in admin with email and password
     */
    public void signIn(String email, String password, AuthCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            callback.onFailure("Email is required");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            callback.onFailure("Password is required");
            return;
        }
        
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Log.d(TAG, "signInWithEmail:success - " + user.getEmail());
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure("Authentication failed");
                            }
                        } else {
                            String errorMessage = task.getException() != null 
                                ? task.getException().getMessage() 
                                : "Authentication failed";
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            callback.onFailure(errorMessage);
                        }
                    }
                });
    }
    
    /**
     * Sign out current admin user
     */
    public void signOut() {
        mAuth.signOut();
        Log.d(TAG, "User signed out");
    }
    
    /**
     * Check if admin is currently authenticated
     */
    public boolean isAuthenticated() {
        return mAuth.getCurrentUser() != null;
    }
    
    /**
     * Get current authenticated user
     */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    
    /**
     * Get current user's email
     */
    public String getCurrentUserEmail() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
    
    /**
     * Callback interface for authentication operations
     */
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String errorMessage);
    }
}
