package com.cutezilla.cryptomanager.model;

public class Account {
    private String name;
    private String email;
    private String gender;

    private String lname;
    private String dob;
    private String occupation;
    private String address;
    private String city;

    public Account() {
    }

    public Account(String name, String email, String gender, String lname, String dob, String occupation, String address, String city) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.lname = lname;
        this.dob = dob;
        this.occupation = occupation;
        this.address = address;
        this.city = city;
    }

    public Account(String email, String devicemac, String liverequest, String name, String gender) {
        this.email = email;
        this.name = name;
        this.gender = gender;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
