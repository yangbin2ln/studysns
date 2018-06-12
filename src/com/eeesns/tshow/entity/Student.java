package com.eeesns.tshow.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import com.eeesns.tshow.util.UUUID;

/**
 * @author yb
 * 
 */
@Entity
@Table(name = "student")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Student implements java.io.Serializable {

	@Id
	@Column(name = "student_id")
	private String studentId = UUUID.getNextIntValue();
	@Column(name = "student_pid")
	private String studentPid;
	/*//@Version
	@Column(name = "version", length = 11)
	private int version;*/
	@Column(name = "nickname")
	private String nickName;
	@Column(name = "realname")
	private String realName;
	@Column(name = "head_ico")
	private String headIco;
	@Column(name = "sex")
	private String sex;
	@Column(name = "age")
	private Integer age;
	@Column(name = "email")
	private String email;
	@Column(name = "invitation_id")
	private String invitationId;
	@Column(name = "province_id")
	private String provinceId;
	@Column(name = "password")
	private String password;
	@Column(name = "year")
	private String year;
	@Column(name = "signature")
	private String signature;
	/* start 八大参数 */
	@Column(name = "product_count")
	private Integer productCount = 0;
	@Column(name = "collect_count")
	private Integer collectCount = 0;
	@Column(name = "suggestion_count")
	private Integer suggestionCount = 0;
	@Column(name = "attention_count")
	private Integer attentionCount = 0;
	@Column(name = "follower_count")
	private Integer followerCount = 0;
	@Column(name = "view_count")
	private Integer viewCount = 0;
	@Column(name = "praise_count")
	private Integer praiseCount = 0;
	@Column(name = "student_count")
	private Integer studentCount = 0;
	/* end 八大参数 */

	/* start 三大类别标签 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	@OrderBy(value = "product_count desc")
	private List<StudentLabel> studentLabels;
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	@OrderBy(value = "product_count desc")
	private List<StudentSuggestionLabel> studentSuggestionLabels;
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	@OrderBy(value = "product_count desc")
	private List<StudentCollectLabel> studentCollectLabels;

	/* end 三大类别标签 */
	@ManyToOne()
	// fetch = FetchType.LAZY
	@JoinColumn(name = "school_id")
	private School school;
	@ManyToOne()
	@JoinColumn(name = "job_id")
	private Label jobLabel;
	@ManyToOne()
	@JoinColumn(name = "professional_id")
	private Profession profession;
	@Column(name = "type")
	private String type;
	@Column(name = "perfect_info")
	private String perfectInfo = "N";
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	/*
	 * @OneToMany(fetch=FetchType.EAGER)//(cascade=CascadeType.ALL)//此处不允许级联改动label表
	 * 
	 * @JoinTable(name="student_interest",
	 * joinColumns={@JoinColumn(name="student_id"
	 * ,referencedColumnName="student_id"
	 * ,insertable=false,updatable=false,nullable=false)},
	 * inverseJoinColumns={@JoinColumn
	 * (name="interest_id",referencedColumnName="label_id"
	 * ,insertable=false,updatable=false,nullable=false)}) private List<Label>
	 * interest;
	 */
	// @Formula("(select count(*) from product p join praise pr on(p.product_id=pr.product_id) where p.student_id=student_id and p.label_id =professional_id)")
	/*
	 * @Transient private Integer productCount;
	 */
	// 每个标签下的俩个代表作
	@Column(name = "create_time")
	private String createTime;
	@Column(name = "state")
	private String state = "A";
	// 是否被当前用户关注
	@Transient
	private String isAttention = "N";
	@Transient
	private Integer jobProductCount = 0;
	// 用户模块展示权限列表
	@Transient
	private List<String> modules;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentPid() {
		return studentPid;
	}

	public void setStudentPid(String studentPid) {
		this.studentPid = studentPid;
	}

	/*
	 * public int getVersion() { return version; }
	 * 
	 * public void setVersion(int version) { this.version = version; }
	 */
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadIco() {
		return headIco;
	}

	public void setHeadIco(String headIco) {
		this.headIco = headIco;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public Integer getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}

	public Integer getSuggestionCount() {
		return suggestionCount;
	}

	public void setSuggestionCount(Integer suggestionCount) {
		this.suggestionCount = suggestionCount;
	}

	public Integer getAttentionCount() {
		return attentionCount;
	}

	public void setAttentionCount(Integer attentionCount) {
		this.attentionCount = attentionCount;
	}

	public Integer getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Integer followerCount) {
		this.followerCount = followerCount;
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

	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public List<StudentLabel> getStudentLabels() {
		return studentLabels;
	}

	public void setStudentLabels(List<StudentLabel> studentLabels) {
		this.studentLabels = studentLabels;
	}

	public List<StudentSuggestionLabel> getStudentSuggestionLabels() {
		return studentSuggestionLabels;
	}

	public void setStudentSuggestionLabels(List<StudentSuggestionLabel> studentSuggestionLabels) {
		this.studentSuggestionLabels = studentSuggestionLabels;
	}

	public List<StudentCollectLabel> getStudentCollectLabels() {
		return studentCollectLabels;
	}

	public void setStudentCollectLabels(List<StudentCollectLabel> studentCollectLabels) {
		this.studentCollectLabels = studentCollectLabels;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Label getJobLabel() {
		return jobLabel;
	}

	public void setJobLabel(Label jobLabel) {
		this.jobLabel = jobLabel;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPerfectInfo() {
		return perfectInfo;
	}

	public void setPerfectInfo(String perfectInfo) {
		this.perfectInfo = perfectInfo;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(String isAttention) {
		this.isAttention = isAttention;
	}

	public Integer getJobProductCount() {
		return jobProductCount;
	}

	public void setJobProductCount(Integer jobProductCount) {
		this.jobProductCount = jobProductCount;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Student() {
		System.out.println("student11111------------------------------------------------------");

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Student) {
			if (this.studentId == ((Student) obj).getStudentId())
				return true;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.studentId.hashCode();
	}

	@Override
	public String toString() {
		return "studntId:" + this.studentId + "nickName:" + this.nickName;
	}

}
