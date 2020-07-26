package com.gprs.haryana;


import java.util.ArrayList;

public class AssignWorkHelper {

    String time;
    String status;
    String monitorname, monitorphone, monitorrole, title, desc, startdate, duedate, contact, priority, leader;
    ArrayList<String> name, role, phone, comment, commentname;
    Double lat, lon;


    public String getMonitorname() {
        return monitorname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMonitorname(String monitorname) {
        this.monitorname = monitorname;
    }

    public ArrayList<String> getCommentname() {
        return commentname;
    }

    public void setCommentname(ArrayList<String> commentname) {
        this.commentname = commentname;
    }

    public AssignWorkHelper(String monitorname, String monitorphone, String monitorrole, String title, String desc, String startdate, String duedate, String contact, String priority, String leader, ArrayList<String> name, ArrayList<String> role, ArrayList<String> phone, Double lat, Double lon, String status, String time) {
        this.monitorname = monitorname;
        this.monitorphone = monitorphone;
        this.monitorrole = monitorrole;
        this.title = title;
        this.desc = desc;
        this.startdate = startdate;
        this.duedate = duedate;
        this.contact = contact;
        this.priority = priority;
        this.leader = leader;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.lat = lat;
        this.lon = lon;
        this.status = status;
        this.time = time;
        comment = new ArrayList<>();
        commentname = new ArrayList<>();
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }

    public String getMonitorphone() {
        return monitorphone;
    }

    public void setMonitorphone(String monitorphone) {
        this.monitorphone = monitorphone;
    }

    public String getMonitorrole() {
        return monitorrole;
    }

    public void setMonitorrole(String monitorrole) {
        this.monitorrole = monitorrole;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getRole() {
        return role;
    }

    public void setRole(ArrayList<String> role) {
        this.role = role;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public AssignWorkHelper() {
    }


}
