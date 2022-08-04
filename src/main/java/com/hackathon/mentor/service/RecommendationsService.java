package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.response.MentorsResponse;

import java.util.List;

public interface RecommendationsService {
    List<MentorsResponse> getRecommendations(Long mentorID);

}
