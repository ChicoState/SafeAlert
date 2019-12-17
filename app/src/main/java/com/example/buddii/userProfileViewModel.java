/**
 * Class that prepares the data for viewing in the UserProfileFragment and reacts to user interactions.
 * */

package com.example.buddii;

import androidx.lifecycle.ViewModel;

public class userProfileViewModel extends ViewModel {

    private String userId, displayName, userEmail, userPhone;

    public userProfileViewModel(String userId, String displayName, String userEmail) {
        this.userId = userId;
        this.displayName = displayName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }
    public userProfileViewModel(){}

    public String getUserId() {
        return userId;
    }
    public String getDisplayName() { return displayName; }
    public String getUserEmail() {
        return userEmail;
    }
}