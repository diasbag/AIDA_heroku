package com.hackathon.mentor.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "mentors")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private  User user;

    @Min(value = 0, message = "Age < 0")
    @Max(100)
    private int age;

    @Column(name = "phone_number")
    private String number;

    @OneToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @Column(name = "iin")
    private String iin;

    @Column(name = "major")
    private String major;

    @Column(name = "university")
    private String university;

    @Column(name = "country")
    private String country;

    @Column(name = "work")
    private String work;

    @Column(name = "userInfo")
    private String userInfo;

    @Column(name = "school")
    private String school;

    @Column(name = "finish_year")
    private Integer year;

    @Column(name = "subject1")
    private String subject1;

    @Column(name = "subject2")
    private String subject2;

    @ManyToMany
    @Column(name = "mentee_id")
    private Set<Mentee> mentees = new HashSet<>();

    public Mentor(
                  int age, String iin, String number,
                  String major, String university,
                  String country, String work, String userInfo) {

        this.age = age;
        this.number = number;
        this.iin = iin;
        this.major = major;
        this.university = university;
        this.country = country;
        this.work = work;
        this.userInfo = userInfo;
    }

    public Mentor() {

    }

    public Set<Mentee> getMentees() {
        return mentees;
    }

    public Mentee getMentee() {
        List<Mentee> menteeList = new ArrayList<>(mentees);
        for (int i = 0; i < menteeList.size(); i++) {
            return menteeList.get(i);
        }
        return null;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setMentees(Set<Mentee> mentees) {
        this.mentees = mentees;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
