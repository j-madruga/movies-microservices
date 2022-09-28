package com.jmadruga.serieservice.controller;


import com.jmadruga.serieservice.model.Serie;
import com.jmadruga.serieservice.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

	private SerieService serieService;

	@Autowired
	public SerieController(SerieService serieService) {
		this.serieService = serieService;
	}

	@GetMapping("/{genre}")
	public ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre) {
		List<Serie> series = serieService.getListByGenre(genre);
		return series.isEmpty()
				? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(series, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Serie> save(@RequestBody Serie serie) {
		return ResponseEntity.ok().body(serieService.save(serie));
	}

	@PostMapping("/saveSerie")
	public ResponseEntity<String> saveSerieRabbit(@RequestBody Serie serie){
		serieService.save(serie);
		return ResponseEntity.ok("La serie se envió a la cola.");
	}
}
