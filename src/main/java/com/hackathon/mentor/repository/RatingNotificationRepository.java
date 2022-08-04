package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.RatingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingNotificationRepository extends JpaRepository<RatingNotification, Long> {
    Optional<List<RatingNotification>> findRatingNotificationByMentorAndMentee(Mentor mentor, Mentee mentee);

    List<RatingNotification> findRatingNotificationByMentee(Mentee mentee);

    List<RatingNotification> findRatingNotificationByMentor(Mentor mentor);

}
