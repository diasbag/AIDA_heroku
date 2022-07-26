package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Rating;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.payload.request.UpdateMenteeRequest;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RatingRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.MenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenteeServiceImpl implements MenteeService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;

    @Override
    public ResponseEntity<?> getAllMentees() {
        List<Mentee> mentees = menteeRepository.getAll();
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMenteeById(Long id) {
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        if (mentee == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> editProfile(String email, UpdateMenteeRequest updateMenteeRequest) {
        User user = userRepository.getByEmail(email);
        Mentee mentee = menteeRepository.findByUser(user);

        user.setFirstname(updateMenteeRequest.getFirstname());
        user.setLastname(updateMenteeRequest.getLastname());
        user.setEmail(updateMenteeRequest.getEmail());

        userRepository.save(user);

        mentee.setSchool(updateMenteeRequest.getSchool());
        mentee.setAchievements(updateMenteeRequest.getAchievements());
        mentee.setGrade(updateMenteeRequest.getGrade());
        mentee.setIin(updateMenteeRequest.getIin());
        mentee.setNumber(updateMenteeRequest.getNumber());
        menteeRepository.save(mentee);
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> rateMentor(Long id, String email, RatingRequest ratingRequest) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentee mentee = menteeRepository.findByUser(user);

        Mentor mentor = mentorRepository.findById(id).orElse(null);
        Set<Mentee> mentees = mentor.getMentees();
        if (!mentees.contains(mentee)) {
            return new ResponseEntity<>("Not your mentor!!!!!!! 4ert", HttpStatus.CONFLICT);
        }
        Rating rating = mentor.getRating();
        if (rating == null) {
            Rating rating1 = new Rating();
            rating1.setRating(ratingRequest.getRate());
            rating1.setPeopleCount(1);
            ratingRepository.save(rating1);
            mentor.setRating(rating1);
            mentorRepository.save(mentor);
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        long cnt =  (rating.getPeopleCount()+1);
        double res = (rating.getRating()+ ratingRequest.getRate())/(cnt);
        rating.setRating(res);
        rating.setPeopleCount(cnt);
        ratingRepository.save(rating);
        mentor.setRating(rating);
        mentorRepository.save(mentor);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

}
