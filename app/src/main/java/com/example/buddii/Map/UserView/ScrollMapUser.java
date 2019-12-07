package com.example.buddii.Map.UserView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.buddii.Freakout;
import com.example.buddii.MainActivity;
import com.example.buddii.Map.DirectionsJSONParser;
import com.example.buddii.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.UUID;
import java.util.Vector;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ScrollMapUser extends AppCompatActivity implements OnMapReadyCallback {

    final int REQUEST_PERMISSION_CODE = 1000;
    MediaPlayer mediaPlayer = new MediaPlayer();
    MediaRecorder mediaRecorder;
    String pathSave;
    Button btnPlay, btnRecord, btnStop, btnStopRecord;
    Context context;
    int brightness;
    LinearLayout UserTabInfo, UserTabHome, UserTabReport, UserTabRoute,UserTabChat;
    Button UserHome, UserRoute;
    private FusedLocationProviderClient client;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    private GeofencingClient geofencingClient;
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = null;
    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private LatLng curr = new LatLng(0,0);
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

    protected void requestMediaPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }

    protected boolean checkMediaPermissions() {
        Toast.makeText(ScrollMapUser.this, "FIRST TEST", Toast.LENGTH_SHORT).show();
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_DENIED && record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    protected void setupRecorder() {

        btnRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(ScrollMapUser.this, "TEST\n\n\n\n\n\n", Toast.LENGTH_SHORT).show();
                pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                setupMediaRecorder();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                btnPlay.setEnabled(false);
                btnStop.setEnabled(false);

                Toast.makeText(ScrollMapUser.this, "Recording...", Toast.LENGTH_SHORT).show();

            }

        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(true);
                btnPlay.setEnabled(true);
                btnStop.setEnabled(false);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnStop.setEnabled(true);
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(false);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(pathSave);
                    mediaPlayer.prepare();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(ScrollMapUser.this, "Playing...", Toast.LENGTH_SHORT).show();

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnStopRecord.setEnabled(false);
                btnRecord.setVisibility(View.INVISIBLE);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }
            }


        });
    }

    private Vector<ScrollMapUser.LonLat> RetrieveLocations(){
        //put all of the longitudes and latitudes from the database
        //into a vector of type LonLat defined at the top
        Vector<ScrollMapUser.LonLat> temp = new Vector<>();
        return temp;
    }

    private void setWaypoints(Vector<ScrollMapUser.LonLat> x) {

        for(int i = 0; i < x.size(); i++) {
            ScrollMapUser.LonLat temp = x.get(i);
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

    private void getCurrLocation(){
        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(ScrollMapUser.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

        }

        client.getLastLocation().addOnSuccessListener(ScrollMapUser.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addCircle(new CircleOptions().center(curr)
                            .radius(1)
                            .strokePattern(PATTERN_POLYLINE_DOTTED)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.CYAN));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));

                    TextView textView = findViewById(R.id.locationUser);
                    textView.setText(location.toString());
                }else{
                    Toast.makeText(ScrollMapUser.this, "FUCK YOU", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_map_user);
        Toolbar toolbarUser = findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbarUser);
        requestPermissions();
        getCurrLocation();

        //Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 254);

        btnPlay = findViewById(R.id.btnPlay);
        btnRecord = (Button) findViewById(R.id.btnRecord);
        btnStop = findViewById(R.id.btnStop);
        btnStopRecord = findViewById(R.id.btnStopRecord);

        if (checkMediaPermissions()) {
            setupRecorder();
        } else {
            requestMediaPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapUser);
        mapFragment.getMapAsync(this);
        mMarkerPoints = new ArrayList<>();

    }

     private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    public void onHomeClick(View view) {
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(VISIBLE);
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
        UserTabChat.setVisibility(VISIBLE);
        UserTabInfo.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
    }
    public void onRouteClick(View view){
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(VISIBLE);
        UserTabRoute.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
    }

    public void onReportClick(View view){
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(VISIBLE);
        UserTabReport.setVisibility(VISIBLE);
        UserTabHome.setVisibility(GONE);
        UserTabRoute.setVisibility(GONE);
        UserTabInfo.setVisibility(GONE);
    }

    public void onAcceptRouteClick(View view){
        UserTabInfo = findViewById(R.id.UserTabInfo);
        UserTabHome = findViewById(R.id.UserTabHome);
        UserTabReport = findViewById(R.id.UserTabReport);
        UserTabRoute = findViewById(R.id.UserTabRoute);
        UserHome = findViewById(R.id.UserHome);
        UserRoute = findViewById(R.id.UserRoute);
        UserTabChat = findViewById(R.id.UserTabChat);
        UserTabChat.setVisibility(VISIBLE);
        UserTabRoute.setVisibility(GONE);
        UserTabHome.setVisibility(VISIBLE);
        UserTabInfo.setVisibility(GONE);
        UserTabReport.setVisibility(GONE);
        UserHome.setVisibility(VISIBLE);
        UserRoute.setVisibility(GONE);
    }

    public void onCurrLocClick(View view){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
    }

    public void onBigAlertEnergyClick(View view) {
        Toast.makeText(this, "Oh shit a rat", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ScrollMapUser.this, Freakout.class);
        startActivity(intent);

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

    public void onMapSearch(View view) {
        EditText locationSearch = findViewById(R.id.RouteManual);
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
        LatLng Chico = new LatLng(39.7285, -121.8375);
        mMap.addMarker(new MarkerOptions().position(Chico).title("Marker in Chico"));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Chico));

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

        ScrollMapUser.DownloadTask downloadTask = new ScrollMapUser.DownloadTask();

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

            ScrollMapUser.ParserTask parserTask = new ScrollMapUser.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

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
                //initRecyclerView(parser.getPoints());
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

}
