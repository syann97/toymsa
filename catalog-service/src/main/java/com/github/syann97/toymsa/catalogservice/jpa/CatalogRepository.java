package com.github.syann97.toymsa.catalogservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
	CatalogEntity findByProductId(String productId);
}
