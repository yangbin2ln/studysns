package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="recommend")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Recommend implements java.io.Serializable {
	@Id
	@Column(name="recommend_id")
	private Integer recommendId;
	@Column(name="label_id")
	private String labelId;
	@Column(name="type")
	private String type;
	@Column(name="state")
	private String state;
	@Column(name="create_time")
	private String createTime;
	public Integer getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
