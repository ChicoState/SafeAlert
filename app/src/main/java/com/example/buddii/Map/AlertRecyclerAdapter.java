package com.example.buddii.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.R;

public class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.AlertViewHolder> {
    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertRecyclerAdapter.AlertViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        public static class AlertViewHolder extends RecyclerView.ViewHolder{

            public ImageView mImageView;
            public TextView mTextView1;
            public TextView mTextView2;


            public AlertViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.imageView);
                mTextView1 = itemView.findViewById(R.id.directionCardText);
                mTextView2 = itemView.findViewById(R.id.directionCardText2);
            }
        }

        return 0;
    }
}
