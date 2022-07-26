package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.SubscribeRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;

    private final UserRepository userRepository;

    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;

    @Override
    public ResponseEntity<?> getSubscribers() {
        List<Subscribe> subscribeList = subscribeRepository.findAll();
        return new ResponseEntity<>(subscribeList , HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> subscribe(Long id, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentee mentee = menteeRepository.findByUser(user);
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if (subscribeRepository.findByMentorAndMentee(mentor, mentee).orElse(null) != null) {
            return new ResponseEntity<>("You are already subscribed!!!!", HttpStatus.CONFLICT);
        }
        Subscribe subscribe = new Subscribe();
        Set<Mentee> mentees = new HashSet<>();
        mentees.add(mentee);
        subscribe.setMentee(mentees);
        subscribe.setMentor(mentor);
        subscribeRepository.save(subscribe);

        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }
}
