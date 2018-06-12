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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) 
@Table(name="attention")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Attention implements java.io.Serializable{
	@Id
	@Column(name = "attention_id")
	private String attentionId=UUUID.getNextIntValue();
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "to_student_id")
	private String toStudentId;
	@Column(name = "create_time")
	private String createTime;
	public String getAttentionId() {
		return attentionId;
	}
	public void setAttentionId(String attentionId) {
		this.attentionId = attentionId;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
