package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseRoute /*extends AppCompatActivity*/
{
    /*
    private Button btnYes;
    private Button btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route);

        btnYes = findViewById(R.id.yesBuddii);
        btnNo = findViewById(R.id.noBuddii);

        btnYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View v)
            {
                moveToSelectBuddii();
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

    private void moveToRoute()
    {
        Intent intent = new Intent(chooseRoute.this, MapsActivity.class);
        startActivity(intent);
    }

    private void moveToSelectBuddii()
    {
        /*Intent intent = new Intent(chooseRoute.this, findBudsList.class);
        startActivity(intent);
    }*/

}
