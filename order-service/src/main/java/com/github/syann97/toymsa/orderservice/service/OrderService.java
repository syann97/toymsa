package com.github.syann97.toymsa.orderservice.service;

import com.github.syann97.toymsa.orderservice.jpa.OrderEntity;
import com.github.syann97.toymsa.orderservice.vo.OrderVo;

public interface OrderService {
	OrderVo createOrder(OrderVo orderDetails);
	OrderVo getOrderByOrderId(String orderId);
	Iterable<OrderEntity> getOrdersByUserId(String userId);
}
