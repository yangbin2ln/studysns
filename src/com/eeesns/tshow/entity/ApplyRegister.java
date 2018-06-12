package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "apply_register")
@DynamicUpdate(true)
@DynamicInsert(true)
public class ApplyRegister {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "apply_register_id")
	private String applyRegisterId;
	@Column(name = "how_know")
	private String howKnow;
	@Column(name = "want_know")
	private String wantKnow;
	@Column(name = "product")
	private String product;
	@Column(name = "product_remark")
	private String productRemark;
	@Column(name = "email")
	private String email;
	@Column(name = "product_header")
	private String productHeader;
	@Column(name = "create_time")
	private String createTime;

	public String getApplyRegisterId() {
		return applyRegisterId;
	}

	public void setApplyRegisterId(String applyRegisterId) {
		this.applyRegisterId = applyRegisterId;
	}

	public String getHowKnow() {
		return howKnow;
	}

	public void setHowKnow(String howKnow) {
		this.howKnow = howKnow;
	}

	public String getWantKnow() {
		return wantKnow;
	}

	public void setWantKnow(String wantKnow) {
		this.wantKnow = wantKnow;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProductRemark() {
		return productRemark;
	}

	public void setProductRemark(String productRemark) {
		this.productRemark = productRemark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getProductHeader() {
		return productHeader;
	}

	public void setProductHeader(String productHeader) {
		this.productHeader = productHeader;
	}

}
