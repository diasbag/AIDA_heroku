package com.hackathon.mentor.controllers;

import java.security.Principal;

import javax.validation.Valid;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupMenteeRequest;
import com.hackathon.mentor.payload.request.SignupMentorRequest;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.AuthenticationService;
import com.hackathon.mentor.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import com.hackathon.mentor.payload.request.LoginRequest;
import com.hackathon.mentor.payload.response.JwtResponse;
import com.hackathon.mentor.payload.response.MessageResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final RegistrationService registrationService;
	private final AuthenticationService authenticationService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		JwtResponse jwtResponse = authenticationService.authUser(loginRequest);
		return ResponseEntity.ok(jwtResponse);
	}


	@PostMapping("/mentor/signup")
	public ResponseEntity<?> registerMentor(@Valid @RequestBody SignupMentorRequest signupMentorRequest) {
		MessageResponse messageResponse = registrationService.regMentor(signupMentorRequest);
		return ResponseEntity.ok(messageResponse);
	}

	@PostMapping("/mentee/signup")
	public ResponseEntity<?> registerMentee(@Valid @RequestBody SignupMenteeRequest signupMenteeRequest) {
		MessageResponse messageResponse = registrationService.regMentee(signupMenteeRequest);
		return ResponseEntity.ok(messageResponse);
	}
	@GetMapping("/user/role")
	public ResponseEntity<?> getRole(Principal principal) {
		User user = userRepository.getByEmail(principal.getName());
		return ResponseEntity.ok(user.getRoles());
	}
}
