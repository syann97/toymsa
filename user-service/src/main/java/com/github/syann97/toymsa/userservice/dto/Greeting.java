package com.github.syann97.toymsa.userservice.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Greeting {
	@Value("${greeting.message}")
	private String message;
}
