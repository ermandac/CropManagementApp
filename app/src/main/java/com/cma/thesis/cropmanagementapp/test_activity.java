package com.cma.thesis.cropmanagementapp;

import android.app.DownloadManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class test_activity extends AppCompatActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    String cropLink = "http://192.168.43.61/crop/model/api.php?id=1";

    //a list to store all the products
    List<testa> testaList;

    //the recyclerview
    RecyclerView recyclerView;

    RequestQueue request;
    TextView display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        request = Volley.newRequestQueue(this);
        display = (TextView)findViewById(R.id.txtdisplay);
        loadProducts();
    }

    private void loadProducts() {
        //Toast.makeText(test_activity.this, "labase", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, cropLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(test_activity.this, "test", Toast.LENGTH_SHORT).show();
                        try {


                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                //Toast.makeText(test_activity.this, "LOAD" + product.getString("crop_name"), Toast.LENGTH_SHORT).show();
                                display.setText(product.getString("crop_name"));
                            }

                            //creating adapter object and setting it to recyclerview
                            testadapter adapter = new testadapter(test_activity.this, testaList);
   //                         recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(test_activity.this, "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest); 
    }
}