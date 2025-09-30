package com.github.syann97.toymsa.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestUser {
	@Size(min = 2, message = "Email not be less than two characters")
	@Email
	private String email;

	@Size(min = 8, message = "Password must be equal or grater than 8 character")
	private String password;

	@Size(min = 2, message = "Name not be less than two characters")
	private String name;
}
