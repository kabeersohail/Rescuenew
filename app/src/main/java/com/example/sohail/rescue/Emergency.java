package com.example.sohail.rescue;

import java.util.Date;

public class Emergency {

    double latitude,longitude;
    String need,phone,commission,uid;
    String time;
    String date;

    public Emergency(){

    }

    public Emergency(double Latitude,double Longitude,String Need,String commission,String phone,String Uid,int Count,String date,String time){
        this.latitude = Latitude;
        this.longitude = Longitude;
        this.need = Need;
        this.commission = commission;
        this.phone = phone;
        this.uid = Uid;
        this.time = time;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}


