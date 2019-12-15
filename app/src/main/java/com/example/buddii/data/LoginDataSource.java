package com.example.buddii.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buddii.DatabaseHandler;
import com.example.buddii.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 */
public class LoginDataSource extends AppCompatActivity {

    DatabaseHandler handler=new DatabaseHandler(LoginDataSource.this);


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Result<LoggedInUser> login(String username, String password){



        String checkCred = handler.getPword(password,username);
        //Log.d("xxxLogINpwordRTN", checkCred);

        try {

            // TODO: handle loggedInUser authentication


            if( checkCred.equals("true")) {
                //will sey UID for logged in user
                // handler.setLoggedInUser(username);
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Jane Doe");
                Log.w(null, "success");
                return new Result.Success<>(fakeUser);

            }
        }catch(Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return new Result.Error(new IOException("error with attempt", null));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}