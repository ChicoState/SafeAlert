package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorialActivity extends AppCompatActivity {
    private Button btnabt;
    private Button btnbnb;
    private Button btnfsftr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        btnabt = findViewById(R.id.abtUS);
        btnbnb = findViewById(R.id.beinB);
        btnfsftr = findViewById(R.id.fndnR);

        btnabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAbout();
            }
        });

        btnbnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToBeing();
            }
        });

        btnfsftr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTofinding();
            }
        });

    }

    private void moveToAbout(){
        Intent intent = new Intent (TutorialActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    private void moveToBeing(){
        Intent intent = new Intent (TutorialActivity.this, beingABuddyActivity.class);
        startActivity(intent);
    }

    private void moveTofinding(){
        Intent intent = new Intent (TutorialActivity.this, findingSafestRouteActivity.class);
        startActivity(intent);
    }
}
