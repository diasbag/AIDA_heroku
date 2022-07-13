package com.hackathon.mentor.payload.request;

public class FilterRequest {
    private String country;
    private String major;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
