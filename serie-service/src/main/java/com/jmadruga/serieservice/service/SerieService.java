package com.jmadruga.serieservice.service;

import com.jmadruga.serieservice.model.Serie;
import com.jmadruga.serieservice.repository.SerieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {
	@Value("${queue.serie.name}")
	private String serieQueue;
	public static Logger LOG = LoggerFactory.getLogger(SerieService.class);
	private final SerieRepository serieRepository;

	private final RabbitTemplate rabbitTemplate;

	@Autowired
	public SerieService(SerieRepository serieRepository, RabbitTemplate rabbitTemplate) {
		this.serieRepository = serieRepository;
		this.rabbitTemplate = rabbitTemplate;
	}

	public List<Serie> getListByGenre(String genre) {
		LOG.info("Buscando series según género");
		return serieRepository.findAllByGenre(genre);
	}

	public Serie save(Serie serie) {
		LOG.info("Guardando serie");
		return serieRepository.save(serie);
	}

	public void saveSerieRabbit(Serie serie){
		rabbitTemplate.convertAndSend(serieQueue, serie);
	}
}
