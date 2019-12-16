package com.example.buddii.Map.UserView;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.Map.directionItem;
import com.example.buddii.Map.directionsAdapter;
import com.example.buddii.Map.directionsJSONParser;
import com.example.buddii.Map.directionsRecyclerAdapter;
import com.example.buddii.R;
import com.example.buddii.databaseHandler;
import com.example.buddii.freakout;
import com.example.buddii.selectBud;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
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
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Math.sqrt;

public class ScrollMapUser extends AppCompatActivity
        implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final int REQUEST_PERMISSION_CODE = 1000;
    Context mContext;
    LinearLayout UserTabInfo, UserTabHome, UserTabReport, UserTabRoute;
    Button UserHome, UserRoute;
    private FusedLocationProviderClient client;
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private static final String TAG = ScrollMapUser.class.getSimpleName();
    String directionTestString;
    ArrayList<directionItem> directionList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Circle currLocMovingCircle;
    databaseHandler dbHandler;


    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = null;
    private static GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private static Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    Location currentLocation;


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

    private void makeDirections(LatLng pT, LatLng pTF, LatLng pTA) { //add the direcitons to the recycler view using three points and comparing their rise/run
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
                directionList.add(new directionItem(R.drawable.right, "Right", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " right,";
            } else {
                directionList.add(new directionItem(R.drawable.left, "Left", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " left,";
            }
        } else {
            if (firstRise >= secondRise) {
                directionList.add(new directionItem(R.drawable.left, "Left", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " left,";
            } else {
                directionList.add(new directionItem(R.drawable.right, "Right", String.format("%.2f", distanceTo) + " miles"));
                directionTestString += " right,";
            }
        }

    }

    private void makeDirectionsAdapter() { //cycles through the turns that need to be displayed on the directions view
        for (int i = 0; i < directionsJSONParser.pointsTurn.size() - 1; i += 2) {
            makeDirections(new LatLng((Double) directionsJSONParser.pointsTurn.elementAt(i), (Double) directionsJSONParser.pointsTurn.elementAt(i + 1)), new LatLng((Double) directionsJSONParser.pointsTurnFrom.elementAt(i), (Double) directionsJSONParser.pointsTurnFrom.elementAt(i + 1)), new LatLng((Double) directionsJSONParser.pointsTurnAfter.elementAt(i), (Double) directionsJSONParser.pointsTurnAfter.elementAt(i + 1)));
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

    private void setBlueThings() { //temporary place to keep the emergency beacons on campus

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

    private void getCurrLocation() { //retrieves the current locaiton using android api
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

    private void requestPermissions() { //requests permission for location
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void createGoogleApi() { //google api for locaiton services
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

        //initializa recycler view
        mRecyclerView = findViewById(R.id.directionsRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new directionsRecyclerAdapter(directionList);
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

    //Watches for change in current location so that it can be used by the User and the Buddii
    final LocationListener locationListenerGPS = new LocationListener() {
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

            dbHandler = new databaseHandler(ScrollMapUser.this);
            try {
                dbHandler.addGPS(latitude, longitude);
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


    //Checks if location is able to be used by the User
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


        //Function to allow the user to find directions by clicking on different points on the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

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

    //beginning of the process that makes directions between current location and the location chosen by the user
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

    //when the user reports a location the camera moves to it and marks its location for the geofencer (currently not working)
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

            try {
                dbHandler.sendFlag("0", "L");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //Change to home tab when the Home button is clicked
    public void onHomeClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabInfo.setVisibility(View.GONE);
        UserTabHome.setVisibility(VISIBLE);
        UserTabReport.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
    }

    //Change to info tab when the Info button is clicked
    public void onInfoClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabInfo.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
    }

    //Change to route tab when the route button is clicked
    public void onRouteClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabRoute.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
    }

    //Change to the report tab when the Report tab is clicked
    public void onReportClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabReport.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
    }

    //Change to Directions view and send route info to the Buddii through firebase
    public void onAcceptRouteClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserHome = findViewById(R.id.UserHome);
        UserRoute = findViewById(R.id.UserRoute);
        UserTabRoute.setVisibility(GONE);
        UserTabHome.setVisibility(VISIBLE);
        UserTabInfo.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
        UserHome.setVisibility(VISIBLE);
        UserRoute.setVisibility(GONE);

        dbHandler = new databaseHandler(ScrollMapUser.this);
        try {
            dbHandler.sendFlag("0", "R");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        makeDirectionsAdapter();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));

    }

    //Drops route and changes to screen where user can pick route. also sends route drop info to the Buddii
    public void onDropRoute(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabInfo.setVisibility(View.GONE);
        UserTabHome.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
        UserTabRoute.setVisibility(VISIBLE);
        for (int i = 0; i < directionList.size(); i++) {
            directionList.remove(i);
        }

        try {
            dbHandler.sendFlag("0", "D");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserHome.setVisibility(GONE);
        UserRoute.setVisibility(VISIBLE);
    }

    //Drops buddii and sends that flag to the buddii
    public void onDropBuddiiClick(View view) {

        Intent intent  = new Intent(ScrollMapUser.this, selectBud.class);
        startActivity(intent);

        try {
            dbHandler.sendFlag("0", "B");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Moves the map to the current location of the User
    public void onCurrLocClick(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
    }

    //Notifies the Buddii of the Alert and changes the User's view to the Freakout function
    public void onBigAlertEnergyClick(View view) {

        Intent intent = new Intent(ScrollMapUser.this, freakout.class);
        startActivity(intent);
        dbHandler = new databaseHandler(ScrollMapUser.this);
        try {
            dbHandler.sendFlag("0", "A");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //allows use of mPolyline to directionsAdapter
    public static Polyline getPolyline(){
        return mPolyline;
    }

    //allows user of the map from directionsAdapter
    public static GoogleMap getMap(){
        return mMap;
    }

}
