package com.hackathon.mentor.payload.request;

import javax.validation.constraints.*;
import java.util.Set;

public class SignupMentorRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String firstname;
    @NotBlank
    @Size(min = 1, max = 20)
    private String lastname;

    @Min(value = 0, message = "Age < 0")
    @Max(100)
    private int age;

    private String number;

    private String iin;

    private String major;

    private String university;

    private String country;

    private String work;

    private String userInfo;

    private String school;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;


    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }



    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public String getNumber() {
        return number;
    }

    public String getIin() {
        return iin;
    }

    public String getMajor() {
        return major;
    }

    public String getUniversity() {
        return university;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getCountry() {
        return country;
    }

    public String getWork() {
        return work;
    }

    public String getUserInfo() {
        return userInfo;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
