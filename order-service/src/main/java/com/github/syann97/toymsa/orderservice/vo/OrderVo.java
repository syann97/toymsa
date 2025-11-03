package com.github.syann97.toymsa.orderservice.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderVo implements Serializable {
	private String productId;
	private Integer qty;
	private Integer unitPrice;
	private Integer totalPrice;

	private String orderId;
	private String userId;
}
