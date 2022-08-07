package com.hackathon.mentor;

import com.hackathon.mentor.service.AdminService;
import com.hackathon.mentor.service.UserService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableSwagger2
@SpringBootApplication
@SecurityScheme(name = "basicauth", scheme = "basic",
		type = SecuritySchemeType.HTTP, in =
		SecuritySchemeIn.HEADER)
//@EnableSwagger2
@RequiredArgsConstructor
public class HackathonApp implements CommandLineRunner {
	private final AdminService adminService;
	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(HackathonApp.class, args);
	}
	@Override
	public void run(String... arg0) {
		userService.initRoles();
		adminService.createAdmin();
//		userService.setPasswords();
	}



}
