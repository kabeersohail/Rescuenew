package com.example.sohail.rescue;

/**
 * Created by DELL on 26-Mar-18.
 */

public class Location {
    String Name;
    double Latitude,Longitude;
    int Seconds;

    public Location(){}

    public Location(String Name,double Latitude,double Longitude,int seconds){
        this.Name=Name;
        this.Latitude=Latitude;
        this.Longitude=Longitude;
        this.Seconds = seconds;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getSeconds() {
        return Seconds;
    }

    public void setSeconds(int seconds) {
        Seconds = seconds;
    }
}
