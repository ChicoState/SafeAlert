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
import android.widget.Toast;

import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.GeofencingClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;



public class MapsActivity extends MainActivity implements OnMapReadyCallback{

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

        EditText report = findViewById(R.id.editText);
        String input = report.getText().toString();
        String[] inputArray = input.split(",");
        double latitude = Double.parseDouble(inputArray[0]);
        double longitude = Double.parseDouble(inputArray[1]);

        //THIS IS WHERE THE REPORTED LOCATION WILL BE ADDED TO THE DATABASE HOPEFULLY

        Vector<LonLat> temp = new Vector<>();
        LonLat templonlat = new LonLat();
        templonlat.setLatitude(latitude);
        templonlat.setLongitude(longitude);
        temp.add(templonlat);

        DatabaseHandler handler=new DatabaseHandler(MapsActivity.this);
        handler.addGPS(latitude,longitude);

        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(input)

                .setCircularRegion(
                        latitude,
                        longitude,
                        20
                )
                .setExpirationDuration(1000000000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getIncomingIntent();
        /*DB STUFF */

        //getLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMarkerPoints = new ArrayList<>();

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
        reportLonLat(view);

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
        geofencingClient = LocationServices.getGeofencingClient(this);


    // Add a marker in Sydney and move the camera
        LatLng chico = new LatLng(39.7285, -121.8375);
        mMap.addMarker(new MarkerOptions().position(chico).title("Marker in Chico"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chico));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) { // Im aware this shit makes no sense right now just bare with me

                    // Already two locations
                    if(mMarkerPoints.size()>1){
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
                    if(mMarkerPoints.size()==1){
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }else if(mMarkerPoints.size()==2){
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    // Add new marker to the Google Map Android API V2
                    mMap.addMarker(options);

                    // Checks, whether start and end locations are captured
                    if(mMarkerPoints.size() >= 2){
                        mOrigin = mMarkerPoints.get(0);
                        mDestination = mMarkerPoints.get(1);
                        drawRoute();
                    }

                }


        });

        setWaypoints(RetrieveLocations());
        setBlueThings(); //Temporary place to put the addition of the blue things before we get a database for them

        mMap.setTrafficEnabled(true);
        mMap.setMapType(2);

    }

    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        String google_maps_key = "AIzaSyB-lVKAaaAgSpzcPmCLUgmbkiIiFzCjpoU";

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + google_maps_key;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
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
