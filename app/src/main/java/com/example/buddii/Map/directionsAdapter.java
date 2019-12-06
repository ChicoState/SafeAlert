package com.example.buddii.Map;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class directionsAdapter {

    private static final String TAG = "com.example.buddii.Map.directionsAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder{

        //DirectionImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //image = itemView.findViewBy
        }
    }
}