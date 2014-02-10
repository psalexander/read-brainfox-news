package com.asinenko.brainfoxnews.items;

public class NewsListItem {
	private String id;
	private String name;
	private String date;
	private String text;
	public static String timestamp;
	public static String errorcode;

	public NewsListItem(String id, String name, String text, String date) {
		this.id = id;
		this.name = name;
		this.date = date;
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
}
