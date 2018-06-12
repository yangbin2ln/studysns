package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="copyright")
public class Copyright implements java.io.Serializable{
	public Copyright(){}
	@Id
	@Column(name="copyright_id")
	private String copyrightId;
	@Column(name="copyright_name")
	private String copyrightName;
	public String getCopyrightId() {
		return copyrightId;
	}
	public void setCopyrightId(String copyrightId) {
		this.copyrightId = copyrightId;
	}
	public String getCopyrightName() {
		return copyrightName;
	}
	public void setCopyrightName(String copyrightName) {
		this.copyrightName = copyrightName;
	}
	
}
