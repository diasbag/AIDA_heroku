package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByIgnoreIsFalse();
    List<Report> findAllByIgnoreIsTrue();
    Optional<Report> findReportByReporterIDAndHarasserID(Long reporterID, Long harasserID);
}
