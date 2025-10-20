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

public class Adapter_Comment  extends ArrayAdapter<Class_Comment> {
    Context context;
    List<Class_Comment> arrayListComment;


    public Adapter_Comment(@NonNull Context context,List<Class_Comment> arrayListComment ) {
        super(context, R.layout.custom_list_item_comment, arrayListComment);

        this.context = context;
        this.arrayListComment = arrayListComment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_comment,null,true);

        TextView tvId = view.findViewById(R.id.txt_comment_id);
        TextView tvComment = view.findViewById(R.id.txt_listcomment);

        String ID = String.valueOf(arrayListComment.get(position).getId());
        String Comment = arrayListComment.get(position).getComment();

        tvId.setText(ID);
        tvComment.setText(Comment);

        return view;
    }
}
