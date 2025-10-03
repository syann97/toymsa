package com.github.syann97.toymsa.catalogservice.service;

import org.springframework.stereotype.Service;

import com.github.syann97.toymsa.catalogservice.jpa.CatalogEntity;
import com.github.syann97.toymsa.catalogservice.jpa.CatalogRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

	private final CatalogRepository catalogRepository;

	public CatalogServiceImpl(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	@Override
	public Iterable<CatalogEntity> getCatalogs() {
		return catalogRepository.findAll();
	}
}
