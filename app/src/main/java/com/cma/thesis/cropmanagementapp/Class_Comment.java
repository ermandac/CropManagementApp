package com.cma.thesis.cropmanagementapp;

public class Class_Comment {
    public Class_Comment(int id, String cropId, String comment, String commentDate) {
        this.id = id;
        CropId = cropId;
        this.comment = comment;
        this.commentDate = commentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCropId() {
        return CropId;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    int id;
    String CropId;
    String comment;
    String commentDate;


}
