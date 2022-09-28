package com.jmadruga.serieservice.model;

import java.util.List;

public class Season {
	private String id;
	private Integer seasonNumber;
	private List<Chapter> chapters;

	public Season() {
		// No-args constructor
	}

	public Season(String id, Integer seasonNumber, List<Chapter> chapters) {
		this.id = id;
		this.seasonNumber = seasonNumber;
		this.chapters = chapters;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSeasonNumber() {
		return seasonNumber;
	}

	public void setSeasonNumber(Integer seasonNumber) {
		this.seasonNumber = seasonNumber;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	@Override
	public String toString() {
		return "Season{" +
				"id='" + id + '\'' +
				", seasonNumber=" + seasonNumber +
				", chapters=" + chapters +
				'}';
	}
}
