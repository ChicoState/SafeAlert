package com.example.buddii.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;


public class GeofenceTransitionsIntentService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
               // String errorMessage = GeofenceErrorMessages.getErrorString(this,
                //        geofencingEvent.getErrorCode());
                //Log.e(TAG, errorMessage);
                Toast.makeText(context, "ERROR IN ON RECIEVE", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();

            // Test that the reported transition was of interest.
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

                // Get the geofences that were triggered. A single event can trigger
                // multiple geofences.
                List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

                Toast.makeText(context, "THERE HAS BEEN A TRANSITION", Toast.LENGTH_SHORT).show();

                // Get the transition details as a String.
                /*
                String geofenceTransitionDetails = getGeofenceTransitionDetails(
                        this,
                        geofenceTransition,
                        triggeringGeofences
                );


                 */
                // Send notification and log the transition details.
                //sendNotification(geofenceTransitionDetails);
                //Log.i(TAG, geofenceTransitionDetails);
            } else {
                // Log the error.
                //Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
                       // geofenceTransition));
            }

    }
}
