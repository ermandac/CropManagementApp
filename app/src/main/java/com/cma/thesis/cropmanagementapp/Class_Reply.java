package com.cma.thesis.cropmanagementapp;

public class Class_Reply {

    int id;
    String commentID;
    String reply;
    String date;
    String replyfrom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReplyfrom() {
        return replyfrom;
    }

    public void setReplyfrom(String replyfrom) {
        this.replyfrom = replyfrom;
    }

    public Class_Reply(int id, String commentID, String reply, String date, String replyfrom) {
        this.id = id;
        this.commentID = commentID;
        this.reply = reply;
        this.date = date;
        this.replyfrom = replyfrom;
    }
}
