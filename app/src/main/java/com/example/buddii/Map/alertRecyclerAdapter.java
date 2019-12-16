package com.example.buddii.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.R;

import java.util.ArrayList;

public class alertRecyclerAdapter extends RecyclerView.Adapter<alertRecyclerAdapter.AlertViewHolder> {
    private ArrayList<alertItem> mAlertList;


    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_card, parent, false);
        AlertViewHolder evh = new AlertViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        alertItem currentItem = mAlertList.get(position);

//        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getmText2());

    }

    @Override
    public int getItemCount() {
        return mAlertList.size();
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder{

            public ImageView mImageView;
            public TextView mTextView1;
            public TextView mTextView2;


            public AlertViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.imageView);
                mTextView1 = itemView.findViewById(R.id.alertCardText);
                mTextView2 = itemView.findViewById(R.id.alertCardText2);
            }
    }

    public alertRecyclerAdapter(ArrayList<alertItem> alertList){
        mAlertList = alertList;
    }

}
