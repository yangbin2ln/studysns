package com.eeesns.tshow.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.drools.core.io.impl.ClassPathResource;
import org.springframework.stereotype.Component;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Role;

/**
 * 通用数据类
 * 
 * @author yangbin
 * 
 */
@Component
public class CommonDataUtil {
	@Resource
	private BaseDao baseDao;

	// 角色列表初始化
	private List<Role> roleList = null;
	private static CommonDataUtil commonDataUtil = null;
	private static Map sfConfig;

	public static CommonDataUtil getInstance(BaseDao baseDao) {
		if (commonDataUtil == null) {
			synchronized (CommonDataUtil.class) {
				if (commonDataUtil == null) {
					commonDataUtil = new CommonDataUtil(baseDao);
				}
			}
		}
		return commonDataUtil;
	}

	/**
	 * 初始化通用数据
	 */
	public CommonDataUtil() {
	}

	/**
	 * 初始化通用数据
	 */
	public CommonDataUtil(BaseDao baseDao) {
		this.baseDao = baseDao;
		initRoleList();
		initScConfig();
	}

	private void initScConfig() {
		this.sfConfig = findScoreConfig();

	}

	/**
	 * 初始化所有角色数据
	 */
	private void initRoleList() {
		String hql = "from Role ";
		Page page = baseDao.findPageByHql(hql, 0, -1, false);
		this.roleList = page.getResult();
	}

	/**
	 * 根据角色类型查询角色
	 * 
	 * @param type
	 *            角色类型
	 * @return
	 */
	public Role findRoleByType(String type) {
		for (Role role : roleList) {
			if (role.getRoleType().equals(type)) {
				return role;
			}
		}
		return null;
	}

	/**
	 * 算法配置
	 * 
	 * @return
	 */
	public static Map findScoreConfig() {
		Map config = new HashMap();
		ClassPathResource fis = new ClassPathResource("properties/sfConfig.properties");
		InputStream inputStream;

		try {
			inputStream = fis.getInputStream();
			Properties p = new Properties();
			p.load(inputStream);
			inputStream.close();
			String account = p.getProperty("account");
			Set<Entry<Object, Object>> entrySet = p.entrySet();
			Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<Object, Object> next = iterator.next();
				config.put(next.getKey(), next.getValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}

	/**
	 * 将类中的属性数据置空使得数据重新加载
	 */
	public static void reload() {
		commonDataUtil = null;
	}

	public Integer findScoreByKey(String key) {
		String value = (String) sfConfig.get(key);
		return Integer.parseInt(value);
	}

	public static void main(String[] args) {
		Map map = findScoreConfig();
		System.out.println(map.get("vote"));
	}

}
