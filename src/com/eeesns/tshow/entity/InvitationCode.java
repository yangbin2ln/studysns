package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

/**
 * 邀请码邮箱关系表
 * 
 * @author yangbin
 * 
 */
@Entity
@Table(name = "invitation_code")
@DynamicUpdate(true)
@DynamicInsert(true)
public class InvitationCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invitation_code_id")
	private Integer invitationCodeId;
	@Column(name = "invitation_id")
	private String invitationId;
	@Column(name = "email")
	private String email;
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "type")
	private String type = "YQHY";
	@Column(name = "create_time")
	private String createTime;
	@Column(name = "state")
	private String state = "N";
	@Column(name = "keycode")
	private String keycode;

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
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

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public Integer getInvitationCodeId() {
		return invitationCodeId;
	}

	public void setInvitationCodeId(Integer invitationCodeId) {
		this.invitationCodeId = invitationCodeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
