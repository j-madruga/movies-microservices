package com.jmadruga.serieservice.model;

public class Chapter {
	private String id;
	private String name;
	private Integer number;
	private String urlStream;

	public Chapter() {
		// No-args constructor
	}

	public Chapter(String id, String name, Integer number, String urlStream) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.urlStream = urlStream;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getUrlStream() {
		return urlStream;
	}

	public void setUrlStream(String urlStream) {
		this.urlStream = urlStream;
	}

	@Override
	public String toString() {
		return "Chapter{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", number=" + number +
				", urlStream='" + urlStream + '\'' +
				'}';
	}
}
