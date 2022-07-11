package com.hackathon.mentor.payload.request;

import javax.validation.constraints.*;
import java.util.Set;

public class SignupMenteeRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    @NotNull
    private String firstname;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 20)
    private String lastname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

    @NotNull
    @NotBlank
    private String number;

    @NotNull
    @NotBlank
    private String iin;

    @NotNull
    @NotBlank
    private String school;

    private int grade;

    private String achievements;




    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }



    public String getNumber() {
        return number;
    }

    public String getIin() {
        return iin;
    }



    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }



    public void setNumber(String number) {
        this.number = number;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
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
