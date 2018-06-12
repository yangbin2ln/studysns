package com.eeesns.tshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "point")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Point implements java.io.Serializable, Cloneable {
	@Id
	@Column(name = "point_id")
	private String pointId = UUUID.getNextIntValue();
	@Column(name = "label_id")
	private String labelId;
	@Column(name = "point_name")
	private String pointName;
	@Column(name = "background_image_big")
	private String backImgB;
	@Column(name = "background_image_small")
	private String backImgS;
	@Column(name = "type")
	private String type = "T";// 默认图片
	@Column(name = "hot")
	private Integer hot = 0;

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public String getBackImgB() {
		return backImgB;
	}

	public void setBackImgB(String backImgB) {
		this.backImgB = backImgB;
	}

	public String getBackImgS() {
		return backImgS;
	}

	public void setBackImgS(String backImgS) {
		this.backImgS = backImgS;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
