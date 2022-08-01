package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {

    private final UserRepository userRepository;

    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;

    private final RatingRepository ratingRepository;

    private final CommentRepository commentRepository;
    @Override
    public ResponseEntity<?> rateUser(Long id, String email, RatingRequest ratingRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        ERole role = user.getRoles().get(0).getName();
        Set<Mentee> mentees;
        Mentor mentor;
        Mentee mentee;
        if (role.equals(ERole.ROLE_MENTOR)) {
            mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user with email " + email));
            mentee = menteeRepository.findById(id).orElseThrow(() ->
                    new AccountNotFound("mentee with id " + id));
        } else if (role.equals(ERole.ROLE_MENTEE)){
            mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user - " + user));
            mentor = mentorRepository.findById(id).orElseThrow(() ->
                    new AccountNotFound("mentor with id " + id));
        } else {
            return new ResponseEntity<>("Wrong role", HttpStatus.CONFLICT);
        }
        mentees = mentor.getMentees();
        if (!mentees.contains(mentee)) {
            return new ResponseEntity<>("Not your mentee!", HttpStatus.CONFLICT);
        }
        Comment comment = new Comment();
        Rating rating = user.getRating();
        double overallRating = 0;
        comment.setComment(ratingRequest.getComment());
        comment.setUser(mentor.getUser());
        if (rating == null) {
            overallRating = (ratingRequest.getKnowledgeRating() + ratingRequest.getCommunicationRating() +
                    ratingRequest.getQualityOfServiceRating())/3.0;
            Rating rating1 = new Rating();
            rating1.getComments().add(comment);
            rating1.setKnowledgeRating(ratingRequest.getKnowledgeRating());
            rating1.setCommunicationRating(ratingRequest.getCommunicationRating());
            rating1.setQualityOfServiceRating(ratingRequest.getQualityOfServiceRating());
            rating1.setRating(overallRating);
            rating1.setPeopleCount(1);
            commentRepository.save(comment);
            ratingRepository.save(rating1);
            user.setRating(rating1);
        } else {
            long cnt =  (rating.getPeopleCount()+1);
            double res = ((rating.getRating()* rating.getPeopleCount()) + overallRating)/(cnt);
            commentRepository.save(comment);
            rating.getComments().add(comment);
            rating.setRating(res);
            rating.setPeopleCount(cnt);
            ratingRepository.save(rating);
            user.setRating(rating);
        }
        userRepository.save(user);
        log.info("Mentor was rated!!!");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    //    public User rate(User user, RatingRequest ratingRequest) {
//        Comment comment = new Comment();
//        Rating rating = user.getRating();
//        if (rating == null) {
//
//            comment.setComment(ratingRequest.getComment());
//            comment.setUser(user);
//            Rating rating1 = new Rating();
//            rating1.getComments().add(comment);
//
//            rating1.setRating(ratingRequest.getRate());
//            rating1.setPeopleCount(1);
//            commentRepository.save(comment);
//
//            ratingRepository.save(rating1);
//            user.setRating(rating1);
//            return user;
//        }
//        long cnt =  (rating.getPeopleCount()+1);
//        double res = ((rating.getRating()* rating.getPeopleCount()) + ratingRequest.getRate())/(cnt);
//        comment.setComment(ratingRequest.getComment());
//        comment.setUser(user);
//
//        commentRepository.save(comment);
//        rating.getComments().add(comment);
//        rating.setRating(res);
//        rating.setPeopleCount(cnt);
//        ratingRepository.save(rating);
//        user.setRating(rating);
//        log.info("Mentor was rated!!!");
//        return user;
//    }
}




