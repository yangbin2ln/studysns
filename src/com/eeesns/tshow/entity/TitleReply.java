package com.eeesns.tshow.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import com.eeesns.tshow.util.UUUID;
import com.eeesns.tshow.util.ZhengzeUtil;

@Entity
@Table(name = "title_reply")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TitleReply implements java.io.Serializable{
	public String getTitleReplyId() {
		return titleReplyId;
	}

	public void setTitleReplyId(String titleReplyId) {
		this.titleReplyId = titleReplyId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<NotationReply> getNotationReplys() {
		return notationReplys;
	}

	public void setNotationReplys(List<NotationReply> notationReplys) {
		this.notationReplys = notationReplys;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Student getReplyStudent() {
		return replyStudent;
	}

	public void setReplyStudent(Student replyStudent) {
		this.replyStudent = replyStudent;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = ZhengzeUtil.filterNoSecurity(titleName);
	}

	public Map getHotParams() {
		return hotParams;
	}

	public void setHotParams(Map hotParams) {
		this.hotParams = hotParams;
	}

	public List getNrPersonList() {
		return nrPersonList;
	}

	public void setNrPersonList(List nrPersonList) {
		this.nrPersonList = nrPersonList;
	}

	public Integer getNrPersonListCount() {
		return nrPersonListCount;
	}

	public void setNrPersonListCount(Integer nrPersonListCount) {
		this.nrPersonListCount = nrPersonListCount;
	}

	@Id
	@Column(name = "title_reply_id")
	private String titleReplyId = UUUID.getNextIntValue();
	@Column(name = "product_id")
	private String productId;
	@Column(name = "title_name")
	private String titleName;
	@ManyToOne()
	@JoinColumn(name = "reply_student_id")
	private Student replyStudent;
	@Column(name = "start")
	private String start;
	@Column(name = "end")
	private String end;
	@Column(name = "create_time")
	private String createTime;
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "title_reply_id")
	@OrderBy("create_time desc")
	private List<NotationReply> notationReplys;
	// 热度参数
	@Transient
	private Map hotParams;
	//此标题下的某个用户的意见(一级评论)
	@Transient
	private List nrPersonList;
	@Formula("(select count(1) from notation_reply nr where nr.title_reply_id = title_reply_id)")
	private Integer nrPersonListCount;
}
