package com.asinenko.brainfoxnews.items;

import java.util.LinkedList;
import java.util.List;

public class NewsItemJSON {
	private String id;
	private String name;
	private String shorttext;
	private String data;
	private String type;
	public static List<String> deleted = new LinkedList<String>();
	public static String timestamp;
	public static String errorcode;

	public NewsItemJSON(String id, String name, String shorttext, String data, String type) {
		super();
		this.id = id;
		this.name = name;
		this.shorttext = shorttext;
		this.data = data;
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
	public String getShorttext() {
		return shorttext;
	}
	public void setShorttext(String shorttext) {
		this.shorttext = shorttext;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
