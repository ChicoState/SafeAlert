package com.example.buddii;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class addFriend extends AppCompatActivity {
    EditText input1,input2;
    String data1,data2;
    Button SubmitBUTTON;
    TextView Tx1,Tx2,Tx3,Tx4, TempTexViewVariable2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final databaseHandler dbHandler = new databaseHandler(this);
        int numOfBuddies = dbHandler.getNumOfUsers();
        //If database is empty return , othewise will crash app

        // LOAD USERS INPUT BY ID INTO USER VARIABLE
        setContentView(R.layout.add_friend);
        input1=(EditText)findViewById(R.id.addingFriend);
        input2=(EditText)findViewById(R.id.deletingFriend);


        Tx1=(TextView)findViewById(R.id.addingFriend); // need this variable to display user info after click
        TempTexViewVariable2=(TextView)findViewById(R.id.gpsbutton);

        SubmitBUTTON=(Button)findViewById(R.id.addBuddiiFriend);
        SubmitBUTTON.setOnClickListener(new View.OnClickListener() { // In other words , do this after click
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            // WHEN CLICKED SUBMIT , PASS THESE VALUES
            public void onClick(View view) {
                //dbHandler.checkFireBaseDBForUsers();
                //FIRST CONVERT
                data1=input1.getText().toString();
                data2=input2.getText().toString();

                //THEN PASS
                databaseHandler handler=new databaseHandler(addFriend.this);
                //where we need to add t friends table
                //NEED TO CLEAR OUT THE TABLE AFTER SUBMIT WAS PRESSED
                input1.setText("");
                input2.setText("");

            }
        });
    }



}

