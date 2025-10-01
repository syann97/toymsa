package com.github.syann97.toymsa.userservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("password"));
	}
}
