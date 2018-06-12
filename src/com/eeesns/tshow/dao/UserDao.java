package com.eeesns.tshow.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.eeesns.tshow.entity.ApplyRegister;
import com.eeesns.tshow.entity.InvitationCode;
import com.eeesns.tshow.entity.Student;

@Component
public class UserDao {
	@Resource
	BaseDao baseDao;

	public Student findStudentByUserNameAndPassword(String userName, String password) {
		String hql = "from Student s where s.email = ? or s.invitationId = ?";
		Session session = baseDao.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, userName);
		query.setString(1, userName);
		Student student = (Student) query.uniqueResult();
		return student;
	}

	public List<String> findModulesById(String studentId) {
		List list = new ArrayList();
		String sql = "select pr.url url from student st join role_privilege rp on(st.role_id = rp.role_id) "
				+ " join privilege pr on(pr.privilege_id = rp.privilege_id) where st.student_id = '"
				+ studentId + "' and pr.type='show' order by pr.privilege_id";
		List<Map> modules = baseDao.findListBySql(sql, new String[] {});
		for (Map m : modules) {
			list.add(m.get("url"));
		}
		return list;
	}

	/**
	 * 查看此激活码是否存在
	 * 
	 * @return
	 */
	public InvitationCode findInvitationCodeByKey(String keycode) {
		String hql = "from InvitationCode ic where ic.keycode = ?";
		Session session = baseDao.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, keycode);
		return (InvitationCode) query.uniqueResult();
	}

	public void saveYzmEmail(String yzm, String email) {
		String sql = "insert into yzm_email(yzm,email)values(?,?)";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, yzm);
		sqlQuery.setString(1, email);
		sqlQuery.executeUpdate();
	}

	public Map findYzmValid(String yzm, String email) {
		String sql = "select * from  yzm_email ye where ye.yzm = ? and ye.email= ? and ye.create_time >DATE_SUB(NOW(),INTERVAL 30 MINUTE)"
				+ " order by ye.create_time desc limit 1 ";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, yzm);
		sqlQuery.setString(1, email);
		List list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() > 0) {
			return (Map) list.get(0);
		}
		return null;
	}

	/**
	 * 初次完善信息
	 * 
	 * @param realName
	 * @param headIco
	 * @param schoolId
	 * @param jobId
	 * @param professionId
	 * @param year
	 * @param signature
	 * @param roleId
	 * @param studentId
	 */
	public void updateUserInfo(String realName, String headIco, String schoolId, String jobId,
			String professionId, String year, String signature, String roleId, String studentId) {
		String sql = "update student set realname = ?,head_ico= ?,school_id = ?,job_id=?,"
				+ " professional_id=?,year=?,signature=?,perfect_info=?,role_id = ? where student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, realName);
		sqlQuery.setString(1, headIco);
		sqlQuery.setString(2, schoolId);
		sqlQuery.setString(3, jobId);
		sqlQuery.setString(4, professionId);
		sqlQuery.setString(5, year);
		sqlQuery.setString(6, signature);
		sqlQuery.setString(7, "Y");
		sqlQuery.setString(8, roleId);
		sqlQuery.setString(9, studentId);
		sqlQuery.executeUpdate();
	}

	/**
	 * 更新可以修改的信息
	 * 
	 * @param headIco
	 * @param jobId
	 * @param signature
	 * @param studentId
	 */
	public void updateUserInfo(String headIco, String jobId, String signature, String studentId) {
		String sql = "update student set head_ico= ?,job_id=?,"
				+ " signature=? where student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, headIco);
		sqlQuery.setString(1, jobId);
		sqlQuery.setString(2, signature);
		sqlQuery.setString(3, studentId);
		sqlQuery.executeUpdate();

	}

	public void updateUserPwd(String pwd, String studentId) {
		String sql = "update student set password = ? where student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, pwd);
		sqlQuery.setString(1, studentId);
		sqlQuery.executeUpdate();
	}

	/**
	 * 配置用户作品的曝光范围(学校)
	 * 
	 * @param schoolId
	 * @param studentId
	 */
	public void saveProductShowArea(String schoolId, String studentId) {
		String sql = "insert into company_product_show_area(student_id,area_id)values(?,?)";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.setString(1, schoolId);
		sqlQuery.executeUpdate();
	}

	public void deleteProductShowArea(String studentId) {
		String sql = "delete from company_product_show_area where student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.executeUpdate();
	}

	public void deleteProductShowYear(String studentId) {
		String sql = "delete from company_product_show_year where student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.executeUpdate();

	}

	public void saveProductShowYear(String year, String studentId) {
		String sql = "insert into company_product_show_year(student_id,grade)values(?,?)";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.setString(1, year);
		sqlQuery.executeUpdate();

	}

	public void deleteYzm(String email) {
		if (email.toUpperCase().contains("OR")) {
			return;
		}
		String sql = "delete from yzm_email where email= '" + email + "' ";
		baseDao.executeUpdateBySql(sql);
	}

	public void findApplyResiter(ApplyRegister applyRegister) {
		baseDao.saveEntity(applyRegister);
	}
}
