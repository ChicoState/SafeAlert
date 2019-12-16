package com.example.buddii.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class loggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    loggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
