package com.eeesns.tshow.util;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.entity.Role;

@Component
public class RoleEntity {
	@Resource
	BaseDao baseDao;
	private List<Role> roles;
	public static String czqxGroup = "0";
	public static String ckpl = "1";
	public static String zpl = "2";
	// 企业管理权限组
	public static String qyglGroup = "26";
	// 查看企业子账户
	public static String ckqyzzh = "27";
	// 分配企业子账户
	public static String fpqizzh = "28";
	// 冻结企业子账户
	public static String djqyzzh = "29";
	// 邀请码操作权限组
	public static String yqmczGroup = "44";
	// 发送邀请码
	public static String fsyqm = "45";

	public List<Role> getRoles() {
		if (roles == null) {
			String hql = "from Role";
			List<Role> roles = baseDao.findListByHql(hql);
			JSONArray.fromObject(roles).toString();
			this.roles = roles;
		}
		return this.roles;
	}

}
