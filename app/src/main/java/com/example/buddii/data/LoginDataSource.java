package com.example.buddii.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buddii.databaseHandler;
import com.example.buddii.data.model.loggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class loginDataSource extends AppCompatActivity {

    databaseHandler handler=new databaseHandler(loginDataSource.this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public result<loggedInUser> login(String username, String password){


        // real function call
        String checkCred = handler.getPword(password,username);

        try {

            // TODO: handle loggedInUser authentication


            if( checkCred.equals("true")) {
               //will sey UID for logged in user
                 loggedInUser fakeUser =
                        new loggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Jane Doe");
                Log.w(null, "success");
                return new result.Success<>(fakeUser);

            }
        }catch(Exception e) {
            return new result.Error(new IOException("Error logging in", e));
        }
        return new result.Error(new IOException("error with attempt", null));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}