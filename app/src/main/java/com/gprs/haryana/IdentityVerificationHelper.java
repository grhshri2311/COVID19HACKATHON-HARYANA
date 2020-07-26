package com.gprs.haryana;

public class IdentityVerificationHelper {

    String name, phone, email, role, address, dob, passphoto, proof, gender, status, comment;

    public IdentityVerificationHelper() {
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassphoto() {
        return passphoto;
    }

    public void setPassphoto(String passphoto) {
        this.passphoto = passphoto;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public IdentityVerificationHelper(String name, String phone, String email, String role, String address, String dob, String passphoto, String proof, String gender, String status, String comment) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.dob = dob;
        this.passphoto = passphoto;
        this.proof = proof;
        this.gender = gender;
        this.status = status;
        this.comment = comment;
    }

}
