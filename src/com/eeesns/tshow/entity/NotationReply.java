package com.eeesns.tshow.entity;

import java.util.List;

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

/**
 * @author yb
 * 文字作品的批注以及图片作品的打点实体类
 *
 */
@Entity
@Table(name="notation_reply")
@DynamicUpdate(true)
@DynamicInsert(true)
public class NotationReply implements java.io.Serializable{
	@Id
	@Column(name="notation_reply_id")
	private String notationReplyId=UUUID.getNextIntValue();
	//防止双向关联出现死循环
	/*@ManyToOne()
	@JoinColumn(name="product_id")
	private Product product;*/
	@Column(name="title_reply_id")
	private String titleReplyId;
	@Column(name="title")
	private String title;
	@Column(name="notation_content")
	private String notationContent;
	@Column(name="create_time")
	private String createTime;
	@Column(name="adopt_time")
	private String adoptTime;
	@ManyToOne()
	@JoinColumn(name="reply_student_id")
	private Student replyStudent;
	@Column(name="start")
	private String start;
	@Column(name="end")
	private String end;
	@Column(name="product_content")
	private String productContent;
	@OneToMany(cascade={CascadeType.ALL},fetch=FetchType.LAZY)//保存的时候如果需要级联保存需要配置此属性
	@JoinColumn(name="notation_reply_id")
	@OrderBy(value="create_time desc")
	private List<NotationReplyReply> notationReplyReplys;
	@ManyToOne()
	@JoinColumn(name="point_id")
	private Point point;
	@JoinColumn(name="src")
	private String src;
	@Column(name="isadopt")
	private String isadopt="N";
	@Column(name="praise_count")
	private Integer praiseCount=0;
	@Transient
	private Integer myPraiseCount=0;
	@Formula("(select count(1) from notation_reply_reply nrr where nrr.notation_reply_id = notation_reply_id)")
	private Integer replyReplyCount;
	public Integer getReplyReplyCount() {
		return replyReplyCount;
	}
	public void setReplyReplyCount(Integer replyReplyCount) {
		this.replyReplyCount = replyReplyCount;
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
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public String getNotationReplyId() {
		return notationReplyId;
	}
	public void setNotationReplyId(String notationReplyId) {
		this.notationReplyId = notationReplyId;
	}
	public String getNotationContent() {
		return notationContent;
	}
	public void setNotationContent(String notationContent) {
		this.notationContent = ZhengzeUtil.filterNoSecurity(notationContent);
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
	public String getProductContent() {
		return productContent;
	}
	public void setProductContent(String productContent) {
		this.productContent = productContent;
	}
	/*public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}*/
	public List<NotationReplyReply> getNotationReplyReplys() {
		return notationReplyReplys;
	}
	 
	public String getAdoptTime() {
		return adoptTime;
	}
	public void setAdoptTime(String adoptTime) {
		this.adoptTime = adoptTime;
	}
	public String getTitleReplyId() {
		return titleReplyId;
	}
	public void setTitleReplyId(String titleReplyId) {
		this.titleReplyId = titleReplyId;
	}
	public void setNotationReplyReplys(List<NotationReplyReply> notationReplyReplys) {
		this.notationReplyReplys = notationReplyReplys;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public boolean equals(Object obj) {
		if(this==obj)return true;
		if(obj instanceof NotationReply){
			if(this.notationReplyId.equals(((NotationReply)obj).getNotationReplyId()))return true;
			
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.notationReplyId.hashCode();
	}
	@Override
	public String toString() {
		return "notationReplyId:"+this.notationReplyId;
	}
	
}
