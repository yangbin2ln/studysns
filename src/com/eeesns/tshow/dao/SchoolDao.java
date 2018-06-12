package com.eeesns.tshow.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import sparknet.starshine.common.util.StringUtil;

import com.eeesns.tshow.entity.School;

@Component
public class SchoolDao {
	@Resource
	BaseDao baseDao;

	public void updateLabelCount(String schoolId) {
		String sql = "update school set label_count = label_count+1 where school_id = '" + schoolId
				+ "'";
		baseDao.executeUpdateBySql(sql);

	}

	public void updateLabelCountBySchoolLabel(String schoolId, String labelId) {
		String sql = "update school_label set product_count = product_count+1 where school_id = '"
				+ schoolId + "' and label_id ='" + labelId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	public School findSchoolBySchoolId(String schoolId) {
		if (StringUtil.isEmpty(schoolId)) {
			return null;
		}
		School school = (School) baseDao.findEntity(School.class, schoolId);
		return school;
	}

	public List findSchoolsByKey(String key) {
		String sql = "select sc.school_id schoolId,sc.name schoolName ,sc.badge badge from school sc where sc.name like '%"
				+ key + "%'";
		Page page2 = baseDao.findPageBySql(sql, 0, 5, false, new String[] {});
		return page2.getResult();
	}

	public List findSchoolsByProvince(String provinceId, String studentId) {
		String sql = "SELECT sc.school_id schoolId, sc. NAME schoolName,"
				+ "	pr.province_id provinceId,	pr.name provinceName, cpsa.area_id areaId"
				+ " FROM school sc JOIN province pr ON ( sc.province_id = pr.province_id )"
				+ " LEFT JOIN (select area_id from company_product_show_area cpsa where student_id ='"
				+ studentId + "') cpsa " + " ON (cpsa.area_id = sc.school_id)"
				+ " WHERE pr.province_id = '" + provinceId + "'";
		List list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	public void unLock(String schoolId) {
		String sql = "update school set state ='Y',student_count=(student_count+1) where school_id = ?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, schoolId);
		sqlQuery.executeUpdate();
	}
}
