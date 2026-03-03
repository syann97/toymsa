package com.github.syann97.toymsa.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.syann97.toymsa.userservice.client.OrderServiceClient;
import com.github.syann97.toymsa.userservice.dto.ResponseOrder;
import com.github.syann97.toymsa.userservice.jpa.UserEntity;
import com.github.syann97.toymsa.userservice.jpa.UserRepository;
import com.github.syann97.toymsa.userservice.vo.UserVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	private final Environment environment;
	private final RestTemplate restTemplate;
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	OrderServiceClient orderServiceClient;

	public UserServiceImpl(PasswordEncoder passwordEncoder,
				UserRepository userRepository,
				Environment environment,
				RestTemplate restTemplate,
				OrderServiceClient orderServiceClient) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.environment = environment;
		this.restTemplate = restTemplate;
		this.orderServiceClient = orderServiceClient;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if (userEntity == null)
			throw new UsernameNotFoundException(username + ": not found");

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
			true, true, true, true,
			new ArrayList<>());
	}

	@Override
	public UserVo createUser(UserVo userVo) {
		userVo.setUserId(UUID.randomUUID().toString());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userVo, UserEntity.class);
		userEntity.setEncryptedPassword(passwordEncoder.encode(userVo.getPassword()));

		userRepository.save(userEntity);

		return mapper.map(userEntity, UserVo.class);
	}

	@Override
	public UserVo getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new UsernameNotFoundException("User not found with userId: " + userId);
		}

		UserVo userVo = new ModelMapper().map(userEntity, UserVo.class);

		/* Using a restTemplate */
		// String orderUrl = String.format(environment.getProperty("order_service.url"), userId);
		// ResponseEntity<List<ResponseOrder>> orderListResponse =
		// 	restTemplate.exchange(orderUrl, HttpMethod.GET, null,
		// 		new ParameterizedTypeReference<List<ResponseOrder>>() {
		// 		});

		// List<ResponseOrder> orderList = orderListResponse.getBody();

		/* Using a feignClient */
		List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
		userVo.setOrders(orderList);

		return userVo;
	}

	@Override
	public Iterable<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserVo getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		return new ModelMapper().map(userEntity, UserVo.class);
	}
}
