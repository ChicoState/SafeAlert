package com.example.buddii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//MainActivity is the activity for the homepage
//has a few buttons that go to different activities
//such as the find safe route, be a buddy, tutorial,
//and adding friends
public class MainActivity extends AppCompatActivity {
    private Button btnFSR;
    private Button btnBab;
    private Button btnTut;
    private Button btnFriend;

    //takes care of creating the activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnFSR = findViewById(R.id.findRoute);
        btnBab = findViewById(R.id.beABuddii);
        btnTut = findViewById(R.id.Tutorial);
        btnFriend = findViewById(R.id.addBuddiiFriend);

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

        btnTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTut();
            }
        });

    }

    //function to move the activity to the be a buddii activity
    private void moveToBaB()
    {
        Intent intent = new Intent (MainActivity.this, select_bud.class);
        startActivity(intent);
    }

    //function to move the activity to the choose route activity
    private void moveToChooseRoute()
    {
        Intent intent = new Intent(MainActivity.this, chooseRoute.class);
        startActivity(intent);
    }

    //function to move the activity to the tutorial activity
    private void moveToTut(){
        Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
        startActivity(intent);
    }


    //function to move the activity to the add friend activity
    private void moveToAddFriend(){
        Intent intent = new Intent(MainActivity.this, addFriend.class);
        startActivity(intent);
    }

}
