package com.example.bzoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Car> cars;
    TextView heading;
    Switch active;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cars = new ArrayList<>();
        heading = (TextView) findViewById(R.id.heading);
        active = (Switch) findViewById(R.id.active);

        roleNativeLyout();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            if(Role.role.equals("owner")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();

            } if (Role.role.equals("rider")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Rider_Home_Fragment()).commit();


            } if(Role.role.equals("driver")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RiderSetPicknDrop()).commit();
            } if(Role.role.equals("chauffeur") && Role.status == false){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChaufferMainFragment()).commit();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Role.role.equals("rider")){

            showAlertDialog();
            active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(active.isChecked()){

                        alertDialog.show();
                    }
                }
            });

        }
        if(Role.role.equals("chauffeur") && Role.status == true){
            showAlertDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.show();
                }
            },1000L);

        }



    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.ride_req_popup, null);
        Button accept = (Button) promptsView.findViewById(R.id.next);
        builder.setView(promptsView);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show it
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PickupRide.class);
                startActivity(intent);
            }
        });

        final ProgressBar progressBar = (ProgressBar)  promptsView.findViewById(R.id.progress);
        ValueAnimator animator = ValueAnimator.ofInt(0, progressBar.getMax());
        animator.setDuration(10000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                progressBar.setProgress((Integer)animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // start your activity here
                alertDialog.dismiss();
            }
        });
        animator.start();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void roleNativeLyout(){
        if (Role.role != null){
            if(Role.role.equals("rider")){
                heading.setText("Home");
            }else if(Role.role.equals("owner")){
                active.setVisibility(View.GONE);
            }else if(Role.role.equals("driver")){
                active.setVisibility(View.GONE);
                heading.setText("Bzoom");
            }else if(Role.role.equals("chauffeur")){
                if(Role.status){
                    heading.setText("Start rides");
                    active.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Rider_Home_Fragment()).commit();
                }else {
                    active.setVisibility(View.GONE);
                    heading.setText("Bzoom");
                }
            }
        }
    }

    public ArrayList<Car> Cars_list(){

        Car car  = new Car("Faw",4,"165jfyu","blue",1);
        Car car1  = new Car("Faw",3,"165jfyu","blue",1);
        Car car3  = new Car("Faw",4,"165jfyu","blue",0);
        Car car4  = new Car("Faw",2,"165jfyu","blue",0);
        Car car5  = new Car("Faw",5,"165jfyu","blue",1);

        cars.add(car);
        cars.add(car1);
        cars.add(car3);
        cars.add(car4);
        cars.add(car5);
        return cars ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
