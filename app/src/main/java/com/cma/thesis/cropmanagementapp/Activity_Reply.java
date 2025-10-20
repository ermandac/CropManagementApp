package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class Activity_Reply extends AppCompatActivity {

    TextView ID,CropID,Comment,DateCommented;
    Ipaddress address = new Ipaddress();
    String insertLink = address.ipaddress + "model/insert_reply.php";

    EditText replyText;

    Class_Reply replyClass;

    ListView listViewReply;
    Adapter_Reply adapter_reply;
    public static ArrayList<Class_Reply> replyArrayList = new ArrayList<>();
    Button saveReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__reply);

        listViewReply  = findViewById(R.id.listview_reply);
        adapter_reply = new Adapter_Reply(this,replyArrayList);
        listViewReply.setAdapter(adapter_reply);

        saveReply = (Button)findViewById(R.id.btnsaveReply);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String extraID = extras.getString("EXTRA_ID");
        String extraCropID = extras.getString("EXTRA_CROPID");
        String extraComment = extras.getString("EXTRA_COMMENT");
        String extraDate = extras.getString("EXTRA_DATE");

        Comment = (TextView) findViewById(R.id.tvCommentSubject);
        DateCommented = (TextView) findViewById(R.id.tvCommentSubjectDate);
        ID = (TextView) findViewById(R.id.tvSubjectID);
        CropID = (TextView) findViewById(R.id.tvSubjectCropid);

        ID.setVisibility(View.GONE);
        CropID.setVisibility(View.GONE);

        Comment.setText(extraComment);
        DateCommented.setText(extraDate);
        ID.setText(extraID);
        CropID.setText(extraCropID);


        RetrieveReply(extraID);


        //send reply
        saveReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert Reply
                InsertReply();
            }
        });
    }

    private void RetrieveReply(String id) {
        Ipaddress address = new Ipaddress();
        String url = address.ipaddress + "model/commentReplyapi.php?id="+id;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                replyArrayList.clear();
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
                            String commentID = object.getString("commentID");
                            String reply = object.getString("reply");
                            String date = object.getString("date");
                            String replyfrom = object.getString("replyfrom");

                            replyClass = new Class_Reply(Integer.parseInt(id),commentID,reply,date,replyfrom);

                            replyArrayList.add(replyClass);
                            adapter_reply.notifyDataSetChanged();

                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(Activity_Reply.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_Reply.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue((getApplicationContext()));
        queue.add(request);
    }

    void InsertReply()
    {
        replyText = (EditText)findViewById(R.id.txtreply);
        final String cropIDValue = ID.getText().toString();
        final String commentValue = replyText.getText().toString().trim();
        final String dateValue = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


        StringRequest request = new StringRequest(Request.Method.POST, insertLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                replyText.setText("");
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
                param.put("reply",commentValue);
                param.put("date",dateValue);
                return param;
            }
        };

        RequestQueue queue = Volley.newRequestQueue((getApplicationContext()));
        queue.add(request);

        Intent intent= new Intent(this, Activity_Comments.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        adapter_reply.notifyDataSetChanged();
        listViewReply.setAdapter(adapter_reply);
    }
}