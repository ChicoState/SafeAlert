package com.example.buddii;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class MapsActivity extends MainActivity implements OnMapReadyCallback{

    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = null;
    private GoogleMap mMap;
    ArrayList markerPoints = new ArrayList();




    private int mLocationPermissionGranted = 0;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Button reportButton;
    private Button searchButton;

    public class LonLat {
        private double longitude;
        private double latitude;
        public void setLongitude(double lon){
            this.longitude = lon;
        }
        public void setLatitude(double lat){
            this.latitude = lat;
        }
        public double getLat(){
            return this.latitude;
        }
        public double getLon(){
            return this.longitude;
        }
    }

    private Vector<LonLat> RetrieveLocations(){
        //put all of the longitudes and latitudes from the database
        //into a vector of type LonLat defined at the top
        Vector<LonLat> temp = new Vector<>();
        return temp;
    }

    private void setWaypoints(Vector<LonLat> x) {

        for(int i = 0; i < x.size(); i++) {
            LonLat temp = x.get(i);
            mMap.addCircle(new CircleOptions().center(new LatLng(temp.getLon(),temp.getLat()))
                    .radius(20)
                    .strokePattern(PATTERN_POLYLINE_DOTTED)
                    .strokeColor(Color.RED)
                    .fillColor(Color.YELLOW));;
        }
    }

    private void setBlueThings(){ //THIS IS TEMPORARY YOU SONS OF BITCHES

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

    public void reportLonLat(View view) {
        //EditText report = findViewById(R.id.reportLocation);
        //String input = report.getText().toString();
        //String[] inputArray = input.split(",");
        //double latitude = Double.parseDouble(inputArray[1]);
        //double longitude = Double.parseDouble(inputArray[0]);



        /*DB STUFF */

        //THIS IS WHERE THE REPORTED LOCATION WILL BE ADDED TO THE DATABASE HOPEFULLY
        /*
        Vector<LonLat> temp = new Vector<>();
        LonLat templonlat = new LonLat();
        templonlat.setLatitude(latitude);
        templonlat.setLongitude(longitude);
        temp.add(templonlat);

        setWaypoints(temp);
*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getIncomingIntent();
        /*DB STUFF */
        Double latitude = 11.12345;
        Double longitude = 999.7900000;

        DatabaseHandler handler=new DatabaseHandler(MapsActivity.this);
        handler.addGPS(latitude,longitude);
        /*DB STUFF */

        //getLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reportButton = findViewById(R.id.report);
        searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBack();
            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeView();

            }
        });

    }

    private void changeView(){
        findViewById(R.id.searchField).setVisibility(View.GONE);
        findViewById(R.id.reportField).setVisibility(View.VISIBLE);
    }

    private void changeBack(){
        findViewById(R.id.searchField).setVisibility(View.VISIBLE);
        findViewById(R.id.reportField).setVisibility(View.GONE);
        getIncomingIntent();

    }

    public void onMapSearch(View view) {
        EditText locationSearch = findViewById(R.id.searchText);
        String location = locationSearch.getText().toString();
        List<Address>addressList = null;

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
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng chico = new LatLng(39.7285, -121.8375);
        mMap.addMarker(new MarkerOptions().position(chico).title("Marker in Chico"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chico));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) { // Im aware this shit makes no sense right now just bare with me

                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }

                markerPoints.add(latLng);

                MarkerOptions options = new MarkerOptions();

                options.position(latLng);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                mMap.addMarker(options);

                if (markerPoints.size() >= 2) {
                    LatLng origin = (LatLng) markerPoints.get(0);
                    LatLng dest = (LatLng) markerPoints.get(1);
                }

            }
        });


        setWaypoints(RetrieveLocations());
        setBlueThings(); //Temporary place to put the addition of the blue things before we get a database for them

        mMap.setTrafficEnabled(true);
        mMap.setMapType(2);

    }

    private void getIncomingIntent()
    {
        if(getIntent().hasExtra("buddiiImage") && getIntent().hasExtra("userBuddii"))
        {
            String buddiiImage = getIntent().getStringExtra("buddiiImage");
            String userBuddii = getIntent().getStringExtra("userBuddii");

            //setImage(buddiiImage, userBuddii);
        }
    }

    private void setImage(String buddiiImage, String userBuddii)
    {
        TextView name = findViewById(R.id.userBuddii);
        name.setText(userBuddii);

        /*ImageView image = findViewById(R.id.buddiiImage);
        image.setImageIcon(R.id.buddiiImage);
        */

    }

}
