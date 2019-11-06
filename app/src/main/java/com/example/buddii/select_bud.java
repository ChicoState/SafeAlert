package com.example.buddii;


import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class select_bud extends Activity
{
    private ArrayList<String> mbuddiiNames = new ArrayList<>();
    private ArrayList<String> mbuddiiImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_buds_list);
        addUsers();
    }

//where the database info needs to be implemented; when this function calls initRecyclerView it is going to take the picture of
    //the buddii and the name of the buddii then store it within the card view
    private void addUsers(){


        DatabaseHandler dbHandler = new DatabaseHandler(this);
        int numOfBuddies = dbHandler.getNumOfUsers();
        String ArrayOfBuddies[];
        ArrayOfBuddies=(dbHandler.loadUsers("name"));
        // mbuddiiNames.add("TESTING");

        // for loop will irretiate the index of ArrayOfBuddies and will
        // populate them into mBuddiNames ArrayList
        for (int i = 0 ; i < numOfBuddies; i++) {

            mbuddiiNames.add(ArrayOfBuddies[i]);
        }

        initRecyclerView();

    }
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.findBudsList);
        buddiisAdapter adapter = new buddiisAdapter(this, mbuddiiImages, mbuddiiNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}



