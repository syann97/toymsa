package com.github.syann97.toymsa.catalogservice.messagequeue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.syann97.toymsa.catalogservice.jpa.CatalogEntity;
import com.github.syann97.toymsa.catalogservice.jpa.CatalogRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {
	CatalogRepository catalogRepository;

	@Autowired
	public KafkaConsumer(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	// 카프카에 해당 토픽명으로 데이터가 들어오면 해당 메서드를 실행
	@KafkaListener(topics = "orders-cdc.msa_order_service.orders")
	public void updateQty(String kafkaMessage) {
		log.info("CDC Kafka Message : -> " + kafkaMessage);

		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(kafkaMessage);

			JsonNode after = rootNode.path("after");

			if (after.isMissingNode() || after.isNull()) {
				return;
			}

			String productId = after.path("product_id").asText();
			int qty = after.path("qty").asInt();
			log.info("qty : " + qty);

			CatalogEntity entity = catalogRepository.findByProductId(productId);

			if (entity != null) {
				entity.setStock(entity.getStock() - qty);
				catalogRepository.save(entity);
			}
			else {
				log.info("entity is Null !!");
			}
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
