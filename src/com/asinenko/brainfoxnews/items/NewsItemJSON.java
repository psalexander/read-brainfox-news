package com.asinenko.brainfoxnews.items;

public class NewsItemJSON {
	private String id;
	private String name;
	private String shorttext;
	private String data;
	public static String timestamp;
	public static String errorcode;

	public NewsItemJSON(String id, String name, String shorttext, String data) {
		super();
		this.id = id;
		this.name = name;
		this.shorttext = shorttext;
		this.data = data;
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

}
