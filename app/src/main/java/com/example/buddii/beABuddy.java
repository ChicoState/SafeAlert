package com.example.buddii;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class beABuddy extends AppCompatActivity {

    private ArrayList<String> mbuddiiNames = new ArrayList<>();
    private ArrayList<String> mbuddiiImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_abuddy);
        addUsers();

    }

    private void addUsers(){

        mbuddiiNames.add("John Smith");
        mbuddiiNames.add("Sven Archer");
        mbuddiiNames.add("Dallas Fortworth");
        mbuddiiNames.add("Gandalf T. White");

        initRecyclerView();

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.BeBudList);
        beBuddiiAdapter adapter = new beBuddiiAdapter(this, mbuddiiImages, mbuddiiNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
