package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountBadRequest;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Role;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private  final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    @Override
    public void createAdmin() {
        if (!userRepository.findByEmail("admin@gmail.com").isPresent()) {
            User user = new User("Admin", "Admin", "admin@gmail.com",
                    encoder.encode("admin"));
            Role role = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() ->
                    new AccountNotFound("Error: Role is not found."));
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
    @Override
    public void createNewAdmin(String firstName, String lastName, String email, char[] password) {
        if (!userRepository.findByEmail(email).isPresent()) {
            User user = new User(firstName, lastName, email,
                    encoder.encode(java.nio.CharBuffer.wrap(password)));
            Role role = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() ->
                    new AccountNotFound("Error: Role is not found."));
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    @Override
    public List<User> findAllAdmins() {
        log.info("finding all admins ...");
         List<User> userList = userRepository.findAll();
         List<User> out = new ArrayList<>();
         for (User user: userList) {
             if (user.getRoles().get(0).getName().name().equals("ROLE_ADMIN")) {
                 out.add(user);
             }
         }
         log.info("all admins were retrieved <<<");
         return out;
    }

    @Override
    public void deactivateAccount(String email) {
        log.info("deactivating account ...");
        if (email.isEmpty()) {
            throw new AccountBadRequest("check email");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound(email));
        user.setStatus(false);
        userRepository.save(user);
        log.info("account was deactivated: " + email);
    }
}
