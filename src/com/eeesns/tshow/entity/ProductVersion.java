package com.eeesns.tshow.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.eeesns.tshow.util.DateUtil;
import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "product_version")
@DynamicUpdate(true)
@DynamicInsert(true)
public class ProductVersion implements java.io.Serializable {
	public ProductVersion() {
		this.productId = UUUID.getNextIntValue();
	}

	@Id
	@Column(name = "product_id")
	public String productId;
	@ManyToOne()
	@JoinColumn(name = "label_id")
	private Label label;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "type")
	private String type = "T";
	@Column(name = "product_disc")
	private String productDisc;
	@Column(name = "content")
	private String content;
	@ManyToOne()
	@JoinColumn(name = "student_id")
	private Student student;
	@Column(name = "create_time", insertable = true, updatable = false)
	// 创建时间只在第一次插入时默认为当前时间，之后不会再修改
	private String createTime = DateUtil.formatDate();
	@Column(name = "last_act_time")
	private String lastActTime;
	@ManyToOne()
	@JoinColumn(name = "copyright_id")
	private Copyright copyright;
	@Column(name = "original")
	private String original = "Y";// 是否原创
	@Column(name = "version")
	private Integer version = 1;
	@Column(name = "version_count")
	private Integer versionCount;
	@Column(name = "score")
	private Integer score = 0;
	@Column(name = "width")
	private String width;
	@Column(name = "height")
	private String height;
	@Column(name = "view_count")
	private Integer viewCount = 0;
	@Column(name = "praise_count")
	private Integer praiseCount = 0;
	@Column(name = "vote_count")
	private Integer voteCount = 0;
	@Column(name = "collection_count")
	private Integer collectionCount = 0;
	@Column(name = "reply_count")
	private Integer replyCount = 0;
	@Column(name = "report_count")
	private Integer reportCount;
	@Column(name = "product_series_id")
	private String productSeriesId;
	@ManyToOne()
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
	// 当前登录用户
	@Transient
	private String nowStudentId = "";
	/* 当前用户对此作品是否收藏 */
	@Transient
	private List<Collection> collections;
	@Transient
	private List<Vote> votes;
	@Transient
	private List<Praise> praises;
	@Transient
	private List<Report> reports;
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@OrderBy("create_time desc")
	private List<TitleReply> titleReplys;
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	@JoinTable(name = "product_point", joinColumns = { @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "point_id", referencedColumnName = "point_id", insertable = false, updatable = false, nullable = false) })
	private List<Point> points = new ArrayList();
	/* 共同参与者 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_student", joinColumns = { @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "student_id", referencedColumnName = "student_id", insertable = false, updatable = false, nullable = false) })
	@Where(clause = "productstu0_.type='CU'")
	private Set<Student> productStudents;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getProductSeriesId() {
		return productSeriesId;
	}

	public void setProductSeriesId(String productSeriesId) {
		this.productSeriesId = productSeriesId;
	}

	public Integer getVersionCount() {
		return versionCount;
	}

	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	public Set<Student> getProductStudents() {
		return productStudents;
	}

	public void setProductStudents(Set<Student> productStudents) {
		this.productStudents = productStudents;
	}

	public String getNowStudentId() {
		return nowStudentId;
	}

	public void setNowStudentId(String nowStudentId) {
		this.nowStudentId = nowStudentId;
	}

	public String getLastActTime() {
		return lastActTime;
	}

	public void setLastActTime(String lastActTime) {
		this.lastActTime = lastActTime;
	}

	public List<Praise> getPraises() {
		return praises;
	}

	public void setPraises(List<Praise> praises) {
		this.praises = praises;
	}

	public List<TitleReply> getTitleReplys() {
		return titleReplys;
	}

	public void setTitleReplys(List<TitleReply> titleReplys) {
		this.titleReplys = titleReplys;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getPraiseCount() {
		return praiseCount;
	}

	public Integer getReportCount() {
		return reportCount;
	}

	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Copyright getCopyright() {
		return copyright;
	}

	public void setCopyright(Copyright copyright) {
		this.copyright = copyright;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public String getProductDisc() {
		return productDisc;
	}

	public void setProductDisc(String productDisc) {
		this.productDisc = productDisc;
	}

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Product) {
			if (this.productId == ((Product) obj).getProductId())
				return true;

		}
		return false;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public int hashCode() {
		return this.productId.hashCode();
	}

	@Override
	public String toString() {
		return "productId:" + this.productId + "productName:" + this.productName;
	}
}
