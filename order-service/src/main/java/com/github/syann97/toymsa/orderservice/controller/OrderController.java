package com.github.syann97.toymsa.orderservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.syann97.toymsa.orderservice.dto.RequestOrder;
import com.github.syann97.toymsa.orderservice.dto.ResponseOrder;
import com.github.syann97.toymsa.orderservice.jpa.OrderEntity;
import com.github.syann97.toymsa.orderservice.messagequeue.KafkaProducer;
import com.github.syann97.toymsa.orderservice.service.OrderService;
import com.github.syann97.toymsa.orderservice.vo.OrderVo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/order-service")
public class OrderController {

	private final OrderService orderService;
	KafkaProducer kafkaProducer;

	public OrderController(OrderService orderService, KafkaProducer kafkaProducer) {
		this.orderService = orderService;
		this.kafkaProducer = kafkaProducer;
	}

	@GetMapping("/health-check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in Catalog Service on Port %d", (Integer) request.getServerPort());
	}

	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable String userId, @RequestBody RequestOrder orderDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		/* jpa */
		OrderVo orderVo = modelMapper.map(orderDetails, OrderVo.class);
		orderVo.setUserId(userId);
		OrderVo createVo = orderService.createOrder(orderVo);

		ResponseOrder responseOrder = modelMapper.map(createVo, ResponseOrder.class);

		/* send this order to the kafka */
		kafkaProducer.send("example-catalog-topic", orderVo);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) throws Exception {
		log.info("Before retrieve orders data");
		Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

		List<ResponseOrder> result = new ArrayList<>();
		for (OrderEntity order : orderList) {
			result.add(new ModelMapper().map(order, ResponseOrder.class));
		}

		log.info("After retrieved orders data");
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
