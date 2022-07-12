package com.hackathon.mentor.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private List<String> roles;

	public JwtResponse(String accessToken, List<String> roles) {
		this.token = accessToken;
		this.roles = roles;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}


	public List<String> getRoles() {
		return roles;
	}
}
