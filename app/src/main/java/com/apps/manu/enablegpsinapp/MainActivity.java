package com.apps.manu.enablegpsinapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    boolean isGPS;
    LocationManager mLocationManager;
    static AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestGps();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TestGps();
    }

    void TestGps(){

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPS = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        int MIN_TIME_BW_UPDATES = 5;
        int MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;

        if (!isGPS) {
            Toast.makeText(getApplicationContext(), "GPS is not anabled", Toast.LENGTH_SHORT).show();
            isGPS = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d("GPS", "" + isGPS);

            // checking the GPS status on the phone

            if (isGPS == false) {
                //Enable GPS pop-up notification.
                adb = new AlertDialog.Builder(MainActivity.this);

                // set title
                adb.setTitle("Enable GPS?");

                // set dialog message
                adb.setMessage("Enable GPS to get full function from the app.");
                adb.setCancelable(false);

                //Yes Button
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivityForResult(
                                new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                0);
                        Log.d("Life_splash", "onActivityresults called");

                        isGPS = mLocationManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    }
                });

                //No Button
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

                // create GPS Alert [Pop-up]
                AlertDialog alertDialog = adb.create();

                // show it
                alertDialog.show();

            }

        } else {
            Toast.makeText(getApplicationContext(), "GPS is enabled", Toast.LENGTH_SHORT).show();
            Location gpsLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(gpsLocation==null){
                gpsLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                gpsLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            Toast.makeText(getApplicationContext(), "Current Location is"+gpsLocation, Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Toast.makeText(getApplicationContext(),"current location is"+location,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

}

