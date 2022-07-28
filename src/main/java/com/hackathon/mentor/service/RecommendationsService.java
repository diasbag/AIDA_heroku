package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Mentor;

import java.util.List;
import java.util.Set;

public interface RecommendationsService {
    List<Mentor> getRecommendations(Long mentorID);

}
