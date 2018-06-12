package com.eeesns.tshow.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "course")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Course {
	@Id
	@Column(name = "course_id")
	private String courseId = UUUID.getNextIntValue();
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "place")
	private String place;
	@Column(name = "telephone")
	private String telephone;
	@Column(name = "content")
	private String content;
	@Column(name = "count")
	private Integer count = 0;
	@Column(name = "count_limit")
	private Integer countLimit = 50;
	@Column(name = "create_time")
	private String createTime;

	public Integer getCountLimit() {
		return countLimit;
	}

	public void setCountLimit(Integer countLimit) {
		this.countLimit = countLimit;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
