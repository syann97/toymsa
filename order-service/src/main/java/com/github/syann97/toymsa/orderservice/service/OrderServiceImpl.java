package com.github.syann97.toymsa.orderservice.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.github.syann97.toymsa.orderservice.jpa.OrderEntity;
import com.github.syann97.toymsa.orderservice.jpa.OrdersRepository;
import com.github.syann97.toymsa.orderservice.vo.OrderVo;

import lombok.Data;

@Data
@Service
public class OrderServiceImpl implements OrderService {

	private final OrdersRepository ordersRepository;

	public OrderServiceImpl(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@Override
	public OrderVo createOrder(OrderVo orderDetails) {
		orderDetails.setOrderId(UUID.randomUUID().toString());
		orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		OrderEntity orderEntity = modelMapper.map(orderDetails, OrderEntity.class);

		ordersRepository.save(orderEntity);

		return modelMapper.map(orderEntity, OrderVo.class);
	}

	@Override
	public OrderVo getOrderByOrderId(String orderId) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(orderId);
		return new ModelMapper().map(orderEntity, OrderVo.class);
	}

	@Override
	public Iterable<OrderEntity> getOrdersByUserId(String userId) {
		return ordersRepository.findByUserId(userId);
	}
}
