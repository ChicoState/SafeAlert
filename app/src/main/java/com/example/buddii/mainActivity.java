/***
package com.example.buddii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import com.example.buddii.navigationDrawer;


//public class mainActivity extends navigationDrawer {
public class mainActivity extends AppCompatActivity{

    private Button btnFSR;
    private Button btnBab;
    private Button btnTut;
    private Button btnTst;
    TextView UserTexViewVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnFSR = findViewById(R.id.findRoute);
        btnBab = findViewById(R.id.beABuddii);
        btnTut = findViewById(R.id.Tutorial);
        // btnTst = findViewById(R.id.testdb);
        btnFSR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToChooseRoute();
            }
        });
        btnBab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToBaB();
            }
        });
        //getLocationPermission();

        // UserTexViewVariable=(TextView)findViewById(R.id.bud2);
        btnTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTut();
            }
        });

        navigationDrawer nav_draw = new navigationDrawer();


    }

    private void moveToBaB()
    {
        Intent intent = new Intent (mainActivity.this, select_bud.class);
        startActivity(intent);
    }

    private void moveToChooseRoute()
    {
        Intent intent = new Intent(mainActivity.this, chooseRoute.class);
        startActivity(intent);
    }

    private void moveToTut(){
        Intent intent = new Intent(mainActivity.this, TutorialActivity.class);
        startActivity(intent);
    }

    private void moveToDb(){
        Intent intent = new Intent(mainActivity.this, dbActivity.class);
        startActivity(intent);
    }


}
****/

package com.example.buddii;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class mainActivity extends AppCompatActivity {

    private Button btnFSR;
    private Button btnBab;
    private Button btnTut;
    private Button btnTst;
    TextView UserTexViewVariable;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFSR = findViewById(R.id.findRoute);
        btnBab = findViewById(R.id.beABuddii);
        btnTut = findViewById(R.id.Tutorial);
        // btnTst = findViewById(R.id.testdb);
        btnFSR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToChooseRoute();
            }
        });
        btnBab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToBaB();
            }
        });
        //getLocationPermission();

        // UserTexViewVariable=(TextView)findViewById(R.id.bud2);
        btnTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTut();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_map, R.id.tutorial,
                R.id.nav_settings, R.id.nav_profile, R.id.nav_map_user)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    private void moveToBaB()
    {
        Intent intent = new Intent (mainActivity.this, select_bud.class);
        startActivity(intent);
    }

    private void moveToChooseRoute()
    {
        Intent intent = new Intent(mainActivity.this, chooseRoute.class);
        startActivity(intent);
    }

    private void moveToTut(){
        Intent intent = new Intent(mainActivity.this, TutorialActivity.class);
        startActivity(intent);
    }

    private void moveToDb(){
        Intent intent = new Intent(mainActivity.this, dbActivity.class);
        startActivity(intent);
    }

    /**NAVIGATION SIDE BAR**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
