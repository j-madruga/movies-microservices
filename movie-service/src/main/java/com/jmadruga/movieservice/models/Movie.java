package com.jmadruga.movieservice.models;

import javax.persistence.*;

@Entity
@Table(name = "movie")
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "movie_name")
	private String movieName;
	private String genre;
	@Column(name = "url_stream")
	private String urlStream;

	public Movie() {
		// No-args constructor
	}

	public String getMovieName() {
		return movieName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getUrlStream() {
		return urlStream;
	}

	public void setUrlStream(String urlStream) {
		this.urlStream = urlStream;
	}

	@Override
	public String toString() {
		return "Movie{" +
				"id=" + id +
				", movieName ='" + movieName + '\'' +
				", genre='" + genre + '\'' +
				", urlStream='" + urlStream + '\'' +
				'}';
	}
}
