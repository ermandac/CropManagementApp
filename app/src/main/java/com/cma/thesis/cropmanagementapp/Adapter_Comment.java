package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adapter_Comment  extends ArrayAdapter<Class_Comment> {
    Context context;
    List<Class_Comment> arrayListComment;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Class_Comment comment, int position);
    }

    public Adapter_Comment(@NonNull Context context, List<Class_Comment> arrayListComment, 
                          OnDeleteClickListener deleteClickListener) {
        super(context, R.layout.custom_list_item_comment, arrayListComment);

        this.context = context;
        this.arrayListComment = arrayListComment;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_comment,null,true);

        TextView tvId = view.findViewById(R.id.txt_comment_id);
        TextView tvComment = view.findViewById(R.id.txt_listcomment);
        TextView tvDate = view.findViewById(R.id.txt_comment_date);
        ImageView btnDelete = view.findViewById(R.id.btn_delete_comment);

        Class_Comment comment = arrayListComment.get(position);
        String ID = String.valueOf(comment.getId());
        String Comment = comment.getComment();
        String Date = comment.getCommentDate();

        tvId.setText(ID);
        tvComment.setText(Comment);
        tvDate.setText(Date);

        // Delete button click listener
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(comment, position);
                }
            }
        });

        return view;
    }
}
