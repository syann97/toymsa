package com.github.syann97.toymsa.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.syann97.toymsa.catalogservice.dto.ResponseCatalog;
import com.github.syann97.toymsa.catalogservice.jpa.CatalogEntity;
import com.github.syann97.toymsa.catalogservice.service.CatalogService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
	private final Environment env;
	private final CatalogService catalogService;

	@Autowired
	public CatalogController(Environment env, CatalogService catalogService) {
		this.env = env; // env 초기화
		this.catalogService = catalogService;
	}

	@GetMapping("/health-check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in Catalog Service on Port %s", request.getServerPort());
	}

	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
		Iterable<CatalogEntity> catalogEntities = catalogService.getCatalogs();

		List<ResponseCatalog> catalogs = new ArrayList<>();
		for (CatalogEntity catalogEntity : catalogEntities) {
			catalogs.add(new ModelMapper().map(catalogEntity, ResponseCatalog.class));
		}

		return ResponseEntity.status(HttpStatus.OK).body(catalogs);
	}
}
