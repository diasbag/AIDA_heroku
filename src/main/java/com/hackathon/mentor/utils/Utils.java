package com.hackathon.mentor.utils;

import com.hackathon.mentor.exceptions.AuthenticationFailed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Utils {

    public static String getEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails;
        if(principal instanceof String) {
            throw new AuthenticationFailed((String) principal);
        } else {
            userDetails = (UserDetails) principal;
        }
        return userDetails.getUsername();
    }
}