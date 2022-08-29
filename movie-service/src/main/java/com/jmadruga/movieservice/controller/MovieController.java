package com.jmadruga.movieservice.controller;

import com.jmadruga.movieservice.models.Movie;
import com.jmadruga.movieservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
	@Value("${server.port}")
	private String port;
	private final MovieService movieService;

	@Autowired
	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping("/{genre}")
	public ResponseEntity<List<Movie>> findMoviesByGenre(
			@PathVariable String genre,
			HttpServletResponse response) {
		List<Movie> movies = movieService.findMoviesByGenre(genre);
		response.addHeader("port", port);
		System.out.println("movie-service port: " + port);
		return movies.isEmpty()
				? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(movies, HttpStatus.OK);
	}
}
