package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "report")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Report implements java.io.Serializable {
	@Id
	@Column(name = "report_id")
	private String reportId = UUUID.getNextIntValue();
	@Column(name = "create_time")
	private String createTime;
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "product_id")
	private String productId;
}
