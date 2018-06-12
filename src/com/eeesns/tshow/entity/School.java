package com.eeesns.tshow.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.eeesns.tshow.util.UUUID;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "school")
public class School implements java.io.Serializable {
	@Id
	@Column(name = "school_id")
	private String schoolId = UUUID.getNextIntValue();
	@Column(name = "name")
	private String name;
	@Column(name = "state")
	private String state;
	@Column(name = "province_id")
	private String provinceId;
	@Column(name = "badge")
	private String badge;
	@Column(name = "background_image_big")
	private String backImaB;
	@Column(name = "background_image_small")
	private String backImaS;
	@Column(name = "student_count")
	private Integer studentCount;// 学校注册学生的数量
	@Column(name = "label_count")
	private Integer labelCount;// 所有标签数量
	@Formula("(select count(*) from school_label sl where sl.school_id =school_id and sl.state='Y')")
	private Integer unlockLabelCount;// 已经解锁的标签数量
	@ManyToMany(fetch = FetchType.LAZY)
	// (cascade=CascadeType.ALL)//此处不允许级联改动label表
	@JoinTable(name = "school_label", joinColumns = { @JoinColumn(name = "school_id", referencedColumnName = "school_id", insertable = false, updatable = false, nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "label_id", referencedColumnName = "label_id", insertable = false, updatable = false, nullable = false) })
	@OrderBy("label_id desc")
	// @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<Label> labels;
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	// @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@Where(clause = "state='Y'")
	private Set<SchoolLabel> schoolLabels;

	public Set<SchoolLabel> getSchoolLabels() {
		return schoolLabels;
	}

	public void setSchoolLabels(Set<SchoolLabel> schoolLabels) {
		this.schoolLabels = schoolLabels;
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	/*
	 * public Province getProvince() { return province; } public void
	 * setProvince(Province province) { this.province = province; }
	 */
	public Integer getLabelCount() {
		return labelCount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
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

	public void setLabelCount(Integer labelCount) {
		this.labelCount = labelCount;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUnlockLabelCount() {
		return unlockLabelCount;
	}

	public void setUnlockLabelCount(Integer unlockLabelCount) {
		this.unlockLabelCount = unlockLabelCount;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof School) {
			if (this.schoolId == ((School) obj).getSchoolId())
				return true;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return schoolId.hashCode();
	}

}
