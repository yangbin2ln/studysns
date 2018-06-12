package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "student_collect_label")
@DynamicUpdate(true)
@DynamicInsert(true)
public class StudentCollectLabel implements java.io.Serializable {
	@Id
	@Column(name = "student_collect_label_id")
	private String studentCollectLabelId = UUUID.getNextIntValue();
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "label_id")
	private Label label;
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "background_image")
	private String backgroundImage;
	@Column(name = "product_count")
	private Integer productCount = 1;

	public String getStudentCollectLabelId() {
		return studentCollectLabelId;
	}

	public void setStudentCollectLabelId(String studentCollectLabelId) {
		this.studentCollectLabelId = studentCollectLabelId;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	/*
	 * 说明：此类的hashCode和Label类的一致
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof StudentLabel) {
			if (this.hashCode() == ((StudentLabel) obj).hashCode())
				return true;

		}
		return false;
	}

	@Override
	public int hashCode() {
		return label.hashCode() + studentId.hashCode();
	}

}
