package com.example.buddii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class select_bud extends AppCompatActivity
{

    LinearLayout benArcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_buds_list);

        CardView benArcher = (CardView) findViewById(R.id.benArcher);

        benArcher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToBuddiiRoute();
            }

        });
    }

    private void moveToBuddiiRoute()
    {
        Intent intent = new Intent(select_bud.this, buddiiRoute.class);
        startActivity(intent);

    }
}




