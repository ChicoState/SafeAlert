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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.DatabaseHandler;
import com.example.buddii.Map.AlertItem;
import com.example.buddii.Map.AlertRecyclerAdapter;
import com.example.buddii.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AlertRecyclerAdapter mAdapter;
    ArrayList<AlertItem> alertList = new ArrayList<>();


    private int mLocationPermissionGranted = 0;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Button reportButton;
    private Button searchButton;

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
        DatabaseHandler dbHandlerAlert = new DatabaseHandler(ScrollMapBuddii.this);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime time = LocalTime.now();
            //Toast.makeText(this, "Test at: "+time, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, ">>>"+dbHandlerAlert.loadFlag("0")+"<<<", Toast.LENGTH_SHORT).show();
                if (dbHandlerAlert.loadFlag("0").equals("R") || dbHandlerAlert.loadFlag("0").equals("R")) {
                    alertList.add(new AlertItem(R.drawable.left, "User Has Selected Route", "" + time));
                } else if (dbHandlerAlert.loadFlag("0").equals("A")) {
                    alertList.add(new AlertItem(R.drawable.left, "USER HAS HIT THE ALERT BUTTON", "" + time));
                    NestedScrollView red = findViewById(R.id.buddiiScrollView);
                    red.setBackgroundColor(Color.RED);
                    DatabaseHandler dbHandler = new DatabaseHandler(ScrollMapBuddii.this);
                    String location[] = dbHandler.loadGPS("0");
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), 15));
                } else if (dbHandlerAlert.loadFlag("0").equals("L")) {
                    alertList.add(new AlertItem(R.drawable.left, "User has reported and unsafe location", "" + time));
                } else if (dbHandlerAlert.loadFlag("0").equals("B")) {
                    alertList.add(new AlertItem(R.drawable.left, "User has dropped you", "" + time));
                } else if (dbHandlerAlert.loadFlag("0").equals("U")) {
                    alertList.add(new AlertItem(R.drawable.left, "User has entered an Unsafe Area", "" + time));
                    DatabaseHandler dbHandler = new DatabaseHandler(ScrollMapBuddii.this);
                    String location[] = dbHandler.loadGPS("0");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), 15));
                } else if (dbHandlerAlert.loadFlag("0").equals("D")) {
                    alertList.add(new AlertItem(R.drawable.left, "User has dropped their route", "" + time));
                } else {
                    //Toast.makeText(this, "Dint work", Toast.LENGTH_SHORT).show();
                    return;
                }

        }

        mRecyclerView = findViewById(R.id.alertRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AlertRecyclerAdapter(alertList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Toast.makeText(this, "5 SECOND TEST", Toast.LENGTH_SHORT).show();
        //alertView();
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
    }


    public void onHomeClick(View view) {
        BuddiiTabUser = findViewById(R.id.BuddiiTabUser);
        BuddiiTabUser.setVisibility(View.GONE);
        BuddiiTabHome = findViewById(R.id.BuddiiTabHome);
        BuddiiTabHome.setVisibility(View.VISIBLE);
    }

    public void onUserClick(View view) {
        BuddiiTabUser = findViewById(R.id.BuddiiTabUser);
        BuddiiTabUser.setVisibility(View.VISIBLE);
        BuddiiTabHome = findViewById(R.id.BuddiiTabHome);
        BuddiiTabHome.setVisibility(GONE);
    }

    public void onUserLocClick(View view) {
        DatabaseHandler dbHandler = new DatabaseHandler(ScrollMapBuddii.this);
        String location[] = dbHandler.loadGPS("0");
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]))));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //geofencingClient = LocationServices.getGeofencingClient(this);


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

        //setWaypoints(RetrieveLocations());
        setBlueThings(); //Temporary place to put the addition of the blue things before we get a database for them

        mMap.setTrafficEnabled(true);
        mMap.setMapType(2);

    }

    public void onRefreshClick(View view) {
        dataLoop();
        DatabaseHandler dbHandler = new DatabaseHandler(ScrollMapBuddii.this);
        String location[] = dbHandler.loadGPS("0");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), 15));
    }
}