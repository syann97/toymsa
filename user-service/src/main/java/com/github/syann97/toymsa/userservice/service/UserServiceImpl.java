package com.github.syann97.toymsa.userservice.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.github.syann97.toymsa.userservice.jpa.UserEntity;
import com.github.syann97.toymsa.userservice.jpa.UserRepository;
import com.github.syann97.toymsa.userservice.vo.UserVo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;

	@Override
	public UserVo createUser(UserVo userVo) {
		userVo.setUserId(UUID.randomUUID().toString());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userVo, UserEntity.class);
		userEntity.setEncryptedPassword("encryptedPassword");

		userRepository.save(userEntity);

		return mapper.map(userEntity, UserVo.class);
	}
}
