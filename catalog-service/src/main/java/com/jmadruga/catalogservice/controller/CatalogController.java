package com.jmadruga.catalogservice.controller;

import com.jmadruga.catalogservice.model.DTO.CatalogDTO;
import com.jmadruga.catalogservice.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {
	private final CatalogService catalogService;

	@Autowired
	public CatalogController(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	/**
	 * Devuelve un catalogo de peliculas segun el genero solicitado
	 *
	 * @param genre string con el genero del que se solicita un catálogo
	 * @return
	 */
	@GetMapping("/{genre}")
	public ResponseEntity<CatalogDTO> getMovieCatalogByGenre(@PathVariable String genre) {
		CatalogDTO catalog = catalogService.getMovieCatalogByGenre(genre);
		return Objects.isNull(catalog)
				? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(catalog, HttpStatus.OK);
	}
}
