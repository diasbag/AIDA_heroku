package com.hackathon.mentor;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@SpringBootApplication
@SecurityScheme(name = "basicauth", scheme = "basic",
		type = SecuritySchemeType.HTTP, in =
		SecuritySchemeIn.HEADER)
public class HackathonApp {

	public static void main(String[] args) {
		SpringApplication.run(HackathonApp.class, args);
	}



}
