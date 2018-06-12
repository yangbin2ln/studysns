package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) 
@Table(name="praise")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Praise implements java.io.Serializable{
	@Id
	@Column(name="praise_id")
	private String praiseId;
	@Column(name="create_time")
	private String createTime;
	@Column(name="student_id")
	private String studentId;
	@Column(name="product_id")
	private String productId;
	public String getPraiseId() {
		return praiseId;
	}
	public void setPraiseId(String praiseId) {
		this.praiseId = praiseId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
}
