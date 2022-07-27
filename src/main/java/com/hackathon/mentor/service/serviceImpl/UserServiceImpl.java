package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Role;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    @Override
    public void initRoles() {
        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            Role roleAdmin = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }
        if (!roleRepository.findByName(ERole.ROLE_MENTOR).isPresent()) {
            Role roleMentor = new Role(ERole.ROLE_MENTOR);
            roleRepository.save(roleMentor);
        }
        if (!roleRepository.findByName(ERole.ROLE_MENTEE).isPresent()) {
            Role roleMentee = new Role(ERole.ROLE_MENTEE);
            roleRepository.save(roleMentee);
        }
    }
}
