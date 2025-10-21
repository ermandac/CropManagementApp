package com.cma.thesis.cropmanagementapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Room Entity for storing comments locally
 * Replaces PHP API backend for private user comments
 */
@Entity(tableName = "comments")
public class CommentEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String cropId;
    private String comment;
    private String commentDate;
    private long createdAt;

    // Constructor
    public CommentEntity(String cropId, String comment, String commentDate, long createdAt) {
        this.cropId = cropId;
        this.comment = comment;
        this.commentDate = commentDate;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCropId() {
        return cropId;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
