package com.example.buddii.data;

import com.example.buddii.DatabaseHandler;
import com.example.buddii.data.model.LoggedInUser;
import com.example.buddii.ui.login.LoginActivity;

import java.io.IOException;
import java.lang.Exception;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends AppCompatActivity {


    public Result<LoggedInUser> login(String username, String password){
        try {

            DatabaseHandler handler=new DatabaseHandler(LoginDataSource.this);
            // TODO: handle loggedInUser authentication
            Log.d("xxxPwordFromLogIn",password);
            //handler.checkCredentials(password)
            String checkCred = handler.checkCredentials(password);
            if( checkCred == "true") {
                Log.d("xxxafterChkCdrntls",password);
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
