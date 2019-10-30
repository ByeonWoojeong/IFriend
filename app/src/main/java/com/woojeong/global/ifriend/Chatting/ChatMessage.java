package com.woojeong.global.ifriend.Chatting;

public class ChatMessage {

	String target, type, content, date;

	public ChatMessage(String target, String type, String content, String date) {
		this.target = target;
		this.type = type;
		this.content = content;
		this.date = date;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}