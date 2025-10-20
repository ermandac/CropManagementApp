package com.cma.thesis.cropmanagementapp;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
    Ipaddress address = new Ipaddress();

    String insertLink = address.ipaddress + "model/insert_comment.php";

    Class_Comment commentclass;

    ListView listView;
    Adapter_Comment adapter_comment;
    String cropid = "";


    public static ArrayList<Class_Comment> commentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__comments);

        listView  = findViewById(R.id.listview_Comment);
        adapter_comment = new Adapter_Comment(this,commentArrayList);
        listView.setAdapter(adapter_comment);
        saveComment = (Button)findViewById(R.id.btnsavecomment);
        cropid = getIntent().getStringExtra("passedID");
        RetrieveComment(cropid);

        saveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertComment();
                RetrieveComment(cropid);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ID = String.valueOf(commentArrayList.get(i).getId());
                String CROPID = String.valueOf(commentArrayList.get(i).getCropId());
                String COMMENT = String.valueOf(commentArrayList.get(i).getComment());
                String DATE = String.valueOf(commentArrayList.get(i).getCommentDate());

                Intent intent = new Intent(getApplicationContext(),Activity_Reply.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_ID",ID);
                extras.putString("EXTRA_CROPID",CROPID);
                extras.putString("EXTRA_COMMENT",COMMENT);
                extras.putString("EXTRA_DATE",DATE);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }


    void RetrieveComment(String id)
    {
        Toast.makeText(Activity_Comments.this,id, Toast.LENGTH_SHORT).show();
        String url = address.ipaddress + "model/commentApi.php?id="+id;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    commentArrayList.clear();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if(success.equals("1"))
                        {
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("id");
                                String cropID = object.getString("cropID");
                                String comment = object.getString("comment");
                                String dateComment = object.getString("date_comment");


                                commentclass = new Class_Comment(Integer.parseInt(id),cropID,comment,dateComment);
                                commentArrayList.add(commentclass);
                                adapter_comment.notifyDataSetChanged();

                            }
                        }

                    } catch (JSONException e) {
                        Toast.makeText(Activity_Comments.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_Comments.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue((getApplicationContext()));
        queue.add(request);
    }

    void InsertComment()
    {
        commentText = (EditText)findViewById(R.id.etcomment);
        final String cropIDValue = getIntent().getStringExtra("passedID");
        final String commentValue = commentText.getText().toString().trim();
        final String dateValue = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


        StringRequest request = new StringRequest(Request.Method.POST, insertLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                commentText.setText("");
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> param = new HashMap<String,String>();
//                $crop_id = $_POST['cropid'];
//                $comment = $_POST['comment'];
//                $date = $_POST['date'];
                param.put("cropid",cropIDValue);
                param.put("comment",commentValue);
                param.put("date",dateValue);
                return param;
            }
        };

        RequestQueue queue = Volley.newRequestQueue((getApplicationContext()));
        queue.add(request);

        Intent intent= new Intent(this, Activity_Comments.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        adapter_comment.notifyDataSetChanged();
        listView.setAdapter(adapter_comment);
    }
}