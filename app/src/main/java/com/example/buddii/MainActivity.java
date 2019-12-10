package com.example.buddii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


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


        //testWrite();
        //testRetrieve();
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
                // will need to send the actual UID of user logged in as a parameter
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




    public void testWrite(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("xxx", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("xxxxW", "Failed to read value.", error.toException());
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
