package com.example.buddii.ui.login;

import android.os.Build;
import android.util.Patterns;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.buddii.R;
import com.example.buddii.data.result;
import com.example.buddii.data.model.loggedInUser;

public class loginViewModel extends ViewModel {

    private MutableLiveData<com.example.buddii.ui.login.loginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<com.example.buddii.ui.login.loginResult> loginResult = new MutableLiveData<>();
    private com.example.buddii.data.loginRepository loginRepository;

    loginViewModel(com.example.buddii.data.loginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<com.example.buddii.ui.login.loginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<com.example.buddii.ui.login.loginResult> getLoginResult() {
        return loginResult;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        result<loggedInUser> result = loginRepository.login(username, password);

        if (result instanceof com.example.buddii.data.result.Success) {
            loggedInUser data = ((com.example.buddii.data.result.Success<loggedInUser>) result).getData();
            loginResult.setValue(new loginResult(new loggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new loginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new loginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new loginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new loginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
