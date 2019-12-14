package com.example.buddii.Map.UserView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.DatabaseHandler;
import com.example.buddii.Freakout;
import com.example.buddii.Map.DirectionItem;
import com.example.buddii.Map.DirectionsJSONParser;
import com.example.buddii.Map.DirectionsRecyclerAdapter;
import com.example.buddii.Map.GeofenceTransitionsIntentService;
import com.example.buddii.Map.directionsAdapter;
import com.example.buddii.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Math.sqrt;

public class ScrollMapUser extends AppCompatActivity
        implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final int REQUEST_PERMISSION_CODE = 1000;
    MediaRecorder mediaRecorder;
    String pathSave;
    Context mContext;
    LinearLayout UserTabInfo, UserTabHome, UserTabReport, UserTabRoute, UserTabChat;
    Button UserHome, UserRoute;
    private FusedLocationProviderClient client;
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private static final String TAG = ScrollMapUser.class.getSimpleName();
    String directionTestString;
    ArrayList<DirectionItem> directionList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Circle currLocMovingCircle;
    GeofencingClient mGeofencingClient;
    PendingIntent mGeofencePendingIntent;
    int numero = 0;


    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = null;
    private static GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private static Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    LinkedList<Geofence> geofenceList = null;
    Location currentLocation;

    public static Intent makeNotificationIntent(Context applicationContext, String msg) {
        Log.d(TAG, msg);
        return new Intent(applicationContext, ScrollMapUser.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void makeDirections(LatLng pT, LatLng pTF, LatLng pTA) {
        double firstRise, secondRise, firstRun, secondRun;

        firstRise = pTF.latitude - pTA.latitude;
        secondRise = pT.latitude - pTF.latitude;
        firstRun = pTF.longitude - pTA.longitude;
        secondRun = pT.longitude - pTF.longitude;

        Double distanceRise = currentLocation.getLatitude() - pT.latitude;
        Double distanceRun = currentLocation.getLongitude() - pT.longitude;

        Double distanceTo = sqrt(Math.pow(distanceRise, 2) + Math.pow(distanceRun, 2));
        distanceTo *= 69;

        if (firstRun < secondRun) {
            if (firstRise >= secondRise) {
                directionList.add(new DirectionItem(R.drawable.right, "Right", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " right,";
            } else {
                directionList.add(new DirectionItem(R.drawable.left, "Left", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " left,";
            }
        } else {
            if (firstRise >= secondRise) {
                directionList.add(new DirectionItem(R.drawable.left, "Left", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " left,";
            } else {
                directionList.add(new DirectionItem(R.drawable.right, "Right", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " right,";
            }
        }

    }

    private void makeDirectionsAdapter() {
        for (int i = 0; i < DirectionsJSONParser.pointsTurn.size() - 1; i += 2) {
            makeDirections(new LatLng((Double) DirectionsJSONParser.pointsTurn.elementAt(i), (Double) DirectionsJSONParser.pointsTurn.elementAt(i + 1)), new LatLng((Double) DirectionsJSONParser.pointsTurnFrom.elementAt(i), (Double) DirectionsJSONParser.pointsTurnFrom.elementAt(i + 1)), new LatLng((Double) DirectionsJSONParser.pointsTurnAfter.elementAt(i), (Double) DirectionsJSONParser.pointsTurnAfter.elementAt(i + 1)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted RequestPermissionResult", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied RequestPermissionResult", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void setBlueThings() { //THIS IS TEMPORARY YOU SONS OF BITCHES

        mMap.addCircle(new CircleOptions().center(new LatLng(39.726408, -121.847657))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.730534, -121.848798))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.728891, -121.848637))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.728735, -121.845895))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.729761, -121.843255))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.730822, -121.845217))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.728915, -121.846879))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
        mMap.addCircle(new CircleOptions().center(new LatLng(39.728071, -121.844249))
                .radius(10)
                .strokePattern(PATTERN_POLYLINE_DOTTED)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN));
    }

    private void getCurrLocation() {
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(ScrollMapUser.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        client.getLastLocation().addOnSuccessListener(ScrollMapUser.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addCircle(new CircleOptions().center(curr)
                            .radius(1)
                            .strokePattern(PATTERN_POLYLINE_DOTTED)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.CYAN));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));

                } else {
                    Toast.makeText(ScrollMapUser.this, "Location Unknown", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @SuppressLint({"WrongViewCast", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_map_user);
        Toolbar toolbarUser = findViewById(R.id.toolbarUser);
        GeofencingClient mGeofencingClient = new GeofencingClient(ScrollMapUser.this);
        setSupportActionBar(toolbarUser);
        createGoogleApi();
        requestPermissions();

        getCurrLocation();


        mRecyclerView = findViewById(R.id.directionsRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new DirectionsRecyclerAdapter(directionList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //LOCATION LISTENER
        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);
        isLocationEnabled();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapUser);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        mMarkerPoints = new ArrayList<>();

    }

    LocationListener locationListenerGPS = new LocationListener() {
        private float[] mRotationMatrix = new float[16];
        float mDeclination;
        SensorEvent event;

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
            GeomagneticField field = new GeomagneticField(
                    (float) location.getLatitude(),
                    (float) location.getLongitude(),
                    (float) location.getAltitude(),
                    System.currentTimeMillis()
            );

            LatLng currLocMoving = new LatLng(location.getLatitude(), location.getLongitude());
            CircleOptions circleOptions = new CircleOptions()
                    .center(currLocMoving)
                    .radius(10)
                    .fillColor(android.R.color.holo_blue_bright)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(2);
            if (currLocMovingCircle != null) {
                currLocMovingCircle.remove();
            }
            currLocMovingCircle = mMap.addCircle(circleOptions);

            DatabaseHandler dbHandler = new DatabaseHandler(ScrollMapUser.this);
            try {
                dbHandler.addGPS(latitude,longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float bearing = (float) (Math.toDegrees(orientation[0]) + mDeclination);
            updateCamera(bearing);

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        private void updateCamera(float bearing) {
            CameraPosition oldPos = mMap.getCameraPosition();

            CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));

        }
    };

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();

    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;

    }


    private void isLocationEnabled() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) { // Im aware this shit makes no sense right now just bare with me

                // Already two locations
                if (mMarkerPoints.size() >= 3) {
                    mMarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                mMarkerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (mMarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (mMarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else if (mMarkerPoints.size() == 3) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                } else if (mMarkerPoints.size() >= 4) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (mMarkerPoints.size() == 2) {
                    mOrigin = mMarkerPoints.get(0);
                    mDestination = mMarkerPoints.get(1);
                    directionsAdapter.drawRoute(mOrigin, mDestination);
                }
                if (mMarkerPoints.size() == 3) {
                    mOrigin = mMarkerPoints.get(1);
                    mDestination = mMarkerPoints.get(2);
                    directionsAdapter.drawRoute(mOrigin, mDestination);
                }
                if (mMarkerPoints.size() == 4) {
                    mOrigin = mMarkerPoints.get(2);
                    mDestination = mMarkerPoints.get(3);
                    directionsAdapter.drawRoute(mOrigin, mDestination);
                }
                if (mMarkerPoints.size() >= 4) {
                    mOrigin = mMarkerPoints.get(3);
                    mDestination = mMarkerPoints.get(4);
                    directionsAdapter.drawRoute(mOrigin, mDestination);
                }

            }

        });

        setBlueThings(); //Temporary place to put the addition of the blue things before we get a database for them

        mMap.setTrafficEnabled(false);
        mMap.setMapType(2);

    }
    public void onMapSearch(View view) {
        EditText locationSearch = findViewById(R.id.RouteManual);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 2);

            } catch (IOException e) {
                e.printStackTrace();
            }
            assert addressList != null;
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            mOrigin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mDestination = latLng;
            directionsAdapter.drawRoute(mOrigin, mDestination);
        }
    }

    public void onReport(View view) {
        EditText locationReport = findViewById(R.id.reportTextUser);
        String location = locationReport.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 2);

            } catch (IOException e) {
                e.printStackTrace();
            }
            assert addressList != null;
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            mDestination = latLng;

            if(false) {

                numero++;
                geofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        .setRequestId(numero + "")

                        .setCircularRegion(
                                latLng.latitude,
                                latLng.longitude,
                                50
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());


                mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // do something
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // do something
                            }
                        });
            }
        }

    }

    public void onHomeClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(GONE);
        UserTabInfo.setVisibility(View.GONE);
        UserTabHome.setVisibility(VISIBLE);
        UserTabReport.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
    }

    public void onInfoClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(GONE);
        UserTabInfo.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
    }

    public void onRouteClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(GONE);
        UserTabRoute.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
    }

    public void onReportClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(GONE);
        UserTabReport.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
    }

    public void onAcceptRouteClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserHome = findViewById(R.id.UserHome);
        UserRoute = findViewById(R.id.UserRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabHome.setVisibility(VISIBLE);
        UserTabInfo.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
        UserHome.setVisibility(VISIBLE);
        UserRoute.setVisibility(GONE);

        makeDirectionsAdapter();

        TextView textView = findViewById(R.id.directionsTester);
        textView.setText(directionTestString);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));

    }

    public void onDropRoute(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(GONE);
        UserTabInfo.setVisibility(View.GONE);
        UserTabHome.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
        UserTabRoute.setVisibility(VISIBLE);
        for (int i = 0; i < directionList.size(); i++) {
            directionList.remove(i);
        }
        UserHome.setVisibility(GONE);
        UserRoute.setVisibility(VISIBLE);
    }

    public void onChatClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(VISIBLE);
        UserTabReport.setVisibility(GONE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
    }

    public void onCurrLocClick(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
    }

    public void onBigAlertEnergyClick(View view) {

        Intent intent = new Intent(ScrollMapUser.this, Freakout.class);
        startActivity(intent);
        DatabaseHandler dbHandlerAlert = new DatabaseHandler(ScrollMapUser.this);
    }

    public static Polyline getPolyline(){
        return mPolyline;
    }

    public static GoogleMap getMap(){
        return mMap;
    }
}
