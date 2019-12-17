package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.buddii.ui.usrprofile.UsrProfileFragment;

public class usrProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_profile_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UsrProfileFragment.newInstance())
                    .commitNow();
        }
    }
}
