package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

/**
 * 学生报名课程表
 * 
 * @author yb
 * 
 */
@Entity
@Table(name = "student_course")
@DynamicUpdate(true)
@DynamicInsert(true)
@IdClass(StudentCourseKey.class)
public class StudentCourse {
	@Id
	@Column(name = "student_id")
	private String studentId;
	@Id
	@Column(name = "course_id")
	private String courseId;
	@Column(name = "name")
	private String name;
	@Column(name = "telephone")
	private String telephone;
	@Column(name = "remark")
	private String remark;
	@Column(name = "create_time")
	private String createTime;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
