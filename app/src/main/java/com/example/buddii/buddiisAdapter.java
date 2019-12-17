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


//allows the list of buddiis to be adapted on to the xml with the
//recycler view
public class buddiisAdapter extends RecyclerView.Adapter<buddiisAdapter.ViewHolder>{

    private static final String TAG = "buddiisAdapter";

    private ArrayList<String> mbuddiiImages = new ArrayList<>();
    private ArrayList<String> mbuddiiNames = new ArrayList<>();
    private Context mContext;

    //sets member variables
    public buddiisAdapter(Context context, ArrayList<String> buddiiImages, ArrayList<String> buddiiNames) {

        mbuddiiImages = buddiiImages;
        mbuddiiNames = buddiiNames;
        mContext = context;
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

        holder.userBuddii.setText(mbuddiiNames.get(position));
        try {
            Uri imageUri = Uri.parse(mbuddiiImages.get(position));
            holder.buddiiImage.setImageURI(imageUri);
        }catch(Exception e)
        {
            //TODO: fix diaper pattern (anti-pattern)
        }

        //this allows the user to click on a buddii and then
        //gets sent to the next activity
        holder.parent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ScrollMapUser.class);
                //  intent.putExtra("buddiiImage",mbuddiiImages.get(position));
                intent.putExtra("userBuddii",mbuddiiNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    //gets how many items or in this case buddiis are in the
    //list and returns the size
    @Override
    public int getItemCount() {
        return mbuddiiNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parent;
        ImageView buddiiImage;
        TextView userBuddii;

        //holds the items or in this case buddiis within the recyclerview
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buddiiImage = itemView.findViewById(R.id.buddiiImage);
            userBuddii = itemView.findViewById(R.id.userBuddii);
            parent = itemView.findViewById(R.id.parent);
        }
    }

}
