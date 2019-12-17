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

import org.json.JSONException;

public class DBActivity extends AppCompatActivity {
    EditText input1,input2,input3,input4;
    String data1,data2,data3,data4, deleteUser;
    Button SubmitBUTTON;
    TextView Tx1,Tx2,Tx3,Tx4, TempTexViewVariable2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.checkFireBaseDBForUsers();
        int numOfBuddies = dbHandler.getNumOfUsers();
        //If database is empty return , othewise will crash app
        if (numOfBuddies == 0){
            // dbHandler.insertDefaultUser();
        }


        // LOAD USERS INPUT BY ID INTO USER VARIABLE
        setContentView(R.layout.database_activity);
        input1=(EditText)findViewById(R.id.i1);
        input2=(EditText)findViewById(R.id.i2);
        input3=(EditText)findViewById(R.id.i3);
        input4=(EditText)findViewById(R.id.i4);

        Tx1=(TextView)findViewById(R.id.t1); // need this variable to display user info after click
        TempTexViewVariable2=(TextView)findViewById(R.id.gpsbutton);

        SubmitBUTTON=(Button)findViewById(R.id.b1);
        SubmitBUTTON.setOnClickListener(new View.OnClickListener() { // In other words , do this after click
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            // WHEN CLICKED SUBMIT , PASS THESE VALUES
            public void onClick(View view) {
                dbHandler.checkFireBaseDBForUsers();
                //FIRST CONVERT
                data1=input1.getText().toString();
                data2=input2.getText().toString();
                data3=input3.getText().toString();
                data4=input4.getText().toString();
                //THEN PASS
                DatabaseHandler handler=new DatabaseHandler(DBActivity.this);
                 handler.addToDb(data1,data2,data3,data4,"" ,false);
               //NEED TO CLEAR OUT THE TABLE AFTER SUBMIT WAS PRESSED
                input1.setText("");
                input2.setText("");
                input3.setText("");
                input4.setText("");
            }
        });
    }

    // This class will delete a user by passed ID
    public  void delete(View view)
    {
        // call to get Users' DB to DELETE
        deleteUser=getUserToDelete();
        DatabaseHandler handler=new DatabaseHandler(this);
        // CALL LOADEMP .. THEN DELETE
        handler.deleteUser(deleteUser);}

    // This function is for testing the DB
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadUser(View view) throws JSONException {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        int numOfBuddies = dbHandler.getNumOfUsers();
        //If database is empty return , othewise will crash app
        if (numOfBuddies == 0) {
            return;
        }

        String ArrayOfBuddies[];
        String results = "";

        // by default load these attributes
        ArrayOfBuddies = (dbHandler.loadUsers("Uid,email,name,phoneNumber"));

        for (int i = 0; i < numOfBuddies; i++) {

            results += ArrayOfBuddies[i] + " \n";
            if (i == (numOfBuddies - 1)) {
                Tx1.setText(results);
            }
        }

        String flagresults=dbHandler.loadFlag("1");
        TempTexViewVariable2.setText(flagresults);
    }
    public String getUserToDelete(){
        // will get user to delete
        return input1.getText().toString();
    }



}

