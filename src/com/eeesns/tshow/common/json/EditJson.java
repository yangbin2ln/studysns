package com.eeesns.tshow.common.json;

import java.util.HashMap;
import java.util.Map;

import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.util.DateUtil;

public class EditJson extends JsonBean {

	public EditJson() {
	}

	private Page page;
	private boolean success = true;
	private Map map = new HashMap();;
	private String message;
	private Message mess = new Message();
	private String date = DateUtil.formatDate();

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Message getMess() {
		return mess;
	}

	public void setMess(Message mess) {
		this.mess = mess;
	}

	public Page getPage() {
		return page;
	}

	public Map getMap() {
		return map;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
