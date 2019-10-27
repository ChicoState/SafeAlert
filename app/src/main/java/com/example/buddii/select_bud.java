package com.example.buddii;


import android.app.Activity;
import android.os.Bundle;

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
        addDummyUsers();
    }

//where the database info needs to be implemented; when this function calls initRecyclerView it is going to take the picture of
    //the buddii and the name of the buddii then store it within the card view
    private void addDummyUsers(){

        mbuddiiImages.add("Mick image");
        mbuddiiNames.add("Mick");

        mbuddiiImages.add("Jesus image");
        mbuddiiNames.add("Jesus");

        mbuddiiImages.add("Robert image");
        mbuddiiNames.add("Robert");

        mbuddiiImages.add("Ben image");
        mbuddiiNames.add("Ben");

        mbuddiiImages.add("Isaac image");
        mbuddiiNames.add("Isaac");

        mbuddiiImages.add("Nick image");
        mbuddiiNames.add("Nick");


        initRecyclerView();

    }
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.findBudsList);
        buddiisAdapter adapter = new buddiisAdapter(this, mbuddiiImages, mbuddiiNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}



