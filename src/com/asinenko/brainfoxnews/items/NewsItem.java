package com.asinenko.brainfoxnews.items;

import java.util.List;

public class NewsItem {
	private String id = "";
	private String name = "";
	private String date = "";
	private String shorttext = "";
	private String text = "";
	private List<String> images;

	public static String timestamp = "";
	public static String errorcode = "";

	public NewsItem(String id, String name, String shorttext, String text, String date) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.shorttext = shorttext;
		this.text = text;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getShorttext() {
		return shorttext;
	}
	public void setShorttext(String shorttext) {
		this.shorttext = shorttext;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
}
