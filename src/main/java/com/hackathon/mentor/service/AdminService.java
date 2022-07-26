package com.hackathon.mentor.service;

import com.hackathon.mentor.models.User;

import java.util.List;

public interface AdminService {
    void createAdmin();

    void createNewAdmin(String firstName, String lastName, String email, char[] password);

    List<User> findAllAdmins();

    void deactivateAccount(String email);
}
