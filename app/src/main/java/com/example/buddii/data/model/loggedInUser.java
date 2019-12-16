package com.example.buddii.data.model;

/**
 * Data class that captures user information for logged in users retrieved from loginRepository
 */
public class loggedInUser {

    private String userId;
    private String displayName;

    public loggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
