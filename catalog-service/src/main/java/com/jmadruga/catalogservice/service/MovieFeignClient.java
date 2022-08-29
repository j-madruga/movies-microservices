package com.jmadruga.catalogservice.service;

import com.jmadruga.catalogservice.config.CustomLoadBalancerConfig;
import com.jmadruga.catalogservice.model.DTO.MovieDTO;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "movie-service")
@LoadBalancerClient(name = "movie-service", configuration = CustomLoadBalancerConfig.class)
public interface MovieFeignClient {

	@GetMapping("/movies/{genre}")
	ResponseEntity<List<MovieDTO>> getCatalogByGenre(@PathVariable String genre);

}
