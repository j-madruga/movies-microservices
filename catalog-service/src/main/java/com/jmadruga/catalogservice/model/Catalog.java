package com.jmadruga.catalogservice.model;

import com.jmadruga.catalogservice.model.DTO.MovieDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("catalog")
public class Catalog {
	@Id
	private String id;
	private String genre;
	private List<MovieDTO> movies;

	public Catalog() {
	}

	public Catalog(String id, String genre, List<MovieDTO> movies) {
		this.id = id;
		this.genre = genre;
		this.movies = movies;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public List<MovieDTO> getMovies() {
		return movies;
	}

	public void setMovies(List<MovieDTO> movies) {
		this.movies = movies;
	}
}
