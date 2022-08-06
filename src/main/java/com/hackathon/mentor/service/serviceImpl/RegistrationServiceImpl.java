package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountBadRequest;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.ResetPassRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.payload.response.MessageResponse;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.RegistrationService;
import com.hackathon.mentor.utils.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;

    private final MailService mailService;
    private final ModelMapper modelMapper = new ModelMapper();
    @Transactional
    @Override
    public MessageResponse regMentor(SignupUpdateMentorRequest signupUpdateMentorRequest) {
        log.info("registering mentor ...");
        if (userRepository.existsByEmail(signupUpdateMentorRequest.getEmail())) {
            throw new AccountBadRequest("Email is already in use!");
        }
        User user = modelMapper.map(signupUpdateMentorRequest, User.class);
        user.setPassword(encoder.encode(signupUpdateMentorRequest.getPassword()));
        user.setRegistrationDate(Date.from(Instant.now()));
        user.setStatus(true);
        Role role = roleRepository.findByName(ERole.ROLE_MENTOR).orElseThrow(() ->
                new AccountNotFound("Error: Role is not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        log.info(user.toString());
        user.setRoles(roles);
        userRepository.save(user);
        Mentor mentor = modelMapper.map(signupUpdateMentorRequest, Mentor.class);
        mentor.setUser(user);
        log.info(mentor.toString());
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
        User user = modelMapper.map(signupUpdateMenteeRequest, User.class);
        user.setPassword(encoder.encode(signupUpdateMenteeRequest.getPassword()));
        user.setRegistrationDate(Date.from(Instant.now()));
        user.setStatus(true);
        Role role = roleRepository.findByName(ERole.ROLE_MENTEE).orElseThrow(() ->
                new AccountNotFound("Role is not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        Mentee mentee = modelMapper.map(signupUpdateMenteeRequest, Mentee.class);
        mentee.setUser(user);
        menteeRepository.save(mentee);
        log.info("mentee was registered <<<");
        return new MessageResponse("User registered successfully!");
    }


    @Override
    @Async
    public ResponseEntity<?> forgotPassword(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("User not Found!!!!"));

        user.setResetToken(UUID.randomUUID().toString());
        userRepository.save(user);

        String url = request.getScheme() + "://" + request.getServerName() + "/reset?token=" + user.getResetToken();

        mailService.sendPasswordResetMail(email, url);
        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPassRequest request) {
        User user = userRepository.findByResetToken(request.getResetToken())
                .orElseThrow(() -> new AccountNotFound("Invalid reset Token"));

        user.setPassword(encoder.encode(request.getPassword()));
        user.setResetToken(null);
        userRepository.save(user);
        return new ResponseEntity<>("You have successfully reset your password." , HttpStatus.OK);
    }

}