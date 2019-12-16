package com.example.buddii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class mainActivity extends AppCompatActivity {
    private Button btnFSR;
    private Button btnBab;
    private Button btnTut;
    private Button btnFriend;

    private Button btnTst;
    TextView UserTexViewVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getLocationPermission();

        btnFSR = findViewById(R.id.findRoute);
        btnBab = findViewById(R.id.beABuddii);
        btnTut = findViewById(R.id.Tutorial);
        btnFriend = findViewById(R.id.addBuddiiFriend);
       // btnTst = findViewById(R.id.testdb);
        btnFSR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToChooseRoute();
            }
        });
        btnBab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToBaB();
            }
        });

        btnFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToAddFriend();
            }
        });


       // UserTexViewVariable=(TextView)findViewById(R.id.bud2);

        btnTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTut();
            }
        });

    }

    private void moveToBaB()
    {
        Intent intent = new Intent (mainActivity.this, beABuddy.class);
        startActivity(intent);
    }

    private void moveToChooseRoute()
    {
        Intent intent = new Intent(mainActivity.this, chooseRoute.class);
        startActivity(intent);
    }

    private void moveToTut(){
        Intent intent = new Intent(mainActivity.this, tutorialActivity.class);
        startActivity(intent);
    }

    private void moveToDb(){
        Intent intent = new Intent(mainActivity.this, dbActivity.class);
        startActivity(intent);
    }

    private void moveToAddFriend(){
        Intent intent = new Intent(mainActivity.this, addFriend.class);
        startActivity(intent);
    }

}
