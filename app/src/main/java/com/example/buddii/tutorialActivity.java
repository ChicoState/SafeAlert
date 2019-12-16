package com.example.buddii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class tutorialActivity extends AppCompatActivity {
    private Button btnabt;
    private Button btntutorial;


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

    private void moveToAbout(){
        Intent intent = new Intent (tutorialActivity.this, aboutUsActivity.class);
        startActivity(intent);
    }

    private void moveToTutorial(){
        Intent intent = new Intent (tutorialActivity.this, beBuddiiActivity.class);
        startActivity(intent);
    }


}
