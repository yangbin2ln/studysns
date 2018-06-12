package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "privilege")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Privilege {
	@Id
	@Column(name = "privilege_id")
	private String privilegeId;
	@Column(name = "privilege_pid")
	private String privilegePid;
	@Column(name = "url")
	private String url;
	@Column(name = "privilege_name")
	private String privilegeName;
	@Column(name = "type")
	private String type;
	@Column(name = "create_time")
	private String createTime;

	public String getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(String privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilegePid() {
		return privilegePid;
	}

	public void setPrivilegePid(String privilegePid) {
		this.privilegePid = privilegePid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
