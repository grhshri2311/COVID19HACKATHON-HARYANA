package com.gprs.haryana;


public class CounsellorHelper {

    UserRegistrationHelper u;
    String message, time, counsellor, fixedtime;
    String status;

    public CounsellorHelper(UserRegistrationHelper u, String message, String time, String status) {
        this.u = u;
        this.message = message;
        this.time = time;
        this.status = status;
    }

    public String getCounsellor() {
        return counsellor;
    }

    public void setCounsellor(String counsellor) {
        this.counsellor = counsellor;
    }

    public String getFixedtime() {
        return fixedtime;
    }

    public void setFixedtime(String fixedtime) {
        this.fixedtime = fixedtime;
    }

    public CounsellorHelper() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserRegistrationHelper getU() {
        return u;
    }

    public void setU(UserRegistrationHelper u) {
        this.u = u;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
