package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Role;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final MentorRepository mentorRepository;
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

    @Override
    public void initFakeAccounts() {
        if (userRepository.findByEmail("Alex0@mail.com").isPresent()) {
            return;
        }
        List<Role> roles = roleRepository.findAll();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            Mentor mentor = new Mentor();
            user.setFirstname("Alex" + i);
            user.setMiddlename("Alexovich" + i);
            user.setLastname("Alexov" + i);
            user.setEmail("Alex@mail.com" + i);
            user.getRoles().add(roles.get(1));
            user.setPassword("Alex@345");
            user.setTelegram("@Alex" + i);
            mentor.setUser(user);
            userRepository.save(user);
            mentorRepository.save(mentor);
        }
    }
}
