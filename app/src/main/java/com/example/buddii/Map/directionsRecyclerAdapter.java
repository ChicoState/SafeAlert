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

public class directionsRecyclerAdapter extends RecyclerView.Adapter<directionsRecyclerAdapter.DirectionsViewHolder> {
    private ArrayList<directionItem> mDirectionList;


    @NonNull
    @Override
    public DirectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.direction_card, parent, false);
        DirectionsViewHolder evh = new DirectionsViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull DirectionsViewHolder holder, int position) {
        directionItem currentItem = mDirectionList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getmText2());

    }

    @Override
    public int getItemCount() {
        return mDirectionList.size();
    }

    public static class DirectionsViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;


        public DirectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.directionCardText);
            mTextView2 = itemView.findViewById(R.id.directionCardText2);
        }
    }

    public directionsRecyclerAdapter(ArrayList<directionItem> directionList){
        mDirectionList = directionList;
    }


}
