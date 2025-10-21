package com.cma.thesis.cropmanagementapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_Comments extends AppCompatActivity {

    EditText commentText;
    Button saveComment;

    Class_Comment commentclass;

    ListView listView;
    Adapter_Comment adapter_comment;
    String cropid = "";
    LocalCommentHelper localCommentHelper;

    public static ArrayList<Class_Comment> commentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__comments);

        listView  = findViewById(R.id.listview_Comment);
        adapter_comment = new Adapter_Comment(this, commentArrayList, 
            new Adapter_Comment.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(Class_Comment comment, int position) {
                    showDeleteConfirmation(comment, position);
                }
            });
        listView.setAdapter(adapter_comment);
        saveComment = (Button)findViewById(R.id.btnsavecomment);
        cropid = getIntent().getStringExtra("passedID");
        
        // Initialize local comment helper
        localCommentHelper = new LocalCommentHelper(this);
        
        RetrieveComment(cropid);

        saveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertComment();
            }
        });

        // Comment click listener removed - comments are local-only personal notes
        // No need for reply functionality in local storage mode
    }


    void RetrieveComment(String id)
    {
        commentArrayList.clear();
        
        localCommentHelper.loadCommentsByCropId(id, new LocalCommentHelper.CommentLoadCallback() {
            @Override
            public void onSuccess(ArrayList<Class_Comment> comments) {
                commentArrayList.clear();
                commentArrayList.addAll(comments);
                adapter_comment.notifyDataSetChanged();
                Log.d("Comments", "Loaded " + comments.size() + " comments");
            }

            @Override
            public void onError(String error) {
                Log.e("Comments", "Error loading comments: " + error);
                Toast.makeText(Activity_Comments.this, 
                    "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void InsertComment()
    {
        commentText = (EditText)findViewById(R.id.etcomment);
        final String cropIDValue = getIntent().getStringExtra("passedID");
        final String commentValue = commentText.getText().toString().trim();
        final String dateValue = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        if (commentValue.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        localCommentHelper.createComment(cropIDValue, commentValue, dateValue, 
            new LocalCommentHelper.CommentCallback() {
                @Override
                public void onSuccess(String message) {
                    commentText.setText("");
                    Toast.makeText(Activity_Comments.this, 
                        "Comment added successfully", Toast.LENGTH_SHORT).show();
                    
                    // Reload comments
                    RetrieveComment(cropIDValue);
                }

                @Override
                public void onError(String error) {
                    Log.e("Comments", "Error creating comment: " + error);
                    Toast.makeText(Activity_Comments.this, 
                        "Failed to add comment: " + error, Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showDeleteConfirmation(final Class_Comment comment, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?\n\n" + comment.getComment());
        
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteComment(comment, position);
            }
        });
        
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        builder.show();
    }

    private void deleteComment(final Class_Comment comment, final int position) {
        localCommentHelper.deleteComment(comment.getId(), new LocalCommentHelper.CommentCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(Activity_Comments.this, 
                    "Note deleted successfully", Toast.LENGTH_SHORT).show();
                
                // Remove from list and update adapter
                commentArrayList.remove(position);
                adapter_comment.notifyDataSetChanged();
                
                Log.d("Comments", "Comment deleted: " + comment.getId());
            }

            @Override
            public void onError(String error) {
                Log.e("Comments", "Error deleting comment: " + error);
                Toast.makeText(Activity_Comments.this, 
                    "Failed to delete note: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
