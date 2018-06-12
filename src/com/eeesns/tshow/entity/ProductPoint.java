package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;


public class ProductPoint implements java.io.Serializable{
	@Id
	@Column(name="product_point_id")
	private String studentId=UUUID.getNextIntValue();
	@Column(name="product_id")
	private String productId;
	@Column(name="point_remark")
	private String pointRemark;
	@Column(name="point_id")
	private String pointId;
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
	public String getPointRemark() {
		return pointRemark;
	}
	public void setPointRemark(String pointRemark) {
		this.pointRemark = pointRemark;
	}
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	
}
