package com.eeesns.tshow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.eeesns.tshow.entity.Student;

@Component
public class SearchDao {
	@Resource
	BaseDao baseDao;

	public List findSchoolsByKey(String key,Page page) {
		String sql = "select s.school_id schoolId,s.name schoolName,s.state state,s.badge badge,s.student_count studentCount,s.label_count labelCount "
				+ " from school s where s.name like '%" + key + "%' order by s.student_count desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findLabelsByKey(String key,Page page) {
		String sql = "select l.label_id labelId,l.label_name labelName,l.pid pid,l.student_count studentCount,l.product_count productCount,"
				+ "l.background_image_small backImaS from label l where l.label_name like '%"
				+ key
				+ "%' and l.pid is not null order by l.student_count desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public HashMap findSchoolByKey(String key) {
		String sql = "select s.school_id schoolId,s.name name,s.badge badge,s.student_count studentCount,s.label_count labelCount from school s where s.name = '"
				+ key + "'";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		return map;
	}

	public Map findLabelByKey(String key) {
		String sql = "select l.label_id labelId,l.label_name labelName,l.pid pid,l.student_count studentCount,l.product_count productCount,"
				+ "l.background_image_small backImaS from label l where l.label_name = '"
				+ key
				+ "' ";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		return map;
	}

	public List findStudentsByKeyName(String key,Page page) {
		String sql = "select s.student_id studentId,s.realname realName,s.invitation_id invitationId,s.head_ico headIco,"
				+ " l.label_id labelId,l.label_name labelName,sc.school_id schoolId,sc.name schoolName "
				+ "from student s join label l on(s.job_id = l.label_id) join school sc on (sc.school_id = s.school_id)  where s.realname like '%"
				+ key + "%'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();

	}

	public Map findStudentsByKeyId(String key) {
		String sql = "select s.student_id studentId,s.realname realName,s.invitation_id invitationId,s.head_ico headIco,"
				+ " l.label_id labelId,l.label_name labelName,sc.school_id schoolId,sc.name schoolName from student s join label l on(s.job_id = l.label_id) "
				+ " join school sc on (sc.school_id = s.school_id) where s.invitation_id = '"
				+ key
				+ "'";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		return map;
	}

	public void saveSearchLog(String key, String studentId) {
		String sql = "insert into search_log(student_id,key_content)values('" + studentId + "','"
				+ key + "')";
		baseDao.executeUpdateBySql(sql);
	}

	public List findSchoolLabelBySchoolId(String schoolId,Page page) {
		String sql = "select s.school_id schoolId,s.name schoolName,l.label_id labelId,l.label_name labelName,l.background_image_big backImaS,sl.product_count productCount,sl.count studentCount "
				+ " from school_label sl join school s on(sl.school_id = s.school_id) join label l on(l.label_id = sl.label_id) where sl.school_id = '"
				+ schoolId + "'";
		Page list = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[]{});
		return list.getResult();
	}

	public List findStudentsBySchoolId(String schoolId,Page page) {
		String sql = "select s.student_id studentId,s.invitation_id invitationId,s.realname realName,s.head_ico headIco,sc.school_id schoolId,sc.name schoolName,"
				+ " l.label_id labelId,l.label_name labelName from student s join label l on(s.job_id = l.label_id) join school sc on(s.school_id = sc.school_id) where s.school_id = '"
				+ schoolId + "'";

		Page list = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[]{});
		return list.getResult();
	}

	public List findSchoolsByLabelId(String labelId,Page page) {
		String sql = "select s.school_id schoolId,s.name schoolName,s.state state,l.label_id labelId,l.label_name labelName,s.student_count studentCount,s.label_count labelCount "
				+ " from school_label sl join school s on(sl.school_id = s.school_id) join label l on(l.label_id = sl.label_id) where sl.label_id = '"
				+ labelId + "'";
		Page list = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[]{});
		return list.getResult();
	}

	public List findStudentsByLabelId(String labelId,Page page) {
		String sql = "select s.student_id studentId,s.invitation_id invitationId,s.realname realName,s.head_ico headIco,sc.school_id schoolId,"
				+ " sc.name schoolName,la2.label_id labelId,la2.label_name labelName from student_label sl join student s on (sl.student_id = s.student_id) join school sc on(s.school_id = sc.school_id)"
				+ " join label la2 on(s.job_id = la2.label_id) where sl.label_id = '"
				+ labelId
				+ "'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public Map findSchoolByStudentId(String studentId) {
		String sql = "select s.school_id schoolId,s.name schoolName,s.badge badge,s.student_count studentCount,s.label_count labelCount "
				+ " from school s join student st on(s.school_id = st.school_id) where st.student_id = '"
				+ studentId + "'";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		return map;
	}

	public List findLabelsByPid(String pid,Page page) {
		String sql = "select l.label_id labelId,l.label_name labelName,l.product_count productCount,l.student_count studentCount "
				+ " from label l where l.pid = '" + pid + "'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findLabelsByStudentId(String studentId,Page page) {
		String sql = "select la.label_id labelId,la.label_name labelName,la.product_count productCount,la.student_count studentCount"
				+ " from student_label sl join label la on(la.label_id = sl.label_id) where sl.student_id ='"
				+ studentId + "'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findPointsByKey(String key,Page page) {
		String sql = "select p.label_id labelId,p.point_name pointName,p.point_id pointId,la.label_name labelName,la.student_count studentCount,la.product_count productCount,la.background_image_big backImaS "
				+ " from point p join label la on(p.label_id = la.label_id) where p.point_name like '%"
				+ key + "%' order by p.hot desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findPLabelByKey(String key,Page page) {
		String sql = "select la.label_id labelId  from label la where la.pid is null and la.label_name like '%"
				+ key + "%' ";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findLabelsByPids(String pids,Page page) {
		pids = pids.substring(3);
		String sql = "select l.label_id labelId,l.label_name labelName,l.product_count productCount,l.student_count studentCount "
				+ " from label l where l.pid in  (" + pids + ")";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public void isAttention(List<Map> list, Student student) {
		String studentId = student.getStudentId();
		for (Map m : list) {
			String toStudentId = (String) m.get("studentId");
			String sql = "select 1 from attention at where at.student_id ='" + studentId
					+ "' and at.to_student_id ='" + toStudentId + "' limit 1";
			Map map = baseDao.findMapBySql(sql, new String[] {});
			if (map == null) {
				m.put("isAttention", false);
			} else {
				m.put("isAttention", true);
			}
		}

	}

	public List findStudentsByPointId(String pointId,Page page) {
		String sql = "select st.student_id studentId,st.realname realName,st.invitation_id invitationId,st.head_ico headIco,la.label_id labelId,la.label_name labelName,"
				+ " po.point_id pointId,po.point_name pointName "
				+ " from student_point sp join student st on(sp.student_id = st.student_id) join point po on(po.point_id = sp.point_id) join label la on(la.label_id = st.job_id) where sp.point_id = '"
				+ pointId + "' order by sp.count desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findSchoolsByPointId(String pointId,Page page) {
		String sql = "select sc.school_id schoolId,sc.name schoolName,sc.badge badge,sc.background_image_small backImaS, sc.label_count labelCount,sc.student_count studentCount,"
				+ " po.point_id pointId,po.point_name pointName"
				+ " from school_point scp join school sc on(sc.school_id = scp.school_id) join point po on(po.point_id = scp.point_id) where scp.point_id = '"
				+ pointId + "' order by scp.count desc ";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findLabelsBySchoolIdAndKey(String schoolId, String key,Page page) {
		String sql = "select sl.school_id schoolId,la.label_id labelId,la.label_name labelName,la.background_image_small backImaS  from label la join school_label sl on(la.label_id = sl.label_id) where sl.school_id = '"
				+ schoolId + "' and la.label_name like '%" + key + "%'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findPointsBySchoolIdAndKey(String schoolId, String key,Page page) {
		String sql = "select sp.school_id schoolId,po.point_id pointId,po.point_name pointName, po.background_image_small backImaS, la.label_id labelId,la.label_name labelName from point po join school_point sp on(po.point_id = sp.point_id) join label la on(po.label_id = la.label_id) where sp.school_id = '"
				+ schoolId + "' and po.point_name like '%" + key + "%' ";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public List findStudentsBySchoolIdAndKey(String schoolId, String key,Page page) {
		String sql = "select st.student_id studentId,st.realname realName,st.invitation_id invitationId,st.head_ico headIco from student st where st.school_id = '"
				+ schoolId + "' and st.realname like '%" + key + "%'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false, new String[] {});
		return page2.getResult();
	}

	public Map findStudentsByKeyIdAndSchoolId(String key, String schoolId) {
		String sql = "select s.student_id studentId,s.realname realName,s.invitation_id invitationId,s.head_ico headIco,"
				+ " l.label_id labelId,l.label_name labelName,sc.school_id schoolId,sc.name schoolName from student s join label l on(s.job_id = l.label_id) "
				+ " join school sc on (sc.school_id = s.school_id) where s.invitation_id = '"
				+ key
				+ "' and s.school_id = '"+schoolId+"'";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		return map;
	}
}
