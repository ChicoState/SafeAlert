package com.example.buddii;


import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


import android.widget.TextView;

public class select_bud extends AppCompatActivity {
    TextView T2;
    //LinearLayout benArcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_buds_list);


        T2=(TextView)findViewById(R.id.textView7);

    }
    // Loads info from DB activity
    public void loadUser2(View view)
    {   //proprietary DBhandle
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        int fromBuddyList =1;
        // passes a 1 to confirm that we just need user name and email
        T2.setText(dbHandler.load(fromBuddyList));

    }
}




