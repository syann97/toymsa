package com.github.syann97.toymsa.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
	@NotNull(message = "Email cannot be null")
	@Size(min = 2, message = "Email not be less than two characters")
	@Email
	private String email;

	@NotNull(message = "Password cannot be null")
	@Size(min = 8, message = "Password must be equal or grater than 8 character")
	private String password;

	@NotNull(message = "Name cannot be null")
	@Size(min = 2, message = "Name not be less than two characters")
	private String name;
}
