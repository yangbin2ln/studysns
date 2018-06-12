package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

/**
 * 用户访问消息记录日志表
 * 
 * @author yangbin
 * 
 */
@Entity
@Table(name = "view_message")
@DynamicUpdate(true)
@DynamicInsert(true)
public class ViewMessage implements java.io.Serializable {
	@Id
	@Column(name = "view_message_id")
	private String viewMessageId = UUUID.getNextIntValue();
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "create_time")
	private String createTime;

	public String getViewMessageId() {
		return viewMessageId;
	}

	public void setViewMessageId(String viewMessageId) {
		this.viewMessageId = viewMessageId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
