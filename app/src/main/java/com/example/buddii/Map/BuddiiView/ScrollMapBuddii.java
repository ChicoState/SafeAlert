package com.example.buddii.Map.BuddiiView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buddii.DatabaseHandler;
import com.example.buddii.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;

public class ScrollMapBuddii extends AppCompatActivity implements OnMapReadyCallback {

    final int REQUEST_PERMISSION_CODE = 1000;
    MediaPlayer mediaPlayer = new MediaPlayer();
    MediaRecorder mediaRecorder;
    String pathSave;
    Button btnPlay, btnRecord, btnStop, btnStopRecord;
    Context context;
    int brightness;
    LinearLayout BuddiiTabUser, BuddiiTabHome, BuddiiTabChat;

    private GeofencingClient geofencingClient;
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = null;
    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    LinkedList<Geofence> geofenceList = null;


    private int mLocationPermissionGranted = 0;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Button reportButton;
    private Button searchButton;

    public class LonLat {
        private double longitude;
        private double latitude;

        public void setLongitude(double lon) {
            this.longitude = lon;
        }

        public void setLatitude(double lat) {
            this.latitude = lat;
        }

        public double getLat() {
            return this.latitude;
        }

        public double getLon() {
            return this.longitude;
        }
    }

    private Vector<ScrollMapBuddii.LonLat> RetrieveLocations() {
        //put all of the longitudes and latitudes from the database
        //into a vector of type LonLat defined at the top
        Vector<ScrollMapBuddii.LonLat> temp = new Vector<>();
        return temp;
    }

    private void setWaypoints(Vector<ScrollMapBuddii.LonLat> x) {

        for (int i = 0; i < x.size(); i++) {
            ScrollMapBuddii.LonLat temp = x.get(i);
            mMap.addCircle(new CircleOptions().center(new LatLng(temp.getLon(), temp.getLat()))
                    .radius(20)
                    .strokePattern(PATTERN_POLYLINE_DOTTED)
                    .strokeColor(Color.RED)
                    .fillColor(Color.YELLOW));
            ;
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

    private void dataLoop() {
        //Function to be continually sending data to the Buddii and the database
        
            /*
            Don't need to send currlocation to Buddii because that can be done with a listener
            Don't need to send geofencing data to Buddii because that too can be done with a listener
            Don't need to send destinationArrived to Buddii because you could add a geofence at the locaiton and send it to Buddii with the listener
            Don't
             */
        Toast.makeText(this, "5 SECOND TEST", Toast.LENGTH_SHORT).show();
        alertView();
        //update map to user's location

    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBuddii);
        setSupportActionBar(toolbar);

        // tabListener();

        //Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 254);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapBuddii);
        mapFragment.getMapAsync(this);
        mMarkerPoints = new ArrayList<>();

        Toast.makeText(this, "AAAAAAAAA", Toast.LENGTH_SHORT).show();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScrollMapBuddii.this, "SCHEDULER", Toast.LENGTH_SHORT).show();
                dataLoop();
            }
        }, 5, 5, TimeUnit.SECONDS);

    }

    public void onHomeClick(View view) {
        BuddiiTabUser = findViewById(R.id.BuddiiTabUser);
        BuddiiTabUser.setVisibility(View.GONE);
        BuddiiTabHome = findViewById(R.id.BuddiiTabHome);
        BuddiiTabHome.setVisibility(View.VISIBLE);
        BuddiiTabChat = findViewById(R.id.BuddiiTabChat);
        BuddiiTabChat.setVisibility(View.GONE);
    }

    public void onUserClick(View view) {
        BuddiiTabUser = findViewById(R.id.BuddiiTabUser);
        BuddiiTabUser.setVisibility(View.VISIBLE);
        BuddiiTabHome = findViewById(R.id.BuddiiTabHome);
        BuddiiTabHome.setVisibility(GONE);
        BuddiiTabChat = findViewById(R.id.BuddiiTabChat);
        BuddiiTabChat.setVisibility(View.GONE);
    }

    public void onChatClick(View view) {
        BuddiiTabUser = findViewById(R.id.BuddiiTabUser);
        BuddiiTabUser.setVisibility(View.GONE);
        BuddiiTabHome = findViewById(R.id.BuddiiTabHome);
        BuddiiTabHome.setVisibility(GONE);
        BuddiiTabChat = findViewById(R.id.BuddiiTabChat);
        BuddiiTabChat.setVisibility(View.VISIBLE);
    }

    public void onUserLocClick(View view) {
        DatabaseHandler dbHandler = new DatabaseHandler(ScrollMapBuddii.this);
        String location[]  = dbHandler.loadGPS("0");
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(location[0]),Double.parseDouble(location[1]))));
    }

    private void alertView() {
        /*
        if(flag attribute has changed){
            if(attribute == started route){

            }else if(attribute == userEmergency){
                add picture (emergency)
                add text(user + "has declared and emergency. Their current locaiton is " + userLocation);
                turnBackgroundRed
                centerCameraOnUserLocation
            }else if(attribute == enteredDangerousZone){
                add picture (enter_dangerous_zone);
                add text(user + "has entered a dangerous zone");

            }else if(attribute == exitedDangerousZone){

            }else if(attribute == leftRoute){

            }else if(attribute == userDroppedBuddii){

            }else if(attribute == finishedRoute){

            }
        }
         */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geofencingClient = LocationServices.getGeofencingClient(this);


        // Add a marker in Sydney and move the camera
        LatLng chico = new LatLng(39.7285, -121.8375);
        mMap.addMarker(new MarkerOptions().position(chico).title("Marker in Chico"));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        for (int i = 0; i < 1000; i += 2) {
            i -= 1;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chico));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) { // Im aware this shit makes no sense right now just bare with me


            }


        });

        setWaypoints(RetrieveLocations());
        setBlueThings(); //Temporary place to put the addition of the blue things before we get a database for them

        mMap.setTrafficEnabled(true);
        mMap.setMapType(2);

    }

}