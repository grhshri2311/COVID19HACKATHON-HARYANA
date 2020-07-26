package com.gprs.haryana;


public class MaterialCollectionHelper {

    String fname;
    double lat, lon;
    String phone;
    String type, datetime, image;


    public MaterialCollectionHelper() {
    }

    public MaterialCollectionHelper(String type, String datetime, String image) {
        this.type = type;
        this.datetime = datetime;
        this.image = image;
    }

    public MaterialCollectionHelper(String fname, double lat, double lon, String phone, String type, String datetime, String image) {
        this.fname = fname;
        this.lat = lat;
        this.lon = lon;
        this.phone = phone;
        this.type = type;
        this.datetime = datetime;
        this.image = image;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}