package com.asinenko.brainfoxnews.items;

import java.util.LinkedList;
import java.util.List;

public class NewsListItem {
	private String id;
	private String name;
	private String date;
	private String text;
	private String type;
	public static List<String> deleted = new LinkedList<String>();
	public static String timestamp;
	public static String errorcode;

	public NewsListItem(String id, String name, String text, String date, String type) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.text = text;
		this.type = type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
