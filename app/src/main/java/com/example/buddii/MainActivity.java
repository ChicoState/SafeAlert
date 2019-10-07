package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnFSR;
    private Button btnBab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFSR = findViewById(R.id.findRoute);
        btnBab = findViewById(R.id.beABuddii);
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

    }

    private void moveToBaB()
    {
        Intent intent = new Intent (MainActivity.this, select_bud.class);
        startActivity(intent);
    }

    private void moveToChooseRoute()
    {
        Intent intent = new Intent(MainActivity.this, chooseRoute.class);
        startActivity(intent);
    }
}
