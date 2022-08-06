package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.MentorHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorHistoryRepository extends JpaRepository<MentorHistory, Long> {

    MentorHistory findByMentorAndMentee(Mentor mentor, Mentee mentee);
}
