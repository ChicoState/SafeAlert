
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


//allows the list of friends to be adapted on to the xml with the
// recycler view
public class friendsAdapter extends RecyclerView.Adapter<friendsAdapter.ViewHolder>{

    private static final String TAG = "friendsAdapter";

    private ArrayList<String> fbuddiiImages = new ArrayList<>();
    private ArrayList<String> fbuddiiNames = new ArrayList<>();
    private Context fContext;

//sets member variables
    public friendsAdapter(Context friendContext, ArrayList<String> friendImages, ArrayList<String> friendNames) {

        fbuddiiImages = friendImages;
        fbuddiiNames = friendNames;
        fContext = friendContext;
    }

    //holds the recycler view and inflates the layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_buddis, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //binds the view
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
        //this allows the user to click on a friend and then
        //gets sent to the next activity
        holder.parentFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fContext, ScrollMapUser.class);
                intent.putExtra("friend",fbuddiiNames.get(position));
                fContext.startActivity(intent);
            }
        });
    }

    //gets how many items or in this case friends are in the
    //list and returns the size
    @Override
    public int getItemCount() {
        return fbuddiiNames.size();
    }
    //puts things into the recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parentFriend;
        ImageView friendImage;
        TextView friend;


        //holds the items or in this case friends within the recyclerview
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImage = itemView.findViewById(R.id.friendImage);
            friend = itemView.findViewById(R.id.userFriend);
            parentFriend = itemView.findViewById(R.id.parentFriend);
        }
    }

}
