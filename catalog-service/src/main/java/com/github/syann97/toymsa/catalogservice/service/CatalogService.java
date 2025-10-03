package com.github.syann97.toymsa.catalogservice.service;

import com.github.syann97.toymsa.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
	Iterable<CatalogEntity> getCatalogs();
}
