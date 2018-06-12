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
@Table(name="student_act")
@DynamicUpdate(true)
@DynamicInsert(true)
public class StudentAct implements java.io.Serializable{
	@Id
	@Column(name="student_act_id")
	private String student_act_id=UUUID.getNextIntValue();
	@Column(name="student_id")
	private String studentId;
	@Column(name="act_type")
	private String actType;
	@Column(name="obj_type")
	private String objType;
	@Column(name="obj_id")
	private String objId;
	@Column(name="act_id")
	private String actId;
	@Column(name="create_time")
	private String createTime;
	@Column(name="to_product_id")
	private String toProductId;
	@Column(name="to_student_id")
	private String toStudentId;
	public String getToProductId() {
		return toProductId;
	}
	public void setToProductId(String toProductId) {
		this.toProductId = toProductId;
	}
	public String getToStudentId() {
		return toStudentId;
	}
	public void setToStudentId(String toStudentId) {
		this.toStudentId = toStudentId;
	}
	public String getStudent_act_id() {
		return student_act_id;
	}
	public void setStudent_act_id(String student_act_id) {
		this.student_act_id = student_act_id;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	
}
