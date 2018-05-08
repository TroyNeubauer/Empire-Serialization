package com.troy.byteviewer.guess;

import java.awt.Color;

public class AnnotatedSection {
	private long offset, length;
	private String name, description;
	private Color color;
	
	public AnnotatedSection(long offset, long length, String name, String description, Color color) {
		this.offset = offset;
		this.length = length;
		this.name = name;
		this.description = description;
		this.color = color;
	}

	public long getOffset() {
		return offset;
	}

	public long getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Color getColor() {
		return color;
	}
	
	
	
	
}
