
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

import com.example.buddii.Map.UserView.ScrollMapUser;

import java.util.ArrayList;

public class friendsAdapter extends RecyclerView.Adapter<friendsAdapter.ViewHolder>{

    private static final String TAG = "friendsAdapter";

    private ArrayList<String> fbuddiiImages = new ArrayList<>();
    private ArrayList<String> fbuddiiNames = new ArrayList<>();
    private Context fContext;


    public friendsAdapter(Context friendContext, ArrayList<String> friendImages, ArrayList<String> friendNames) {

        fbuddiiImages = friendImages;
        fbuddiiNames = friendNames;
        fContext = friendContext;
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

        holder.friend.setText(fbuddiiNames.get(position));
        try {
            Uri imageUri = Uri.parse(fbuddiiImages.get(position));
            holder.friendImage.setImageURI(imageUri);
        }catch(Exception e)
        {
            //TODO: fix diaper pattern (anti-pattern)
        }


        holder.parentFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fContext, ScrollMapUser.class);
                intent.putExtra("userBuddii",fbuddiiNames.get(position));
                fContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fbuddiiNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parentFriend;
        ImageView friendImage;
        TextView friend;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImage = itemView.findViewById(R.id.friendImage);
            friend = itemView.findViewById(R.id.userFriend);
            parentFriend = itemView.findViewById(R.id.parentFriend);
        }
    }

}
