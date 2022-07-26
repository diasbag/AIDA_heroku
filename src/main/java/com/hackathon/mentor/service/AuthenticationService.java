package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.LoginRequest;
import com.hackathon.mentor.payload.response.JwtResponse;

public interface AuthenticationService {
    JwtResponse authUser(LoginRequest loginRequest);
}
