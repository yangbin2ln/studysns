package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yb
 * 作品集
 *
 */
@Entity
@Table(name="portfolio")
public class Portfolio implements java.io.Serializable{
	@Id
	@Column(name="portfolio_id")
	private String portfolioId;
	@Column(name="product_id")
	private String productId;
	@Column(name="portfolio_name")
	private String portfolioName;
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	
	
}
