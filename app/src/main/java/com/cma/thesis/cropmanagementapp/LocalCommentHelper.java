package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.cma.thesis.cropmanagementapp.database.AppDatabase;
import com.cma.thesis.cropmanagementapp.database.CommentDao;
import com.cma.thesis.cropmanagementapp.database.CommentEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * LocalCommentHelper
 * Handles local Room database operations for comments
 * Replaces PHP API backend for local-first storage
 */
public class LocalCommentHelper {
    private static final String TAG = "LocalCommentHelper";
    private CommentDao commentDao;
    private Context context;

    public LocalCommentHelper(Context context) {
        this.context = context.getApplicationContext();
        this.commentDao = AppDatabase.getInstance(context).commentDao();
    }

    /**
     * Create a new comment in local database
     */
    public void createComment(String cropId, String comment, String commentDate,
                             CommentCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    long createdAt = System.currentTimeMillis();
                    
                    CommentEntity commentEntity = new CommentEntity(
                        cropId, comment, commentDate, createdAt
                    );
                    
                    long id = commentDao.insert(commentEntity);
                    commentEntity.setId((int) id);
                    
                    Log.d(TAG, "Comment created successfully: " + id);
                    return new Result(true, "Comment created successfully", null, commentEntity);
                } catch (Exception e) {
                    Log.e(TAG, "Error creating comment: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage(), null);
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Load all comments for a specific crop
     */
    public void loadCommentsByCropId(String cropId, CommentLoadCallback callback) {
        new AsyncTask<Void, Void, CommentsResult>() {
            @Override
            protected CommentsResult doInBackground(Void... voids) {
                try {
                    List<CommentEntity> entities = commentDao.getCommentsByCropId(cropId);
                    ArrayList<Class_Comment> comments = new ArrayList<>();
                    
                    for (CommentEntity entity : entities) {
                        Class_Comment comment = entityToComment(entity);
                        comments.add(comment);
                    }
                    
                    Log.d(TAG, "Loaded " + comments.size() + " comments for crop: " + cropId);
                    return new CommentsResult(true, comments, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading comments: " + e.getMessage());
                    return new CommentsResult(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(CommentsResult result) {
                if (result.success) {
                    callback.onSuccess(result.comments);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Get all comments regardless of crop
     */
    public void getAllComments(CommentLoadCallback callback) {
        new AsyncTask<Void, Void, CommentsResult>() {
            @Override
            protected CommentsResult doInBackground(Void... voids) {
                try {
                    List<CommentEntity> entities = commentDao.getAllComments();
                    ArrayList<Class_Comment> comments = new ArrayList<>();
                    
                    for (CommentEntity entity : entities) {
                        Class_Comment comment = entityToComment(entity);
                        comments.add(comment);
                    }
                    
                    Log.d(TAG, "Loaded " + comments.size() + " total comments");
                    return new CommentsResult(true, comments, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading all comments: " + e.getMessage());
                    return new CommentsResult(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(CommentsResult result) {
                if (result.success) {
                    callback.onSuccess(result.comments);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Update an existing comment
     */
    public void updateComment(int commentId, String comment, String commentDate,
                             CommentCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    CommentEntity entity = commentDao.getCommentById(commentId);
                    if (entity != null) {
                        entity.setComment(comment);
                        entity.setCommentDate(commentDate);
                        commentDao.update(entity);
                        Log.d(TAG, "Comment updated successfully: " + commentId);
                        return new Result(true, "Comment updated successfully", null, null);
                    } else {
                        return new Result(false, null, "Comment not found", null);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error updating comment: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage(), null);
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Delete a comment
     */
    public void deleteComment(int commentId, CommentCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    commentDao.deleteById(commentId);
                    Log.d(TAG, "Comment deleted successfully: " + commentId);
                    return new Result(true, "Comment deleted successfully", null, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting comment: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage(), null);
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Delete all comments for a specific crop
     */
    public void deleteCommentsByCropId(String cropId, CommentCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    commentDao.deleteCommentsByCropId(cropId);
                    Log.d(TAG, "Comments deleted for crop: " + cropId);
                    return new Result(true, "Comments deleted successfully", null, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting comments: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage(), null);
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Get comment count for a crop
     */
    public void getCommentCount(String cropId, CountCallback callback) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    return commentDao.getCommentsCountByCropId(cropId);
                } catch (Exception e) {
                    Log.e(TAG, "Error getting comment count: " + e.getMessage());
                    return 0;
                }
            }

            @Override
            protected void onPostExecute(Integer count) {
                callback.onCount(count);
            }
        }.execute();
    }

    /**
     * Convert CommentEntity to Class_Comment
     */
    private Class_Comment entityToComment(CommentEntity entity) {
        return new Class_Comment(
            entity.getId(),
            entity.getCropId(),
            entity.getComment(),
            entity.getCommentDate()
        );
    }

    /**
     * Result wrapper for single operations
     */
    private static class Result {
        boolean success;
        String message;
        String error;
        CommentEntity comment;

        Result(boolean success, String message, String error, CommentEntity comment) {
            this.success = success;
            this.message = message;
            this.error = error;
            this.comment = comment;
        }
    }

    /**
     * Result wrapper for comment list operations
     */
    private static class CommentsResult {
        boolean success;
        ArrayList<Class_Comment> comments;
        String error;

        CommentsResult(boolean success, ArrayList<Class_Comment> comments, String error) {
            this.success = success;
            this.comments = comments;
            this.error = error;
        }
    }

    /**
     * Callback interface for comment operations (create, update, delete)
     */
    public interface CommentCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    /**
     * Callback interface for comment loading operations
     */
    public interface CommentLoadCallback {
        void onSuccess(ArrayList<Class_Comment> comments);
        void onError(String error);
    }

    /**
     * Callback interface for count operations
     */
    public interface CountCallback {
        void onCount(int count);
    }
}
