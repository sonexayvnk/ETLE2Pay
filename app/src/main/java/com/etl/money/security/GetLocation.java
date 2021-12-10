package com.etl.money.security;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLocation {
    public static String get(final Activity activity){
        String strLocation = "";
        GpsTracker gpsTracker;
        try {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        gpsTracker = new GpsTracker(activity);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            strLocation = ""+ longitude ;
            strLocation += "~"+ latitude;
            // Get Address
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(activity, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            //   String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            //  String postalCode = addresses.get(0).getPostalCode();
            //  String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            //================
            strLocation += "~"+country;
            strLocation += "~"+city;
            strLocation += "~"+address;
            //================
        }else{
            gpsTracker.showSettingsAlert();
        }
        return strLocation ;
    }
    private static String getAddress(Activity activity, double latitude, double longitude) {
        // Get Address
/*
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(activity, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            //================
            strLocation += "~"+country;
            strLocation += "~"+city;
            strLocation += "~"+address;
           //================
 */
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("~");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return result.toString();
    }}