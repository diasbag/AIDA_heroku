package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountConflict;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private final RatingNotificationRepository ratingNotificationRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Transactional
    @Override
    public ResponseEntity<?> rateUser(Long id, String email, RatingRequest ratingRequest) {
        log.info("rating of user with id - " + id);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        ERole role = user.getRoles().get(0).getName();
        Set<Mentee> mentees;
        Mentor mentor;
        Mentee mentee;
        User userForRating;
        RatingNotification ratingNotification;
        if (role.equals(ERole.ROLE_MENTOR)) {
            mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user with email " + email));
            mentee = menteeRepository.findById(id).orElseThrow(() ->
                    new AccountNotFound("mentee with id " + id));
            userForRating = mentee.getUser();
            List<RatingNotification> ratingNotificationList =
                    ratingNotificationRepository.findRatingNotificationByMentorAndMentee(mentor, mentee).orElseThrow(
                            () -> new AccountNotFound("rating notification with mentor - " + mentor +
                                    " and mentee - " + mentee));
            ratingNotification = ratingNotificationList.get(ratingNotificationList.size() - 1);
            if (!ratingNotification.getMentorRated()) {
                ratingNotification.setMentorRated(true);
                ratingNotificationRepository.save(ratingNotification);
            }
        } else if (role.equals(ERole.ROLE_MENTEE)){
            mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user - " + user));
            mentor = mentorRepository.findById(id).orElseThrow(() ->
                    new AccountNotFound("mentor with id " + id));
            userForRating = mentor.getUser();
            List<RatingNotification> ratingNotificationList =
                    ratingNotificationRepository.findRatingNotificationByMentorAndMentee(mentor, mentee).orElseThrow(
                            () -> new AccountNotFound("rating notification with mentor - " + mentor +
                                    " and mentee - " + mentee)
                    );
            ratingNotification = ratingNotificationList.get(ratingNotificationList.size() - 1);
            if (!ratingNotification.getMenteeRated()) {
                ratingNotification.setMenteeRated(true);
                ratingNotificationRepository.save(ratingNotification);
            }
        } else {
            return new ResponseEntity<>("Wrong role", HttpStatus.CONFLICT);
        }
        mentees = mentor.getMentees();
        if (!mentees.contains(mentee)) {
            return new ResponseEntity<>("Not your mentee!", HttpStatus.CONFLICT);
        }
        Rating rating = userForRating.getRating();
        double overallRating = (ratingRequest.getSubjectKnowledge() + ratingRequest.getCommunicativeActivity() +
                ratingRequest.getDataQuality())/3.0;
        Comment comment = modelMapper.map(ratingRequest, Comment.class);
        comment.setAverageScore(overallRating);
        comment.setUser(mentor.getUser());
        if (rating == null) {
            Rating rating1 = new Rating();
            rating1.getComments().add(comment);
            rating1.setKnowledgeRating(ratingRequest.getSubjectKnowledge());
            rating1.setCommunicationRating(ratingRequest.getCommunicativeActivity());
            rating1.setQualityOfServiceRating(ratingRequest.getDataQuality());
            rating1.setRating(overallRating);
            rating1.setPeopleCount(1);
            commentRepository.save(comment);
            ratingRepository.save(rating1);
            userForRating.setRating(rating1);
        } else {
            long cnt =  (rating.getPeopleCount()+1);
            double res = ((rating.getRating()*rating.getPeopleCount()) + overallRating)/(cnt);
            double knowledgeRating = ((rating.getKnowledgeRating()*rating.getPeopleCount()) +
                    ratingRequest.getSubjectKnowledge())/(cnt);
            double communicationRating = ((rating.getCommunicationRating() * rating.getPeopleCount()) +
                    ratingRequest.getCommunicativeActivity())/(cnt);
            double qualityOfService = ((rating.getQualityOfServiceRating() * rating.getPeopleCount()) +
                    ratingRequest.getDataQuality())/(cnt);
            commentRepository.save(comment);
            rating.getComments().add(comment);
            rating.setRating(res);
            rating.setCommunicationRating(communicationRating);
            rating.setKnowledgeRating(knowledgeRating);
            rating.setQualityOfServiceRating(qualityOfService);
            rating.setPeopleCount(cnt);
            ratingRepository.save(rating);
            userForRating.setRating(rating);
        }
        userRepository.save(userForRating);
        log.info("user was rated - " + user + " <<<");
        Long returnID;
        if (ratingNotification.getMentorRated()) {
            returnID = ratingNotification.getMentee().getId();
        } else {
            returnID = ratingNotification.getMentor().getId();
        }
        return new ResponseEntity<>(returnID, HttpStatus.OK);
    }

    @Override
    public List<Long> whoToRate() throws AccountConflict {
        log.info("retrieving list of users who user needs to rate ...");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        ERole role = user.getRoles().get(0).getName();
        List<Long> toRate = new ArrayList<>();
        if (role.equals(ERole.ROLE_MENTOR)) {
            Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user with email " + email));
            List<RatingNotification> ratingNotification = ratingNotificationRepository
                    .findRatingNotificationByMentor(mentor);
            for (RatingNotification rating: ratingNotification) {
                if (!rating.getMentorRated()) {
                    toRate.add(rating.getMentee().getId());
                }
            }
        } else if (role.equals(ERole.ROLE_MENTEE)) {
            Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user - " + user));
            List<RatingNotification> ratingNotification = ratingNotificationRepository
                    .findRatingNotificationByMentee(mentee);
            for (RatingNotification rating : ratingNotification) {
                if (!rating.getMenteeRated()) {
                    toRate.add(rating.getMentor().getId());
                }
            }
        } else {
            throw new AccountConflict("Wrong role!");
        }
        log.info("rate list was retrieved - " + toRate);
        return toRate;
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




