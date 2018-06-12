package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "school_label")
@IdClass(SchoolLabelKey.class)
@DynamicUpdate(true)
@DynamicInsert(true)
public class SchoolLabel implements java.io.Serializable {
	@Id
	@Column(name = "school_id")
	private String schoolId;
	@Id
	@Column(name = "label_id", insertable = false, updatable = false)
	private String labelId;
	@Column(name = "state", insertable = false)
	// insertable=false 第一次插入的时候使用默认值
	private String state = "Y";
	@Column(name = "count", insertable = false)
	private Integer count = 1;// 此处给出默认值很重要，因为此属性保存时不和数据库数据交互，所以会发生java和数据库数据不一致的情况
	@Column(name = "product_count", insertable = false)
	private Integer productCount = 1;// 此处给出默认值很重要，因为此属性保存时不和数据库数据交互，所以会发生java和数据库数据不一致的情况
	@Column(name = "unlock_time")
	private String unlockTime;
	/*@ManyToOne
	@JoinColumn(name = "label_id")
	private Label label;*/

	/*public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}*/

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public String getUnlockTime() {
		return unlockTime;
	}

	public void setUnlockTime(String unlockTime) {
		this.unlockTime = unlockTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof SchoolLabel) {
			if (this.schoolId == ((SchoolLabel) obj).getSchoolId()
					&& this.labelId == ((SchoolLabel) obj).getLabelId())
				return true;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return schoolId.hashCode();
	}

}
