package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    private Button btnFSR;
    private Button btnBab;
    private Button btnTut;

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
        btnTst = findViewById(R.id.testdb);
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
                DatabaseHandler handler = new DatabaseHandler(MainActivity.this);
                handler.addToActiveBuddiTable();
            }
        });
        btnTst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToDb();
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
        Intent intent = new Intent (MainActivity.this, select_bud.class);
        startActivity(intent);
    }

    private void moveToChooseRoute()
    {
        Intent intent = new Intent(MainActivity.this, chooseRoute.class);
        startActivity(intent);
    }

    private void moveToTut(){
        Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
        startActivity(intent);
    }

    private void moveToDb(){
        Intent intent = new Intent(MainActivity.this, DBActivity.class);
        startActivity(intent);
        }


}
