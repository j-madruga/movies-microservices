package com.jmadruga.movieservice.service;

import com.jmadruga.movieservice.models.Movie;
import com.jmadruga.movieservice.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Service
public class MovieService {
	public static Logger LOG = LoggerFactory.getLogger(MovieService.class);
	@Value("${queue.movie.name}")
	private String movieQueue;
	private final MovieRepository movieRepository;

	private final RabbitTemplate rabbitTemplate;
	@Value("${server.port}")
	private String port;

	@Autowired
	public MovieService(MovieRepository movieRepository, RabbitTemplate rabbitTemplate) {
		this.movieRepository = movieRepository;
		this.rabbitTemplate = rabbitTemplate;
	}

	public List<Movie> findMoviesByGenre(String genre, Boolean throwError) throws Exception{
		LOG.info("MOVIE-SERVICE :: Se buscan las peliculas en el repositorio");
		if (throwError) {
			LOG.error("MOVIE-SERVICE :: Error en busqueda de movies por género");
			throw new Exception("Error accediendo al servidor de Movies");
		}
		return this.movieRepository.findByGenre(genre);
	}

	/**
	 * Guarda una Movie en MOVIE-SERVICE y
	 * la envía a una queue de Rabbit para que CATALOG-SERVICE la reciba
	 *
	 * @param movie pelicula que sera guardada y enviada a la queue
	 * @return devuelve una pelicula
	 */
	public Movie saveMovie(Movie movie) {
		Movie savedMovie = movieRepository.save(movie);
		LOG.info("Pelicula guardada correctamente, se enviará a queue Movie");
		rabbitTemplate.convertAndSend(movieQueue, savedMovie);
		LOG.info("Se ha enviado a la cola");
		return savedMovie;
	}

	public void addPortHeader(HttpServletResponse response) {
		response.addHeader("port", port);
		System.out.println("movie-service port: " + port);
	}
}
