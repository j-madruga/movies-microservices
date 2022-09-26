package com.jmadruga.movieservice.service;

import com.jmadruga.movieservice.models.Movie;
import com.jmadruga.movieservice.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class MovieService {
	private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);
	private final MovieRepository movieRepository;
	@Value("${server.port}")
	private String port;

	@Autowired
	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public List<Movie> findMoviesByGenre(String genre, Boolean throwError) throws Exception{
		LOG.info("MOVIE-SERVICE :: Se buscan las peliculas en el repositorio");
		if (throwError) {
			LOG.error("MOVIE-SERVICE :: Error en busqueda de movies por género");
			throw new Exception("Error accediendo al servidor de Movies");
		}
		return this.movieRepository.findByGenre(genre);
	}

	public void addPortHeader(HttpServletResponse response) {
		response.addHeader("port", port);
		System.out.println("movie-service port: " + port);
	}
}
