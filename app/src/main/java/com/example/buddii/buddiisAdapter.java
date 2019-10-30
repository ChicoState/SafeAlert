package com.example.buddii;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class buddiisAdapter extends RecyclerView.Adapter<buddiisAdapter.ViewHolder>{

    private static final String TAG = "buddiisAdapter";

    private ArrayList<String> mbuddiiImages = new ArrayList<>();
    private ArrayList<String> mbuddiiNames = new ArrayList<>();
    private Context mContext;


    public buddiisAdapter(Context context, ArrayList<String> buddiiImages, ArrayList<String> buddiiNames) {

        mbuddiiImages = buddiiImages;
        mbuddiiNames = buddiiNames;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_buddis, parent, false);
        ViewHolder holder = new ViewHolder(view);
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
            //TODO: fix diaper pattern (anti-pattern)
        }
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
