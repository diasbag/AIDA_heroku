package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.service.RecommendationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationsServiceImpl implements RecommendationsService {

    private final MentorRepository mentorRepository;
    @Override
    public List<Mentor> getRecommendations(Long mentorID) {
        log.info("recommendations by subject ...");
        Mentor mentor = mentorRepository.findById(mentorID).orElseThrow(() ->
                new AccountNotFound("mentor with id " + mentorID));
        Set<Mentor> majorsSet = new HashSet<>();
        if (mentor.getBachelorsMajor() != null) {
            if(mentor.getMastersMajor() != null) {
                Set<Mentor> majorsList1 = mentorRepository.findByBachelorsMajorOrMastersMajor(
                        mentor.getBachelorsMajor(), mentor.getBachelorsMajor());
                Set<Mentor> majorsList2 = mentorRepository.findByBachelorsMajorOrMastersMajor(
                        mentor.getMastersMajor(), mentor.getMastersMajor());
                majorsSet.addAll(majorsList1);
                majorsSet.addAll(majorsList2);
            } else {
                majorsSet.addAll(mentorRepository.findByBachelorsMajor(mentor.getBachelorsMajor()));
            }
        }
        Set<Mentor> majorsUniversity = new HashSet<>();
        if (mentor.getBachelorsUniversity() != null) {
            if(mentor.getMastersUniversity() != null) {
                Set<Mentor> universitySet1 = mentorRepository.findByBachelorsUniversityOrMastersUniversity(
                        mentor.getBachelorsUniversity(), mentor.getBachelorsUniversity());
                Set<Mentor> universitySet2 = mentorRepository.findByBachelorsUniversityOrMastersUniversity(
                        mentor.getMastersUniversity(), mentor.getMastersUniversity());
                majorsUniversity.addAll(universitySet1);
                majorsUniversity.addAll(universitySet2);
            } else {
                majorsUniversity.addAll(mentorRepository.findByBachelorsUniversity(mentor.getBachelorsUniversity()));
            }
        }
        List<Mentor> mentorList1 = mentorRepository.findBySubjectOfInterest1(mentor.getSubjectOfInterest1());
        List<Mentor> mentorList2 = mentorRepository.findBySubjectOfInterest1(mentor.getSubjectOfInterest2());
        List<Mentor> mentorList3 = mentorRepository.findBySubjectOfInterest2(mentor.getSubjectOfInterest1());
        List<Mentor> mentorList4 = mentorRepository.findBySubjectOfInterest2(mentor.getSubjectOfInterest2());
        Set<Mentor> result1 = mentorList1.stream()
                .distinct()
                .filter(mentorList3::contains)
                .collect(Collectors.toSet());
        Set<Mentor> result2 = mentorList2.stream()
                .distinct()
                .filter(mentorList4::contains)
                .collect(Collectors.toSet());
        Set<Mentor> subjectsSet = new HashSet<>();
        subjectsSet.addAll(result1);
        subjectsSet.addAll(result2);

        Set<Mentor> allSet = new HashSet<>();
        allSet.addAll(majorsSet);
        allSet.addAll(majorsUniversity);
        allSet.addAll(subjectsSet);
        List<Mentor> out = new ArrayList<>(allSet);
        subjectsSet.removeAll(allSet);
        majorsSet.removeAll(allSet);
        majorsUniversity.removeAll(allSet);
        if (subjectsSet.size() > 3 && majorsSet.size() > 3 && majorsUniversity.size() > 3) {
            out.add(subjectsSet.iterator().next());
            out.add(subjectsSet.iterator().next());
            out.add(majorsSet.iterator().next());
            out.add(majorsSet.iterator().next());
            for (int i = 0; i < 6; i++){
                out.add(majorsUniversity.iterator().next());
            }
        } else if (subjectsSet.size() > 9){
            for (int i = 0; i < 10; i++){
                out.add(subjectsSet.iterator().next());
            }
        } else if (majorsSet.size() > 9){
            for (int i = 0; i < 10; i++){
                out.add(majorsSet.iterator().next());
            }
        } else if (majorsUniversity.size() > 9){
            for (int i = 0; i < 10; i++){
                out.add(majorsUniversity.iterator().next());
            }
        } else {
            out.addAll(subjectsSet);
            out.addAll(majorsSet);
            out.addAll(majorsUniversity);
        }
        if (out.size() > 9) {
            out.subList(0, 9);
        } else {
            List <Mentor> filling = mentorRepository.getAll();
            filling.removeAll(out);
            out.addAll(filling);
            if (out.size() > 9) {
                out.subList(0, 9);
            }
        }
        log.info("recommendations by subject is done <<<");
        return out;
    }
}
