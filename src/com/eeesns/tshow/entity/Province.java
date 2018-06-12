package com.eeesns.tshow.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) 
@Table(name="province")
public class Province implements java.io.Serializable{
	@Id
	@Column(name="province_id")
	private String provinceId;
	@Column(name="name")
	private String provinceName;
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="province_id")
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) 
	private List<School> schools;
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public List<School> getSchools() {
		return schools;
	}
	public void setSchools(List<School> schools) {
		this.schools = schools;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	@Override
	public boolean equals(Object obj) {
		if(this==obj)return true;
		if(obj instanceof Province){
			if(this.provinceId==((Province)obj).getProvinceId())return true;
			
		}
		return false;
	}
	@Override
	public int hashCode() {
		return provinceId.hashCode();
	}
}
