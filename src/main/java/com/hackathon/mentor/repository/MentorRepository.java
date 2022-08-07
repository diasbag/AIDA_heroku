package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.FilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long>, JpaSpecificationExecutor<Mentor> {

    Optional<Mentor> findById(Long id);

    @Query(value = "SELECT * FROM mentors", nativeQuery = true)
    List<Mentor> getAll();

    Optional<Mentor> findByUser(User user);

    Mentor getByUser(User user);
    Page<Mentor> findAll(Pageable pageable);
    List<Mentor> findBySubjectOfInterest1(String subjectOfInterest1);
    List<Mentor> findBySubjectOfInterest2(String subjectOfInterest2);

    List<Mentor> findByBachelorsUniversityContainingIgnoreCase(String university);

    Page<Mentor> findByBachelorsUniversityContainingIgnoreCaseAndCountryOfResidence(String university, String country, Pageable pageable);
    Page<Mentor> findByBachelorsUniversityContainingIgnoreCaseAndCountryOfResidenceAndBachelorsMajor(String university,
                                                                                                     String country,
                                                                                                     String major, Pageable pageable);
    Page<Mentor> findByBachelorsUniversityContainingIgnoreCaseAndBachelorsMajor(String university, String major, Pageable pageable);
    Page<Mentor> findByCountryOfResidenceAndBachelorsMajor(String country,
                                                           String major, Pageable pageable);
    List<Mentor> findByBachelorsUniversityContainingIgnoreCaseOrCountryOfResidenceOrBachelorsMajor(String university, String country, String major);
    Page<Mentor> findByCountryOfResidence(String country, Pageable pageable);

    Set<Mentor> findByBachelorsMajorOrMastersMajor(String bachelorsMajor, String MastersMajor);
    Set<Mentor> findByBachelorsUniversityOrMastersUniversity(String bachelorsUniversity, String mastersUniversity);
    Set<Mentor> findByBachelorsUniversity(String bachelorsUniversity);
    Set<Mentor> findByBachelorsMajor(String bachelorsMajor);

    Page<Mentor> getMentorByBachelorsUniversityContainingIgnoreCase(String university, Pageable pageable);

    Page<Mentor> getMentorByBachelorsMajor(String major, Pageable pageable);

}
