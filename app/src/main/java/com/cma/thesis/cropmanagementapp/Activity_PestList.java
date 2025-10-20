package com.cma.thesis.cropmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Activity_PestList extends AppCompatActivity
{
    GridView gridView;
    ArrayList<Class_Pest> pestlist;
    Adapter_Pest pestlistadapter;
    ImageView imgcropimageupdate;
    public static Class_Functions function;
    String cat_id="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pestlist);

        gridView = (GridView) findViewById(R.id.cropGridViewPest);
        pestlist = new ArrayList<>();
        pestlistadapter = new Adapter_Pest(this,R.layout.activity_pest_items,pestlist);

        gridView.setAdapter(pestlistadapter);

        cat_id = getIntent().getStringExtra("category");


        loadPest();

        // action when clicking specific item on gridview
        gridView.setOnItemClickListener(onListViewClick);

    }

    private AdapterView.OnItemClickListener onListViewClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {
            Class_Pest pestset = pestlist.get(position);

            Intent intent = new Intent(getApplicationContext(),Activity_PestInformation.class);
            intent.putExtra("pestid",String.valueOf(pestset.getId()));
            startActivity(intent);
        }
    };

    private void loadPest() {
        pestlist.clear();
        Ipaddress address = new Ipaddress();

        String pestLink = address.ipaddress +"model/pestApi.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, pestLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject pest = array.getJSONObject(i);
                                int id = pest.getInt("id");
                                String pestname = pest.getString("pestname");
                                //String pesticide = pest.getString("pesticide");
                                String image = pest.getString("image");
                                pestlist.add(new Class_Pest(id,pestname,image));
                            }
                            pestlistadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Activity_PestList.this, "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
