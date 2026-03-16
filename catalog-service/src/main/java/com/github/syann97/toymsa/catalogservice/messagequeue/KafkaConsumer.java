package com.github.syann97.toymsa.catalogservice.messagequeue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
	@KafkaListener(topics = "example-catalog-topic")
	public void updateQty(String kafkaMessage) {
		log.info("Kafka Message : -> " + kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		CatalogEntity entity = catalogRepository.findByProductId((String)map.get("productId"));

		if (entity != null) {
			entity.setStock(entity.getStock() - (Integer)map.get("qty"));
			catalogRepository.save(entity);
		}
	}
}
