package com.eeesns.tshow.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
@DynamicUpdate(true)
@DynamicInsert(true)
public class SchoolLabelKey implements java.io.Serializable{
	private String schoolId;
	private String labelId;
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	@Override
	public boolean equals(Object obj) {
		if(this==obj)return true;
		if(obj instanceof SchoolLabelKey){
			return (((SchoolLabelKey) obj).getSchoolId()+((SchoolLabelKey) obj).getLabelId()).hashCode()==this.hashCode();
			
		}
		return false;
	}
	@Override
	public int hashCode() {
		return (this.schoolId+this.getLabelId()).hashCode();
	}
}
