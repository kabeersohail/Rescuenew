package com.example.sohail.rescue;


import com.google.android.gms.maps.model.LatLng;
import android.location.Location;

public class DistenceBwTwoLocations {
    public static double Distence(double Mylat,double MyLong,double ELat,double ELong){
        LatLng latLngA = new LatLng(Mylat,MyLong);
        LatLng latLngB = new LatLng(ELat,ELong);

        Location locationA = new Location("point A");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);

        double distance = locationA.distanceTo(locationB);
        return distance;
    }
}
