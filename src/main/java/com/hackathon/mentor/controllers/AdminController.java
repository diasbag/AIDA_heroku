package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupAdminRequest;
import com.hackathon.mentor.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<String> createNewAdmin(@RequestBody @Valid SignupAdminRequest signupAdminRequest) {
        adminService.createNewAdmin(signupAdminRequest.getFirstname(), signupAdminRequest.getLastname(),
                signupAdminRequest.getEmail(), signupAdminRequest.getPassword());
        return ResponseEntity.ok("Admin was created");
    }

    @GetMapping("/all")
    public List<User> getAllAdmins() {
        return adminService.findAllAdmins();
    }

    @PostMapping("/deactivate_account_by_email")
    public ResponseEntity<String> deactivateAccountByEmail(@RequestParam String email){
        adminService.deactivateAccount(email);
        return ResponseEntity.ok("Account "+ email + "is deactivated");
    }
}
