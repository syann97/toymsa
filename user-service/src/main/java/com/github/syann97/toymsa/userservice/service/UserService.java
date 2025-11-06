package com.github.syann97.toymsa.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.github.syann97.toymsa.userservice.jpa.UserEntity;
import com.github.syann97.toymsa.userservice.vo.UserVo;

public interface UserService extends UserDetailsService {
	UserVo createUser(UserVo userVo);

	UserVo getUserByUserId(String userId);

	Iterable<UserEntity> getAllUsers();

	UserVo getUserDetailsByEmail(String email);
}
