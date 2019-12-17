/**
 * UI controller that displays the data.
 */

package com.example.buddii;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.buddii.data.model.loggedInUser;

public class userProfileActivity extends Activity {
    loggedInUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        updateProfilePage();
    }
    private void updateProfilePage() {
        ImageView profileUserPic = findViewById(R.id.profile_icon);
        TextView profileUserName = findViewById(R.id.profile_name);
        profileUserName.setText(currentUser.getDisplayName());
        Glide.with(this).load("http://goo.gl/gEgYUd").into(profileUserPic);
    }
}
