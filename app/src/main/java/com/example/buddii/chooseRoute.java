package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.buddii.Map.UserView.ScrollMapUser;

//basically just a fork in the road (3 routes) - do you
//need a buddii, a friend, or do you want to go alone and
//just find the safest route. you choose either with the buttons
public class chooseRoute extends AppCompatActivity {
    private Button btnBuddiis;
    private Button btnFriends;
    private Button btnNo;

    //ceates the layout of the activity
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

    //function that allows us to move to the finding a safe route activity (ScrollMapUser)
    private void moveToRoute() {
        Intent intent = new Intent(chooseRoute.this, ScrollMapUser.class);
        startActivity(intent);
    }
    //function that allows us to move to the finding a buddii activity (select_bud)
    private void moveToSelectBuddii() {
        Intent intent = new Intent(chooseRoute.this, select_bud.class);
        startActivity(intent);
    }


    //function that allows us to move to the finding a friend activity (select_friends)
    private void moveToSelectFriend(){
        Intent intent = new Intent(chooseRoute.this, select_friends.class);
        startActivity(intent);
    }


    }

