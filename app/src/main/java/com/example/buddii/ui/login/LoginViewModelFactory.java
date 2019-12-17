package com.example.buddii.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.buddii.data.loginDataSource;
import com.example.buddii.data.loginRepository;

/**
 * ViewModel provider factory to instantiate loginViewModel.
 * Required given loginViewModel has a non-empty constructor
 */
public class loginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(loginViewModel.class)) {
            return (T) new loginViewModel(loginRepository.getInstance(new loginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
