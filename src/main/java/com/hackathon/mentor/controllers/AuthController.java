package com.hackathon.mentor.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupMenteeRequest;
import com.hackathon.mentor.payload.request.SignupMentorRequest;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.mentor.payload.request.LoginRequest;
import com.hackathon.mentor.payload.request.SignupRequest;
import com.hackathon.mentor.payload.response.JwtResponse;
import com.hackathon.mentor.payload.response.MessageResponse;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.security.jwt.JwtUtils;
import com.hackathon.mentor.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	MentorRepository mentorRepository;

	@Autowired
	MenteeRepository menteeRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
												 roles));
	}


	@PostMapping("/mentor/signup")
	public ResponseEntity<?> registerMentor(@Valid @RequestBody SignupMentorRequest signupMentorRequest) {
		if (userRepository.existsByEmail(signupMentorRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		User user = new User(signupMentorRequest.getFirstname(),
				signupMentorRequest.getLastname(),
				signupMentorRequest.getEmail(),
				encoder.encode(signupMentorRequest.getPassword()));
		Role role = roleRepository.findByName(ERole.ROLE_MENTOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));;
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		userRepository.save(user);
		Mentor mentor = new Mentor();
		mentor.setAge(signupMentorRequest.getAge());
		mentor.setIin(signupMentorRequest.getIin());
		mentor.setMajor(signupMentorRequest.getMajor());
		mentor.setUniversity(signupMentorRequest.getUniversity());
		mentor.setCountry(signupMentorRequest.getCountry());
		mentor.setNumber(signupMentorRequest.getNumber());
		mentor.setWork(signupMentorRequest.getWork());
		mentor.setUserInfo(signupMentorRequest.getUserInfo());
		mentor.setSchool(signupMentorRequest.getSchool());
		mentor.setUser(user);

		mentorRepository.save(mentor);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/mentee/signup")
	public ResponseEntity<?> registerMentee(@Valid @RequestBody SignupMenteeRequest signupMenteeRequest) {
		if (userRepository.existsByEmail(signupMenteeRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		User user = new User(signupMenteeRequest.getFirstname(),
				signupMenteeRequest.getLastname(),
				signupMenteeRequest.getEmail(),
				encoder.encode(signupMenteeRequest.getPassword()));
		Role role = roleRepository.findByName(ERole.ROLE_MENTEE).orElseThrow(() -> new RuntimeException("Error: Role is not found."));;
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		userRepository.save(user);

		Mentee mentee = new Mentee();
		mentee.setIin(signupMenteeRequest.getIin());
		mentee.setNumber(signupMenteeRequest.getNumber());
		mentee.setGrade(signupMenteeRequest.getGrade());
		mentee.setAchievements(signupMenteeRequest.getAchievements());
		mentee.setSchool(signupMenteeRequest.getSchool());
		mentee.setUser(user);

		menteeRepository.save(mentee);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}
