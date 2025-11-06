package com.github.syann97.toymsa.orderservice.jpa;

import java.io.Serializable;
import java.util.Date;

// 기존에 사용했던 org.hibernate.annotations.ColumnDefault 대신
import org.hibernate.annotations.CreationTimestamp; // 자동 생성 시간 사용

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType; // IDENTITY 전략을 위해 필요
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

	// 1. @GeneratedValue에 IDENTITY 전략 명시 (MySQL 필수)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String productId;

	// qty를 int로 선언해도 무방하지만, Integer로 통일하는 것이 DTO와의 매핑에 유리할 수 있습니다.
	@Column(nullable = false)
	private Integer qty;

	@Column(nullable = false)
	private Integer unitPrice;

	@Column(nullable = false)
	private Integer totalPrice;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false, unique = true)
	private String orderId;

	/**
	 * MySQL/MariaDB DDL 오류 방지 및 시간 자동 삽입을 위한 표준 패턴
	 * 1. @CreationTimestamp: Hibernate에게 객체 생성 시 시간을 자동 삽입하도록 지시합니다. (insertable = false 역할 대체)
	 * 2. columnDefinition: "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"를 명시하여
	 * Hibernate가 'datetime(6)' 대신 MySQL 친화적인 DDL을 생성하도록 강제합니다.
	 */
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false,
		columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt; // java.time.LocalDateTime 사용을 권장합니다.
}
