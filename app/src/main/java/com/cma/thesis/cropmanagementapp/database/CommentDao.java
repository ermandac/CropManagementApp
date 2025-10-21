package com.cma.thesis.cropmanagementapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

/**
 * Data Access Object for Comment operations
 * Provides CRUD operations for local comment storage
 */
@Dao
public interface CommentDao {
    
    /**
     * Insert a new comment
     */
    @Insert
    long insert(CommentEntity comment);
    
    /**
     * Update an existing comment
     */
    @Update
    void update(CommentEntity comment);
    
    /**
     * Delete a comment
     */
    @Delete
    void delete(CommentEntity comment);
    
    /**
     * Delete a comment by ID
     */
    @Query("DELETE FROM comments WHERE id = :commentId")
    void deleteById(int commentId);
    
    /**
     * Get all comments
     */
    @Query("SELECT * FROM comments ORDER BY createdAt DESC")
    List<CommentEntity> getAllComments();
    
    /**
     * Get comments for a specific crop
     */
    @Query("SELECT * FROM comments WHERE cropId = :cropId ORDER BY createdAt DESC")
    List<CommentEntity> getCommentsByCropId(String cropId);
    
    /**
     * Get a single comment by ID
     */
    @Query("SELECT * FROM comments WHERE id = :commentId LIMIT 1")
    CommentEntity getCommentById(int commentId);
    
    /**
     * Get count of comments for a crop
     */
    @Query("SELECT COUNT(*) FROM comments WHERE cropId = :cropId")
    int getCommentsCountByCropId(String cropId);
    
    /**
     * Get count of all comments
     */
    @Query("SELECT COUNT(*) FROM comments")
    int getCommentsCount();
    
    /**
     * Delete all comments for a specific crop
     */
    @Query("DELETE FROM comments WHERE cropId = :cropId")
    void deleteCommentsByCropId(String cropId);
    
    /**
     * Delete all comments (for testing/reset)
     */
    @Query("DELETE FROM comments")
    void deleteAllComments();
}
