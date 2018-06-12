package com.eeesns.tshow.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;
import com.sun.org.glassfish.gmbal.ManagedAttribute;

@Entity
// @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "label")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Label implements java.io.Serializable {
	@Id
	@Column(name = "label_id")
	private String labelId = UUUID.getNextIntValue();
	@Column(name = "label_name")
	private String labelName;
	@Column(name = "type")
	private String type;
	@Column(name = "background_image_big")
	private String backImaB;
	@Column(name = "background_image_small")
	private String backImaS;
	@Column(name = "student_count")
	private Integer studentCount = 0;
	@Column(name = "product_count")
	private Integer productCount = 0;
	@Column(name = "create_time", insertable = false)
	// 该列不作为生成插入sql语句中的列
	private String createTime;
	@Column(name = "label_parent_id")
	private String labelParentId;
	@Column(name = "background_category")
	private String backgroundCategory;
	@JoinColumn(name = "pid")
	private String pid;
	@Transient
	private List<Label> children;

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		if (labelId.equals("")) {
			labelId = null;
		} else {
			this.labelId = labelId;
		}
	}

	public List<Label> getChildren() {
		return children;
	}

	public void setChildren(List<Label> children) {
		this.children = children;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
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

	public String getLabelParentId() {
		return labelParentId;
	}

	public void setLabelParentId(String labelParentId) {
		this.labelParentId = labelParentId;
	}

	public String getBackgroundCategory() {
		return backgroundCategory;
	}

	public void setBackgroundCategory(String backgroundCategory) {
		this.backgroundCategory = backgroundCategory;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getBackImaB() {
		return backImaB;
	}

	public void setBackImaB(String backImaB) {
		this.backImaB = backImaB;
	}

	public String getBackImaS() {
		return backImaS;
	}

	public void setBackImaS(String backImaS) {
		this.backImaS = backImaS;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Label) {
			if (this.labelId.equals(((Label) obj).getLabelId()))
				return true;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return labelId.hashCode();
	}

}
