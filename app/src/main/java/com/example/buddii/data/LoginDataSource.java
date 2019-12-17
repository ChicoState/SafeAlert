package com.example.buddii.data;

import com.example.buddii.DBActivity;
import com.example.buddii.DatabaseHandler;
import com.example.buddii.data.model.LoggedInUser;
import java.security.NoSuchAlgorithmException;

import java.io.IOException;
import java.lang.Exception;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class LoginDataSource extends AppCompatActivity {

    DatabaseHandler handler=new DatabaseHandler(LoginDataSource.this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Result<LoggedInUser> login(String username, String password){


        // real function call
        String checkCred = handler.getPword(password,username);

        try {
            if( checkCred.equals("true")) {
               //will sey UID for logged in user

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