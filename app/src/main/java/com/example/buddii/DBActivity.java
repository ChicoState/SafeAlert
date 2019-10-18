package com.example.buddii;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class DBActivity extends AppCompatActivity {
    EditText input1,input2,input3,input4;
    String data1,data2,data3,data4, deleteUser;
    Button SubmitBUTTON;
    TextView TempTexViewVariable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_buds_list);


        // LOAD USERS INPUT BY ID INTO USER VARIABLE
        setContentView(R.layout.database_activity);
        input1=(EditText)findViewById(R.id.i1);
        input2=(EditText)findViewById(R.id.i2);
        input3=(EditText)findViewById(R.id.i3);
        input4=(EditText)findViewById(R.id.i4);

        TempTexViewVariable=(TextView)findViewById(R.id.t1); // need this variable to display user info after click

        SubmitBUTTON=(Button)findViewById(R.id.b1);
        SubmitBUTTON.setOnClickListener(new View.OnClickListener() { // In other words , do this after click
            @Override
            // WHEN CLICKED SUBMIT , PASS THESE VALUES
            public void onClick(View view) {
                //FIRST CONVERT
                data1=input1.getText().toString();
                data2=input2.getText().toString();
                data3=input3.getText().toString();
                data4=input4.getText().toString();
                //THEN PASS
                DatabaseHandler handler=new DatabaseHandler(DBActivity.this);
                handler.addToDb(data1,data2,data3,data4);
                // NEED TO CLEAR OUT THE TABLE AFTER SUBMIT WAS PRESSED
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

     public void loadUser(View view)
    {   //propriatary DBhandle
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        TempTexViewVariable.setText(dbHandler.load(0));


    }


    public String getUserToDelete(){
        return input1.getText().toString();

    };

}

