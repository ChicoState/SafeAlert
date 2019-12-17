package com.example.buddii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TutorialActivity extends AppCompatActivity {
    private Button btnabt;
    private Button btntutorial;

    //the onCreate function creates the layout of the Tutorial Activity
    //has two buttons 1 to go to the About page and the other going to the
    //tutorial video
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        btnabt = findViewById(R.id.abtUS);
        btntutorial = findViewById(R.id.tutorial);


        btnabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAbout();
            }
        });

        btntutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTutorial();
            }
        });

    }
    //private void function that moves you to the about us activity
    private void moveToAbout(){
        Intent intent = new Intent (TutorialActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    //private void function that moves you to the beingABuddyActivity
    private void moveToTutorial(){
        Intent intent = new Intent (TutorialActivity.this, beingABuddyActivity.class);
        startActivity(intent);
    }


}
