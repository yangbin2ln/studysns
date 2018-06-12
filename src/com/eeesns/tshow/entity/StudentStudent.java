package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "student_student")
@DynamicUpdate(true)
@DynamicInsert(true)
public class StudentStudent {
	@Id
	@Column(name = "student_student_id")
	private String studentStudentId = UUUID.getNextIntValue();
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "to_student_id")
	private String toStudentId;
	@Column(name = "state")
	private String state = "D";
	@Column(name = "create_time")
	private String createTime;
	@Column(name = "telephone")
	private String telephone;
	@Column(name = "name")
	private String name;
	@Column(name = "remark")
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStudentStudentId() {
		return studentStudentId;
	}

	public void setStudentStudentId(String studentStudentId) {
		this.studentStudentId = studentStudentId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getToStudentId() {
		return toStudentId;
	}

	public void setToStudentId(String toStudentId) {
		this.toStudentId = toStudentId;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
