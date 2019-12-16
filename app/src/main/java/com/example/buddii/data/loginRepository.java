package com.example.buddii.data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.buddii.data.model.loggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class loginRepository {

    private static volatile loginRepository instance;

    private loginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private loggedInUser user = null;

    // private constructor : singleton access
    private loginRepository(loginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static loginRepository getInstance(loginDataSource dataSource) {
        if (instance == null) {
            instance = new loginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(loggedInUser user) {

        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public result<loggedInUser> login(String username, String password) {
        // handle login
        result<loggedInUser> result = dataSource.login(username, password);
        if (result instanceof com.example.buddii.data.result.Success) {
            setLoggedInUser(((com.example.buddii.data.result.Success<loggedInUser>) result).getData());
        }
        return result;
    }
}
