package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author yb
 * 专业实体类
 *
 */
@Entity
@Table(name="profession")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Profession implements java.io.Serializable{
	@Id
	@Column(name="profession_id")
	private String professionId;
	@Column(name="profession_name")
	private String professionName;
	@Column(name="pid")
	private String pid;
	public String getProfessionId() {
		return professionId;
	}
	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
}
