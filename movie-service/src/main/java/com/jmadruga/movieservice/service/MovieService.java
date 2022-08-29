package com.jmadruga.movieservice.service;

import com.jmadruga.movieservice.models.Movie;
import com.jmadruga.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
	private final MovieRepository movieRepository;

	@Autowired
	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public List<Movie> findMoviesByGenre(String genre) {
		return this.movieRepository.findByGenre(genre);
	}
}
