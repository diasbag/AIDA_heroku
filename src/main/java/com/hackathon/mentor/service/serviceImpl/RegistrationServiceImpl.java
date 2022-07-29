package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountBadRequest;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.payload.response.MessageResponse;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public MessageResponse regMentor(SignupUpdateMentorRequest signupUpdateMentorRequest) {
        log.info("registering mentor ...");
        if (userRepository.existsByEmail(signupUpdateMentorRequest.getEmail())) {
            throw new AccountBadRequest("Email is already in use!");
        }
        User user = new User();
        user.setFirstname(signupUpdateMentorRequest.getFirstname());
        user.setMiddlename(signupUpdateMentorRequest.getMiddlename());
        user.setLastname(signupUpdateMentorRequest.getLastname());
        user.setPassword(encoder.encode(signupUpdateMentorRequest.getPassword()));
        user.setEmail(signupUpdateMentorRequest.getEmail());
        user.setTelegram(signupUpdateMentorRequest.getTelegram());
        user.setStatus(true);
        user.setMiddlename(signupUpdateMentorRequest.getMiddlename());
        Role role = roleRepository.findByName(ERole.ROLE_MENTOR).orElseThrow(() ->
                new AccountNotFound("Error: Role is not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        Mentor mentor = new Mentor();
        mentor.setAge(signupUpdateMentorRequest.getAge());
        mentor.setIin(signupUpdateMentorRequest.getIin());
        mentor.setCountry(signupUpdateMentorRequest.getCountryOfResidence());
        mentor.setWork(signupUpdateMentorRequest.getWork());
        mentor.setUserInfo(signupUpdateMentorRequest.getUserInfo());
        mentor.setSchool(signupUpdateMentorRequest.getSchool());
        mentor.setUser(user);
        mentor.setYearOfGraduation(signupUpdateMentorRequest.getYearOfGraduation());
        mentor.setSubjectOfInterest1(signupUpdateMentorRequest.getSubjectOfInterest1());
        mentor.setSubjectOfInterest2(signupUpdateMentorRequest.getSubjectOfInterest2());
        mentor.setCountryOfBachelorsUniversity(signupUpdateMentorRequest.getCountryOfBachelorsUniversity());
        mentor.setCountryOfMastersUniversity(signupUpdateMentorRequest.getCountryOfMastersUniversity());
        mentor.setBachelorsMajor(signupUpdateMentorRequest.getBachelorsMajor());
        mentor.setMastersMajor(signupUpdateMentorRequest.getMastersMajor());
        mentor.setBachelorsUniversity(signupUpdateMentorRequest.getBachelorsUniversity());
        mentor.setMastersUniversity(signupUpdateMentorRequest.getMastersUniversity());
        mentorRepository.save(mentor);
        log.info("mentor was registered <<<");
        return new MessageResponse("User registered successfully!");
    }

    @Override
    public MessageResponse regMentee(SignupUpdateMenteeRequest signupUpdateMenteeRequest) {
        log.info("registering mentee ...");
        if (userRepository.existsByEmail(signupUpdateMenteeRequest.getEmail())) {
            throw new AccountBadRequest("Email is already in use!");
        }
        User user = new User();
        user.setFirstname(signupUpdateMenteeRequest.getFirstname());
        user.setMiddlename(signupUpdateMenteeRequest.getMiddlename());
        user.setLastname(signupUpdateMenteeRequest.getLastname());
        user.setPassword(encoder.encode(signupUpdateMenteeRequest.getPassword()));
        user.setEmail(signupUpdateMenteeRequest.getEmail());
        user.setTelegram(signupUpdateMenteeRequest.getTelegram());
        user.setStatus(true);
        Role role = roleRepository.findByName(ERole.ROLE_MENTEE).orElseThrow(() ->
                new AccountNotFound("Role is not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        Mentee mentee = new Mentee();
        mentee.setIin(signupUpdateMenteeRequest.getIin());
        mentee.setAge(signupUpdateMenteeRequest.getAge());
//        mentee.setNumber(signupUpdateMenteeRequest.getNumber());
        mentee.setSchool(signupUpdateMenteeRequest.getSchool());
        mentee.setUser(user);
        mentee.setSubjectOfInterest1(signupUpdateMenteeRequest.getSubjectOfInterest1());
        mentee.setSubjectOfInterest2(signupUpdateMenteeRequest.getSubjectOfInterest2());

        menteeRepository.save(mentee);
        log.info("mentee was registered <<<");
        return new MessageResponse("User registered successfully!");
    }

}