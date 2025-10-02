package com.github.syann97.toymsa.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.syann97.toymsa.userservice.dto.ResponseOrder;
import com.github.syann97.toymsa.userservice.jpa.UserEntity;
import com.github.syann97.toymsa.userservice.jpa.UserRepository;
import com.github.syann97.toymsa.userservice.vo.UserVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	UserRepository userRepository;

	PasswordEncoder passwordEncoder;

	public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
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

		List<ResponseOrder> orderList = new ArrayList<>();
		userVo.setOrders(orderList);

		return userVo;
	}

	@Override
	public Iterable<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}
}
