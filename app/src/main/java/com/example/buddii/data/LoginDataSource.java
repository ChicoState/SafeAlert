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
public class LoginDataSource extends AppCompatActivity {

    DatabaseHandler handler=new DatabaseHandler(LoginDataSource.this);


    public Result<LoggedInUser> login(String username, String password){


       //Log.d("xxxPwordFromLogIn",password);
       boolean isAMember = handler.chechIfAlreadyMemeber(username);
        String checkCred = handler.getPword(password);
      //Log.d("xxxLogINpwordRTN", checkCred);
        //String checkCred = handler.checkCredentials(pwordInDB, password);
        //  handler.close();
        try {

            // TODO: handle loggedInUser authentication


            if( checkCred.equals("true")) {
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