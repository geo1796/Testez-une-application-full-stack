package com.openclassrooms.starterjwt.payload.request;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class LoginRequest {
	@NotBlank
  private String email;

	@NotBlank
	private String password;

	public String getEmail() {
		return email;
	}


	public String getPassword() {
		return password;
	}
}
