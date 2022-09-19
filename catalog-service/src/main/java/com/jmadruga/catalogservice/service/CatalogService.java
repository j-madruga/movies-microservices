package com.jmadruga.catalogservice.service;

import com.jmadruga.catalogservice.model.DTO.CatalogDTO;
import com.jmadruga.catalogservice.model.DTO.MovieDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CatalogService {
	private final MovieFeignClient movieFeignClient;

	@Autowired
	public CatalogService(MovieFeignClient movieFeignClient) {
		this.movieFeignClient = movieFeignClient;
	}

	/**
	 * devuelve un catalogo de peliculas segun el genero solicitado
	 *
	 * @param genre string con el genero del que se quiere obtener el catálogo
	 * @return un catalogDTO
	 */
	public CatalogDTO getMovieCatalogByGenre(String genre) {
		ResponseEntity<List<MovieDTO>> moviesResponse = movieFeignClient.getCatalogByGenre(genre);
		// Imprime por consola los headers con el puerto y custom
		printHeaders(moviesResponse);
		// Si la rta es correcta Se construye CatalogDTO para devolver
		if (moviesResponse.getStatusCode().is2xxSuccessful() && !Objects.isNull(moviesResponse.getBody())) {
			CatalogDTO catalog = new CatalogDTO();
			catalog.setGenre(genre);
			catalog.setMovies(moviesResponse.getBody());
			return catalog;
		}
		return null;
	}

	/**
	 * Imprime el header port y movie-response-header del response de movie-service
	 *
	 * @param moviesResponse
	 */
	private void printHeaders(ResponseEntity<List<MovieDTO>> moviesResponse) {
		// para ver el puerto del movie-service en la consola de catalog-service
		List<String> responsePort = moviesResponse.getHeaders().get("port");
		responsePort.forEach(System.out::println);
	}

	/**
	 * devuelve un catalogo de peliculas segun el genero solicitado o lanza exception
	 *
	 * @param genre string con el genero del que se quiere obtener el catálogo
	 * @return un catalogDTO
	 */
	@CircuitBreaker(name = "movie", fallbackMethod = "movieFallbackMethod")
	public CatalogDTO getMovieCatalogByGenreOrThrowError(String genre, Boolean throwError) {
		ResponseEntity<List<MovieDTO>> moviesResponse = movieFeignClient.getCatalogOrThrowError(genre, throwError);
		CatalogDTO catalog = new CatalogDTO();
		// Imprime por consola los headers con el puerto y custom
		printHeaders(moviesResponse);
		// Si la rta es correcta Se construye CatalogDTO para devolver
		if (moviesResponse.getStatusCode().is2xxSuccessful() && !Objects.isNull(moviesResponse.getBody())) {
			catalog.setGenre(genre);
			catalog.setMovies(moviesResponse.getBody());
		}
		return catalog;
	}

	public CatalogDTO movieFallbackMethod(CallNotPermittedException exception) {
		System.out.println("CIRCUIT BREAKER :: Se activa  por fallo en :: MOVIE-SERVICE");
		return new CatalogDTO();
	}
}
