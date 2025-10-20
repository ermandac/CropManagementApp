package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter_Reply extends ArrayAdapter<Class_Reply> {
    Context context;
    List<Class_Reply> arrayListReply;


    public Adapter_Reply(@NonNull Context context, List<Class_Reply> arrayListReply ) {
        super(context, R.layout.custom_list_item_reply, arrayListReply);

        this.context = context;
        this.arrayListReply = arrayListReply;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_reply,null,true);

        TextView Reply = view.findViewById(R.id.tvreplycontent);
        TextView Date = view.findViewById(R.id.tvreplydate);
        TextView ReplyFrom = view.findViewById(R.id.tvreplyfrom);



        String replyValue = String.valueOf(arrayListReply.get(position).getReply());
        String DateValue = arrayListReply.get(position).getDate();
        String ReplyFromValue = arrayListReply.get(position).getReplyfrom();


        Reply.setText(replyValue);
        Date.setText(DateValue);
        ReplyFrom.setText(ReplyFromValue);

        ReplyFrom.setVisibility(View.GONE);

        return view;
    }
}
