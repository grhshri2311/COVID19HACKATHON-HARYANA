package com.gprs.haryana;

import java.util.ArrayList;


public class UserSelfAssessHelper {

    String name, phone, email, role;
    private int status;
    private ArrayList<String> ques = null, answer = null;
    double lat, lon;


    public UserSelfAssessHelper(String name, String phone, String email, String role, double lat, double lon, ArrayList<String> ques, ArrayList<String> answer, int status) {
        this.lat = lat;
        this.lon = lon;
        this.answer = answer;
        this.ques = ques;
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserSelfAssessHelper() {

    }

    public ArrayList<String> getQues() {
        return ques;
    }

    public void setQues(ArrayList<String> ques) {
        this.ques = ques;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}
