package com.hackathon.mentor.repository;

import java.util.List;
import java.util.Optional;

import com.hackathon.mentor.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.mentor.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  User getByEmail(String email);
  Boolean existsByEmail(String email);
}
