package com.github.syann97.toymsa.userservice.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.syann97.toymsa.userservice.dto.Greeting;
import com.github.syann97.toymsa.userservice.dto.RequestUser;
import com.github.syann97.toymsa.userservice.dto.ResponseUser;
import com.github.syann97.toymsa.userservice.service.UserService;
import com.github.syann97.toymsa.userservice.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user-service")
@Slf4j
public class UserController {
	private final Environment environment;
	private final Greeting greeting;
	private final UserService userService;

	@Autowired
	public UserController (Environment environment, Greeting greeting, UserService userService) {
		this.environment = environment;
		this.greeting = greeting;
		this.userService = userService;
	}

	@GetMapping("/health-check") // http://localhost:60000/health-check
	public String status() {
		return String.format("It's Working in User Service"
			+ ", port(local.server.port)=" + environment.getProperty("local.server.port")
			+ ", port(server.port)=" + environment.getProperty("server.port"));
	}

	@GetMapping("/welcome")
	public String welcome(HttpServletRequest request) {
		log.info("users.welcome ip: {}, {}, {}, {}", request.getRemoteAddr()
			, request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());

		//        return env.getProperty("greeting.message");
		return greeting.getMessage();
	}

	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserVo userVo = mapper.map(user, UserVo.class);
		userService.createUser(userVo);

		ResponseUser responseUser = mapper.map(userVo, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
}
