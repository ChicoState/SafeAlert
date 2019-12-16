package com.example.buddii.Map.BuddiiView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddii.databaseHandler;
import com.example.buddii.Map.alertItem;
import com.example.buddii.Map.alertRecyclerAdapter;
import com.example.buddii.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;

public class scrollMapBuddii extends AppCompatActivity implements OnMapReadyCallback {

    LinearLayout BuddiiTabUser, BuddiiTabHome;

    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = null;
    private GoogleMap mMap;
    ArrayList<LatLng> mMarkerPoints;
    LinkedList<Geofence> geofenceList = null;
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    alertRecyclerAdapter mAdapter;
    ArrayList<alertItem> alertList = new ArrayList<>();


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
        databaseHandler dbHandlerAlert = new databaseHandler(scrollMapBuddii.this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime time = LocalTime.now();
                if (dbHandlerAlert.loadFlag("0").equals("R") || dbHandlerAlert.loadFlag("0").equals("R")) {
                    alertList.add(new alertItem("User Has Selected Route", "" + time));
                } else if (dbHandlerAlert.loadFlag("0").equals("A")) {
                    alertList.add(new alertItem("USER HAS HIT THE ALERT BUTTON", "" + time));
                    NestedScrollView red = findViewById(R.id.buddiiScrollView);
                    red.setBackgroundColor(Color.RED);
                    databaseHandler dbHandler = new databaseHandler(scrollMapBuddii.this);
                    String location[] = dbHandler.loadGPS("0");
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), 15));
                } else if (dbHandlerAlert.loadFlag("0").equals("L")) {
                    alertList.add(new alertItem("User has reported and unsafe location", "" + time));
                } else if (dbHandlerAlert.loadFlag("0").equals("B")) {
                    alertList.add(new alertItem("User has dropped you", "" + time));
                } else if (dbHandlerAlert.loadFlag("0").equals("U")) {
                    alertList.add(new alertItem("User has entered an Unsafe Area", "" + time));
                    databaseHandler dbHandler = new databaseHandler(scrollMapBuddii.this);
                    String location[] = dbHandler.loadGPS("0");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), 15));
                } else if (dbHandlerAlert.loadFlag("0").equals("D")) {
                    alertList.add(new alertItem("User has dropped their route", "" + time));
                } else {
                    return;
                }

        }

        mRecyclerView = findViewById(R.id.alertRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new alertRecyclerAdapter(alertList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBuddii);
        setSupportActionBar(toolbar);

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
        databaseHandler dbHandler = new databaseHandler(scrollMapBuddii.this);
        String location[] = dbHandler.loadGPS("0");
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]))));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

        setBlueThings(); //Temporary place to put the addition of the blue things before we get a database for them

        mMap.setTrafficEnabled(true);
        mMap.setMapType(2);

    }

    public void onRefreshClick(View view) {
        dataLoop();
        databaseHandler dbHandler = new databaseHandler(scrollMapBuddii.this);
        String location[] = dbHandler.loadGPS("0");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), 15));
    }
}