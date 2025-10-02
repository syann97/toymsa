package com.github.syann97.toymsa.userservice.vo;

import java.util.Date;
import java.util.List;

import com.github.syann97.toymsa.userservice.dto.ResponseOrder;

import lombok.Data;

@Data
public class UserVo {
	private String email;
	private String name;
	private String password;
	private String userId;
	private Date createdAt;

	private String encryptedPwd;

	private List<ResponseOrder> orders;
}