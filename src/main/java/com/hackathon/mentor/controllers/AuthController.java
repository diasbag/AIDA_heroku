package com.hackathon.mentor.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.ResetPassRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.AuthenticationService;
import com.hackathon.mentor.service.RegistrationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.hackathon.mentor.payload.request.LoginRequest;
import com.hackathon.mentor.payload.response.JwtResponse;
import com.hackathon.mentor.payload.response.MessageResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final RegistrationService registrationService;
	private final AuthenticationService authenticationService;

	private final RoleRepository roleRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		JwtResponse jwtResponse = authenticationService.authUser(loginRequest);
		return ResponseEntity.ok(jwtResponse);
	}


	@PostMapping("/mentor/signup")
	public ResponseEntity<?> registerMentor(@Valid @RequestBody SignupUpdateMentorRequest signupUpdateMentorRequest) {
		MessageResponse messageResponse = registrationService.regMentor(signupUpdateMentorRequest);
		return ResponseEntity.ok(messageResponse);
	}

	@PostMapping("/mentee/signup")
	public ResponseEntity<?> registerMentee(@Valid @RequestBody SignupUpdateMenteeRequest signupUpdateMenteeRequest) {
		MessageResponse messageResponse = registrationService.regMentee(signupUpdateMenteeRequest);
		return ResponseEntity.ok(messageResponse);
	}

	@GetMapping("/user/role")
	public ResponseEntity<?> getRole(Principal principal) {
		User user;
		if(principal != null) {
			user = userRepository.getByEmail(principal.getName());
		} else {
			user = new User();
			Role role = roleRepository.findByName(ERole.ANONYMOUS).orElseThrow(() -> new RuntimeException("Role not found"));
			user.getRoles().add(role);
		}
		return ResponseEntity.ok(user.getRoles());
	}

	@PostMapping("/forgot")
	public ResponseEntity<?> forgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
		return registrationService.forgotPassword(email, request);
	}

	@PostMapping("/reset")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPassRequest request) {
		return registrationService.resetPassword(request);
	}
	@GetMapping("/iin_check/{iin}")
	public ResponseEntity<?> iinCheck(@PathVariable String iin) {
		return registrationService.iinCheck(iin);
	}

}
