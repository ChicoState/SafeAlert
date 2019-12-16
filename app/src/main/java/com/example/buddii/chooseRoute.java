package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.buddii.Map.UserView.ScrollMapUser;

public class chooseRoute extends AppCompatActivity {
    private Button btnBuddiis;
    private Button btnFriends;
    private Button btnNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route);
        btnBuddiis = findViewById(R.id.yesBuddii);
        btnFriends = findViewById(R.id.yesFriends);
        btnNo = findViewById(R.id.noBuddii);

        btnBuddiis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToSelectBuddii();
            }
        });

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToSelectFriend();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToRoute();
            }
        });
    }
    private void moveToRoute() {
        Intent intent = new Intent(chooseRoute.this, ScrollMapUser.class);
        startActivity(intent);
    }
    private void moveToSelectBuddii() {
        Intent intent = new Intent(chooseRoute.this, selectBud.class);
        startActivity(intent);
    }

    private void moveToSelectFriend(){
        Intent intent = new Intent(chooseRoute.this, selectFriends.class);
        startActivity(intent);
    }


    }

