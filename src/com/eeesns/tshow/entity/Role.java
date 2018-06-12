package com.eeesns.tshow.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eeesns.tshow.util.UUUID;

@Entity
@Table(name = "role")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Role implements java.io.Serializable {
	@Id
	@Column(name = "role_id")
	private String roleId;
	@Column(name = "role_type")
	private String roleType;
	@Column(name = "type_name")
	private String roleName;
	@Column(name = "create_time")
	private String createTime;
	@Column(name = "apply_code_count")
	private String applyCodeCount;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_privilege", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "privilege_id", referencedColumnName = "privilege_id") })
	private List<Privilege> privileges;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getApplyCodeCount() {
		return applyCodeCount;
	}

	public void setApplyCodeCount(String applyCodeCount) {
		this.applyCodeCount = applyCodeCount;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

}
