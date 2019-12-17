package com.example.buddii;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


//allows users to add specific friends to their profile
//so they can chooose them to be their buddii instead
//of a random person, friend is added via their phone number
//and thats how the database finds the said friend is through
// their phone number
public class addFriend extends AppCompatActivity {
    EditText input1,input2;
    String data1,data2;
    Button SubmitBUTTON;
    TextView Tx1, TempTexViewVariable2;

    @RequiresApi(api = Build.VERSION_CODES.O)

    //creates the layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseHandler dbHandler = new DatabaseHandler(this);
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

            //adds friend to the users list of friends when
            //all fields (phone number and user name
            // are filled out and submitted
            @Override
            // WHEN CLICKED SUBMIT , PASS THESE VALUES
            public void onClick(View view) {
                //FIRST CONVERT
                data1=input1.getText().toString();
                data2=input2.getText().toString();

                //THEN PASS
                DatabaseHandler handler=new DatabaseHandler(addFriend.this);
                //where we need to add t friends table
                //NEED TO CLEAR OUT THE TABLE AFTER SUBMIT WAS PRESSED
                input1.setText("");
                input2.setText("");

            }
        });
    }



}

