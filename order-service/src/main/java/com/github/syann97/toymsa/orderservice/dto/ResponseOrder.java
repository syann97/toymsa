package com.github.syann97.toymsa.orderservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
	private String productId;
	private String qty;
	private String unitPrice;
	private String totalPrice;
	private Date createdAt;

	private String orderId;
}
