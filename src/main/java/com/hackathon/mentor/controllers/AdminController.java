package com.hackathon.mentor.controllers;

//import com.hackathon.mentor.models.MentorHistory;
import com.hackathon.mentor.models.RatingNotification;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupAdminRequest;
import com.hackathon.mentor.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "basicauth")
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createNewAdmin(@RequestBody @Valid SignupAdminRequest signupAdminRequest) {
        adminService.createNewAdmin(signupAdminRequest.getFirstname(), signupAdminRequest.getLastname(),
                signupAdminRequest.getEmail(), signupAdminRequest.getPassword());
        return ResponseEntity.ok("Admin was created");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllAdmins() {
        return adminService.findAllAdmins();
    }

    @PostMapping("/deactivate_account_by_email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateAccountByEmail(@RequestParam String email){
        adminService.deactivateAccount(email);
        return ResponseEntity.ok("Account "+ email + "is deactivated");
    }

    @GetMapping("/mentors/history")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RatingNotification> getMentorsHistory() {
        return adminService.getMentorsHistory();
    }


}
