package com.asinenko.brainfoxnews;

public class NewsListItem {
	private String name;
	private String date;
	private String text;
	public NewsListItem(String name, String date, String text) {
		super();
		this.name = name;
		this.date = date;
		this.text = text;
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
