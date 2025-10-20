package com.cma.thesis.cropmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
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

public class Activity_PestInformation extends AppCompatActivity {

    TextView pest;
    TextView chemical;
    Class_DatabaseHelper api = new Class_DatabaseHelper(this);

    ListView lv;
    ArrayList<Class_PestChemical> pestchemicallist;
    Adapter_PestChemical adapterpestchemical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__pest_information);
        String pestid = getIntent().getStringExtra("pestid");

        //Toast.makeText(this, pestid, Toast.LENGTH_SHORT).show();

        pest = (TextView) findViewById(R.id.tvpest);



        String cropID = getIntent().getStringExtra("passedID");

        Ipaddress address = new Ipaddress();
        String pestLink = address.ipaddress + "model/petinfo_api.php?id="+pestid;

        api.loadPest(pestLink,cropID,pest,this);


        lv = (ListView) findViewById(R.id.listpestchemical);
        pestchemicallist = new ArrayList<>();
        adapterpestchemical = new Adapter_PestChemical(this, R.layout.list_pestchemical, pestchemicallist);
        lv.setAdapter(adapterpestchemical);
        generatepestCCrop(Integer.parseInt(pestid));

    }


    private void generatepestCCrop(int cropid) {
        pestchemicallist.clear();

        Ipaddress address = new Ipaddress();
        String planList = address.ipaddress + "model/pest_chemical_api.php?id="+cropid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, planList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject pestc = array.getJSONObject(i);


                                String pestchemical = pestc.getString("pestchemical");

                                pestchemicallist.add(new Class_PestChemical(pestchemical));
                            }
                            adapterpestchemical.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "error" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
