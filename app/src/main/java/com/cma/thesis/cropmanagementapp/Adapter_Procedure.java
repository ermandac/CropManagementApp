package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mimoy on 4/27/2018.
 */

public class Adapter_Procedure extends BaseAdapter {

    public static int counter = 0;
    public static int donecounter = 0;
    String cropID;
    int counterAll = 0;


    Ipaddress address = new Ipaddress();
    String insertLink = address.ipaddress + "crop/model/update_procedure.php";


    public Adapter_Procedure(Context context, int layout, ArrayList<Class_Procedure> procedure) {
        this.context = context;
        this.layout = layout;
        this.procedurelist = procedure;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_Procedure> procedurelist;

    @Override
    public int getCount() {
        return procedurelist.size();
    }

    @Override
    public Object getItem(int position) {
        return procedurelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder vh = new ViewHolder();
    View row = null;
    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final Class_Procedure procedure = procedurelist.get(position);
        counter = getCount();

        for (int i = 0; i < getCount() ; i++) {
            if(procedure.getStatus().equalsIgnoreCase("done"))
            {

            }
        }

        row = view;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.procedure = (TextView) row.findViewById(R.id.txtprocedure);
            vh.datenotif = (TextView) row.findViewById(R.id.txtdatenotif);
            vh.procedureid = (TextView) row.findViewById(R.id.txtprocedureid);
            vh.cropid = (TextView)row.findViewById(R.id.txtcropIDp);
            vh.btnset = (Button) row.findViewById(R.id.btnsetAlarm);

            for (int i = 0; i < getCount() ; i++) {
                if(procedure.getStatus().equalsIgnoreCase("done"))
                {
                    vh.procedure.setBackgroundColor(Color.GREEN);
                    vh.datenotif.setBackgroundColor(Color.GREEN);

                    vh.btnset.setText("DONE");
                    vh.btnset.setEnabled(false);
                }
            }

            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();


        }

        vh.procedureid.setText(String.valueOf(procedure.getId()));
        vh.cropid.setText(String.valueOf(procedure.getCrop_id()));
        vh.procedure.setText(procedure.getProcedure());
        vh.datenotif.setText(procedure.getdatenofication());
        return row;

    }

    public class ViewHolder {
        TextView procedure;
        TextView datenotif;
        TextView procedureid;
        TextView cropid;
        Button btnset;

    }

    public void updateProcedureStatus(String insertLink,final String id,final String cropid)
    {
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        // Showing response message coming from server.
                        //Toast.makeText(context, ServerResponse + ""+  id + "" + cropid, Toast.LENGTH_LONG  ).show();
                        notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(context, volleyError.networkResponse.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("cropid",cropid);
                params.put("id", id);
                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        notifyDataSetChanged();
    }
}
