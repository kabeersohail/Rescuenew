package com.example.sohail.rescue;

public class Rescuer {
    double latitude,logitude;
    String name,uid;

    public Rescuer(double latitude, double logitude, String name,String uid) {
        this.latitude = latitude;
        this.logitude = logitude;
        this.name = name;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setLogitude(double logitude) {
        this.logitude = logitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
