package pers.ljy.download.ngastack.toexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;

public class MessageDetail {

	@ExcelProperty("楼层")
	private String floor;
	@ExcelProperty("时间")
	private String date;
	@ExcelProperty("作者")
	private String author;
	@ExcelProperty("回复")
	private String reply;
	@ExcelProperty("内容")
	private String content;

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MessageDetail [floor=" + floor + ", date=" + date + ", author=" + author + ", reply=" + reply
				+ ", content=" + content + "]";
	}
	
	
}
