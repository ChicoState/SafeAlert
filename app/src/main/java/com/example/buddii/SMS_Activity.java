/*
 * SMS_Activity implementation allows for the app to send sms messages
 *  to the user of the the contact pages.
 *
 *
 * File is a modification of code provided by Google Inc
 *
 * License Info:
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.buddii;

import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Enables SMS activities allowing user to:
 *  - set a destination number
 * - Enter a phone number.
 * - Enter a message and send the message to the phone number.
 * - Receive SMS messages and display them in a toast.
 */
public class SMS_Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    /**
     * Creates the activity, sets the view, and checks for SMS permission.
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // Check to see if SMS is enabled.
        checkForSmsPermission();

    }
    /**
     * Checks whether the app has SMS permission.
     */
    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an app-defined int constant. The callback method gets the result of the request.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            //Permission already granted. Enable the SMS Button
            enableSMSButton();
        }
    }
    /**
     * Processes permission request codes.
     * @param requestCode  The request code passed in requestPermissions()
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    // Send confirmation
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(MainActivity.this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                    // Disable the message button.
                    //disableSMSButton();
                }
            }
        }
    }

    /**
     * Makes the call button (phone_icon) invisible so that it can't be used,
     * and makes the Retry button visible.
     */

     /*private void disableSMSButton() {
        Toast.makeText(this, R.string.phone_disabled, Toast.LENGTH_LONG).show();
        ImageButton callButton = (ImageButton) findViewById(R.id.message_icon);
        callButton.setVisibility(View.INVISIBLE);
        if (isTelephonyEnabled()) {
            Button retryButton = (Button) findViewById(R.id.button_retry);
            retryButton.setVisibility(View.VISIBLE);
        }
       }*/

    /**
     * Makes the call button (phone_icon) visible so that it can be used.
     */
    private void enableSMSButton() {
        ImageButton callButton = (ImageButton) findViewById(R.id.message_icon);
        callButton.setVisibility(View.VISIBLE);
    }

    /**
     * Enables the call button, and sends an intent to start the activity.
     *
     * @param view View (Retry button) that was clicked.

    public void retryApp(View view) {
        enableSMSButton();
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(intent);
    }
    */


    /**
     * Defines a string (destinationAddress) for the phone number
     * and gets the input text for the SMS message.
     * Uses SmsManager.sendTextMessage to send the message.
     * Before sending, checks to see if permission is granted.
     *
     * @param view View (message_icon) that was clicked.
     */
    public void smsSendMessage(View view) {
        // Set the destination phone number to contact's phone number
        // String destinationAddress = editText.getText().toString();
        String destinationAddress = editText.getText().toString();

        // Find the sms_message view.
        //EditText smsEditText = findViewById(R.id.sms_message);

        String smsMessage = "Buddi has requested you as her Buddii Guard";

        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.
        checkForSmsPermission();
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage, sentIntent, deliveryIntent);
    }
}