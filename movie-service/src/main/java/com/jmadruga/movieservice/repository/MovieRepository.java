package com.jmadruga.movieservice.repository;

import com.jmadruga.movieservice.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
	List<Movie> findByGenre(String genre);
}
