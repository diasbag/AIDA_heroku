package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByFileName(String name);

    Image findByUuid(String uuid);


}
