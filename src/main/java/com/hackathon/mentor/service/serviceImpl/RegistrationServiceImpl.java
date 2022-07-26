package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountBadRequest;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupMenteeRequest;
import com.hackathon.mentor.payload.request.SignupMentorRequest;
import com.hackathon.mentor.payload.response.MessageResponse;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    @Transactional
    @Override
    public MessageResponse regMentor(SignupMentorRequest signupMentorRequest) {
        log.info("registering mentor ...");
        if (userRepository.existsByEmail(signupMentorRequest.getEmail())) {
            throw new AccountBadRequest("Email is already in use!");
        }
        User user = new User(signupMentorRequest.getFirstname(),
                signupMentorRequest.getLastname(),
                signupMentorRequest.getEmail(),
                encoder.encode(signupMentorRequest.getPassword()));
        user.setStatus(true);
        Role role = roleRepository.findByName(ERole.ROLE_MENTOR).orElseThrow(() ->
                new AccountNotFound("Error: Role is not found"));
        List<Role> roles = new ArrayList<>();
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
        log.info("mentor was registered <<<");
        return new MessageResponse("User registered successfully!");
    }

    @Override
    public MessageResponse regMentee(SignupMenteeRequest signupMenteeRequest) {
        log.info("registering mentee ...");
        if (userRepository.existsByEmail(signupMenteeRequest.getEmail())) {
            throw new AccountBadRequest("Email is already in use!");
        }
        User user = new User(signupMenteeRequest.getFirstname(),
                signupMenteeRequest.getLastname(),
                signupMenteeRequest.getEmail(),
                encoder.encode(signupMenteeRequest.getPassword()));
        user.setStatus(true);
        Role role = roleRepository.findByName(ERole.ROLE_MENTEE).orElseThrow(() ->
                new AccountNotFound("Role is not found"));;
        List<Role> roles = new ArrayList<>();
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
        log.info("mentee was registered <<<");
        return new MessageResponse("User registered successfully!");
    }

}