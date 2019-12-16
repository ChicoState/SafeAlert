package com.example.buddii;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class selectBud extends Activity
{
    Button buddiiist, friendList;
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


        databaseHandler dbHandler = new databaseHandler(this);
        int numOfBuddies = dbHandler.getNumOfUsers();
        //If database is empty return , othewise will crash app
        if (numOfBuddies == 0){
            return;
        }
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
        buddiiAdapter adapter = new buddiiAdapter(this, mbuddiiImages, mbuddiiNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}



