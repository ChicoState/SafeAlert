package com.example.buddii;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.Map.BuddiiView.ScrollMapBuddii;

import java.util.ArrayList;


//Class used to display the users that can be chosen
public class beBuddiiAdapter extends RecyclerView.Adapter<beBuddiiAdapter.ViewHolder> {

    private static final String TAG = "buddiiAdapter";

    private ArrayList<String> mbuddiiImages = new ArrayList<>();
    private ArrayList<String> mbuddiiNames = new ArrayList<>();
    private Context mContext;


    beBuddiiAdapter(Context context, ArrayList<String> buddiiImages, ArrayList<String> buddiiNames) {

        mbuddiiImages = buddiiImages;
        mbuddiiNames = buddiiNames;
        mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_buddis, parent, false);
        beBuddiiAdapter.ViewHolder holder = new beBuddiiAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.userBuddii.setText(mbuddiiNames.get(position));
        try {
            Uri imageUri = Uri.parse(mbuddiiImages.get(position));
            holder.buddiiImage.setImageURI(imageUri);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        holder.parent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ScrollMapBuddii.class);
                intent.putExtra("userBuddii",mbuddiiNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mbuddiiNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parent;
        ImageView buddiiImage;
        TextView userBuddii;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buddiiImage = itemView.findViewById(R.id.buddiiImage);
            userBuddii = itemView.findViewById(R.id.userBuddii);
            parent = itemView.findViewById(R.id.parent);
        }
    }


}
