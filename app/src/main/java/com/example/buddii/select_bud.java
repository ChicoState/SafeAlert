package com.example.buddii;


import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


import android.widget.TextView;

public class select_bud extends AppCompatActivity {
    TextView Tx1;
    TextView Tx2;
    TextView Tx3;
    TextView Tx4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_buds_list);


        Tx1=(TextView)findViewById(R.id.find_bud_text_1);
        Tx2=(TextView)findViewById(R.id.find_bud_text_2);
        Tx3=(TextView)findViewById(R.id.find_bud_text_3);
        Tx4=(TextView)findViewById(R.id.find_bud_text_4);

    }
    // Loads info from DB activity
    public void loadUser2(View view)
    {

        //proprietary DBhandle
        DatabaseHandler dbHandler = new DatabaseHandler(this);


        String ArrayOfBuddies[];
        // get number of buddies from DB
        int numOfBuddies = dbHandler.getNumOfUsers();
        // retrieve array of buddies from database
        // each index holds a buddi
        ArrayOfBuddies=(dbHandler.loadUsers("name,phoneNumber,email"));



        for (int i = 0; i < numOfBuddies; i++) {
            if(i == 0) {
                Tx1.setText(ArrayOfBuddies[i]);
            }
            if(i == 1) {
                Tx2.setText(ArrayOfBuddies[i]);
            }
            if(i == 2) {
                Tx3.setText(ArrayOfBuddies[i]);
            }
            if(i == 3) {
                Tx4.setText(ArrayOfBuddies[i]);
            }
        }

    }
}




