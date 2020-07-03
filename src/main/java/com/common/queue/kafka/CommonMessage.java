package com.common.queue.kafka;

import java.io.Serializable;

public class CommonMessage implements Serializable {
	
	private String content;
	
	private long id;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
