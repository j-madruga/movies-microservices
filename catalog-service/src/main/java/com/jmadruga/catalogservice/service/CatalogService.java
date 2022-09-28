package com.jmadruga.catalogservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmadruga.catalogservice.model.Catalog;
import com.jmadruga.catalogservice.model.DTO.CatalogDTO;
import com.jmadruga.catalogservice.model.DTO.MovieDTO;
import com.jmadruga.catalogservice.repository.CatalogRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CatalogService {
	private final ObjectMapper mapper = new ObjectMapper();
	private final CatalogRepository catalogRepository;
	private final MovieFeignClient movieFeignClient;
	private static final Logger LOG = LoggerFactory.getLogger(CatalogService.class);
	@Autowired
	public CatalogService(CatalogRepository catalogRepository, MovieFeignClient movieFeignClient) {
		this.catalogRepository = catalogRepository;
		this.movieFeignClient = movieFeignClient;
	}

	/**
	 * devuelve un catalogo de peliculas segun el genero solicitado
	 *
	 * @param genre string con el genero del que se quiere obtener el catálogo
	 * @return un catalogDTO
	 */
	public CatalogDTO getMovieCatalogByGenre(String genre) {
		LOG.info("CATALOG-SERVICE :: Se hace la petición a movie-service");
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
	 * @param moviesResponse respuesta de microservicio movie-service
	 */
	private void printHeaders(ResponseEntity<List<MovieDTO>> moviesResponse) {
		// para ver el puerto del movie-service en la consola de catalog-service
		List<String> responsePort = moviesResponse.getHeaders().get("port");
		if (Objects.nonNull(responsePort) && !responsePort.isEmpty()) {
			responsePort.forEach(System.out::println);
		}
	}

	/**
	 * devuelve un catalogo de peliculas segun el genero solicitado o lanza exception
	 *
	 * @param genre string con el genero del que se quiere obtener el catálogo
	 * @return un catalogDTO
	 */
	@CircuitBreaker(name = "movie", fallbackMethod = "movieFallbackMethod")
	@Retry(name = "movie")
	public CatalogDTO getMovieCatalogByGenreOrThrowError(String genre, Boolean throwError) {
		LOG.info("CATALOG-SERVICE :: Se hace la petición a movie-service, puede lanzar exception.");
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


	@RabbitListener(queues = "${queue.movie.name}")
	public String saveMovieToCatalog(MovieDTO movie) {
		LOG.info("Se recibe una movie por RabbitMQ y se la guarda en el Catalog");
		String response = "Pelicula guardada exitosamente en el catalogo";
		Optional<Catalog> catalogOptional;
		Catalog catalog;
		String genre = movie.getGenre();

		if (movie.getGenre() == null || movie.getGenre().isEmpty() || movie.getGenre().equals(" ")) {
			response = "es necesario que la pelicula tenga un genero, para poder ser guardada "
						+ "en el catalogo correspondiente";
		}

		catalogOptional = catalogRepository.findByGenre(genre);

		if (!catalogOptional.isPresent()) {
			response = "ha ocurrido un error encontrando el catalogo seleccionado";
		}

		catalog = catalogOptional.get();
		catalog.getMovies().add(movie);
		catalogRepository.save(catalog);
		return response;
	}

	public CatalogDTO movieFallbackMethod(CallNotPermittedException exception) {
		System.out.println("ERROR :: CIRCUIT BREAKER :: Se activa  por fallo en :: MOVIE-SERVICE");
		return new CatalogDTO();
	}
}
