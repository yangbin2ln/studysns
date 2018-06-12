package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eeesns.tshow.util.UUUID;
import com.eeesns.tshow.util.ZhengzeUtil;

@Entity
@Table(name="notation_reply_reply")
public class NotationReplyReply implements java.io.Serializable {
	@Id
	@Column(name="notation_reply_reply_id")
	private String notationReplyReplyId=UUUID.getNextIntValue();
	@Column(name="notation_reply_id")
	private String notationReplyId;
	@Column(name="to_notation_reply_reply_id")
	private String toNotationReplyReplyId;
	@ManyToOne
	@JoinColumn(name="reply_student_id")
	private Student replyStudent;
	@ManyToOne
	@JoinColumn(name="toreply_student_id")
	private Student toreplyStudent;
	@Column(name="reply_content")
	private String replyContent;
	@Column(name="create_time")
	private String createTime;
	@Column(name="isadopt")
	private String isadopt="N";
	@Column(name="praise_count")
	private Integer praiseCount=0;
	@Transient
	private Integer myPraiseCount=0;
	public String getToNotationReplyReplyId() {
		return toNotationReplyReplyId;
	}
	public void setToNotationReplyReplyId(String toNotationReplyReplyId) {
		this.toNotationReplyReplyId = toNotationReplyReplyId;
	}
	public Integer getMyPraiseCount() {
		return myPraiseCount;
	}
	public void setMyPraiseCount(Integer myPraiseCount) {
		this.myPraiseCount = myPraiseCount;
	}
	public String getIsadopt() {
		return isadopt;
	}
	public void setIsadopt(String isadopt) {
		this.isadopt = isadopt;
	}
	public Integer getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}
	public String getNotationReplyReplyId() {
		return notationReplyReplyId;
	}
	public void setNotationReplyReplyId(String notationReplyReplyId) {
		this.notationReplyReplyId = notationReplyReplyId;
	}
	public String getNotationReplyId() {
		return notationReplyId;
	}
	public void setNotationReplyId(String notationReplyId) {
		this.notationReplyId = notationReplyId;
	}
	public Student getReplyStudent() {
		return replyStudent;
	}
	public void setReplyStudent(Student replyStudent) {
		this.replyStudent = replyStudent;
	}
	public Student getToreplyStudent() {
		return toreplyStudent;
	}
	public void setToreplyStudent(Student toreplyStudent) {
		this.toreplyStudent = toreplyStudent;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = ZhengzeUtil.filterNoSecurity(replyContent);
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public boolean equals(Object obj) {
		if(this==obj)return true;
		if(obj instanceof NotationReplyReply){
			if(this.notationReplyReplyId==((NotationReplyReply)obj).getNotationReplyReplyId())return true;
			
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.notationReplyReplyId.hashCode();
	}
	@Override
	public String toString() {
		return "notationReplyReplyId:"+this.notationReplyReplyId;
	}
}
