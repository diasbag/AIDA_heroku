package com.hackathon.mentor.repository;

import java.util.Optional;

import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
