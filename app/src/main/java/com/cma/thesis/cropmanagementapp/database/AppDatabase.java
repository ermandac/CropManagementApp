package com.cma.thesis.cropmanagementapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database configuration for local storage
 * Manages Plans and Comments locally on device
 * Will be extended to include cached Crop data
 */
@Database(entities = {PlanEntity.class, CommentEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "crop_management_db";
    private static volatile AppDatabase INSTANCE;
    
    /**
     * Get Plan DAO
     */
    public abstract PlanDao planDao();
    
    /**
     * Get Comment DAO
     */
    public abstract CommentDao commentDao();
    
    /**
     * Singleton pattern to get database instance
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration() // For development - remove in production
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * Close database instance
     */
    public static void destroyInstance() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }
}
