package com.eeesns.tshow.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class ProfessionDao {
	@Resource
	BaseDao baseDao;

	public List findProfessionListByKey(String key) {
		String sql = "select pr.profession_name professionName,pr.profession_id professionId "
				+ " from profession pr where pr.profession_name like '%" + key + "%'";
		Page page2 = baseDao.findPageBySql(sql, 0, 5, false, new String[] {});
		return page2.getResult();
	}
}
