package com.github.syann97.toymsa.userservice.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
