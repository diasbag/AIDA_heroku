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
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        if (role.equals(ERole.ROLE_MENTOR)) {
            Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user with email " + email));
            Mentee mentee = menteeRepository.findById(id).orElseThrow(() ->
                    new AccountNotFound("mentee with id " + id));

            mentees = mentor.getMentees();
            if (!mentees.contains(mentee)) {
                return new ResponseEntity<>("Not your mentee!!!!!!! 4ert", HttpStatus.CONFLICT);
            }
            Comment comment = new Comment();
            Rating rating = user.getRating();
            if (rating == null) {

                comment.setComment(ratingRequest.getComment());
                comment.setUser(mentor.getUser());
                Rating rating1 = new Rating();
                rating1.getComments().add(comment);

                rating1.setRating(ratingRequest.getRate());
                rating1.setPeopleCount(1);
                commentRepository.save(comment);

                ratingRepository.save(rating1);
                user.setRating(rating1);
                userRepository.save(user);
                return new ResponseEntity<>("success", HttpStatus.OK);
            }
            long cnt =  (rating.getPeopleCount()+1);
            double res = ((rating.getRating()* rating.getPeopleCount()) + ratingRequest.getRate())/(cnt);
            comment.setComment(ratingRequest.getComment());
            comment.setUser(mentor.getUser());
            commentRepository.save(comment);
            rating.getComments().add(comment);
            rating.setRating(res);
            rating.setPeopleCount(cnt);
            ratingRepository.save(rating);
            user.setRating(rating);
            userRepository.save(user);
            log.info("Mentor was rated!!!");
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        if (role.equals(ERole.ROLE_MENTEE)) {
            Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user - " + user));
            Mentor mentor = mentorRepository.findById(id).orElseThrow(() ->
                    new AccountNotFound("mentor with id " + id));
            mentees = mentor.getMentees();
            if (!mentees.contains(mentee)) {
                return new ResponseEntity<>("Not your mentor!!!!!!! 4ert", HttpStatus.CONFLICT);
            }
            Comment comment = new Comment();
            Rating rating = user.getRating();
            if (rating == null) {

                comment.setComment(ratingRequest.getComment());
                comment.setUser(mentee.getUser());
                Rating rating1 = new Rating();
                rating1.getComments().add(comment);

                rating1.setRating(ratingRequest.getRate());
                rating1.setPeopleCount(1);
                commentRepository.save(comment);

                ratingRepository.save(rating1);
                user.setRating(rating1);
                userRepository.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            long cnt =  (rating.getPeopleCount()+1);
            double res = ((rating.getRating()* rating.getPeopleCount()) + ratingRequest.getRate())/(cnt);
            comment.setComment(ratingRequest.getComment());
            comment.setUser(mentee.getUser());
            commentRepository.save(comment);
            rating.getComments().add(comment);
            rating.setRating(res);
            rating.setPeopleCount(cnt);
            ratingRepository.save(rating);
            user.setRating(rating);
            userRepository.save(user);
            log.info("Mentor was rated!!!");
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.CONFLICT);
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
