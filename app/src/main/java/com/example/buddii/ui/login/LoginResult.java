package com.example.buddii.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class loginResult {
    @Nullable
    private loggedInUserView success;
    @Nullable
    private Integer error;

    loginResult(@Nullable Integer error) {
        this.error = error;
    }

    loginResult(@Nullable loggedInUserView success) {
        this.success = success;
    }

    @Nullable
    loggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
