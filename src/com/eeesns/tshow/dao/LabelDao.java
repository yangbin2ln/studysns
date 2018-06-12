package com.eeesns.tshow.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import sparknet.starshine.common.util.StringUtil;

import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Point;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.util.CommonDataUtil;

@Component
public class LabelDao {
	@Resource
	private BaseDao baseDao;
	private List<Label> labels;

	public Object findLabels(HttpServletRequest request) {
		//if (labels == null) {
			String hql = "from Label la where la.pid is null";
			List<Label> list = baseDao.findListByHql(hql);
			for (Label la : list) {
				String labelId = la.getLabelId();
				String hql2 = "from Label la where la.pid = '" + labelId + "'";
				List<Label> list2 = baseDao.findListByHql(hql2);
				la.setChildren(list2);
			}
			this.labels = list;
		//}
		return labels;

	}

	/**
	 * 更加内容元素名称查询其id
	 * 
	 * @param name
	 * @param labelId
	 * @return
	 */
	public Point findPointIdByName(String pointName, String labelId) {
		String hql = "from Point p where p.labelId = ? and p.pointName = ?";
		List list = baseDao.findEntityByHql(hql, new String[] { labelId, pointName });
		if (list.size() > 0) {
			return (Point) list.get(0);
		}
		return null;
	}

	public void updateStudentCount(String labelId) {
		String sql = "update label set student_count = student_count+1 where label_id = '"
				+ labelId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	public void updateProductCount(String labelId) {
		String sql = "update label set product_count = product_count+1 where label_id = '"
				+ labelId + "'";
		baseDao.executeUpdateBySql(sql);

	}

	public List findPointByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			Page page) {
		String sql = "";
		if (StringUtil.isEmpty(labelId)) {
			if (StringUtil.isEmpty(schoolId)) {
				sql = "select p.point_id pointId,p.point_name pointName,p.background_image_small backImaS from point p order by p.hot desc";
			} else {
				sql = "select p.point_id pointId,p.point_name pointName from point p join school_point sp on(p.point_id = sp.point_id) "
						+ "where  sp.school_id = '" + schoolId + "' order by p.hot desc";
			}
		} else {
			if (StringUtil.isEmpty(schoolId)) {
				sql = "select p.point_id pointId,p.point_name pointName,p.background_image_small backImaS from point p where p.label_id = '"
						+ labelId + "' order by p.hot desc";
			} else {
				sql = "select p.point_id pointId,p.point_name pointName,p.background_image_small backImaS  from point p join school_point sp on(p.point_id = sp.point_id) "
						+ "where p.label_id = '"
						+ labelId
						+ "' and sp.school_id = '"
						+ schoolId
						+ "' order by p.hot desc";
			}
		}

		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2.getResult();
	}

	/**
	 * 查询用户
	 * 
	 * @param labelId
	 *            二级标签
	 * @param pointId
	 *            内容标签
	 * @param schoolId
	 *            学校id
	 * @param orderBy
	 *            排序类型
	 * @param filter
	 *            筛选类型(达人、普通用户)
	 * @param page
	 * @return
	 */
	public Page findStudentByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			String orderBy, String filter, Page page) {
		String sql = "";
		if (StringUtil.isEmpty(labelId)) {
			if (StringUtil.isEmpty(schoolId)) {
				if (StringUtil.isEmpty(pointId)) {
					sql = "select st.* from student st ";
				} else {
					sql = "select st.* from student_label sl join student st on(st.student_id = sl.student_id)"
							+ " join student_point sp on(st.student_id = sp.student_id) where sp.point_id ='"
							+ pointId + "'";
				}
			} else if (StringUtil.isEmpty(pointId)) {
				sql = "select st.* from student st where st.school_id = '" + schoolId + "' ";
			} else {
				sql = "select st.* from student st join student_label sl on(st.student_id = sl.student_id) "
						+ " join student_point sp on(st.student_id = sp.student_id) where st.school_id = '"
						+ schoolId + "' and  sp.point_id = '" + pointId + "'";
			}
		} else {
			if (StringUtil.isEmpty(schoolId)) {
				if (StringUtil.isEmpty(pointId)) {
					sql = "select st.* from student_label sl join student st on(st.student_id = sl.student_id) where sl.label_id = '"
							+ labelId + "'";
				} else {
					sql = "select st.* from student_label sl join student st on(st.student_id = sl.student_id)"
							+ " join student_point sp on(st.student_id = sp.student_id) where sl.label_id ='"
							+ labelId + "' and sp.point_id ='" + pointId + "'";
				}
			} else if (StringUtil.isEmpty(pointId)) {
				sql = "select st.* from student st join student_label sl on(st.student_id = sl.student_id) where st.school_id = '"
						+ schoolId + "' and sl.label_id = '" + labelId + "'";
			} else {
				sql = "select st.* from student st join student_label sl on(st.student_id = sl.student_id) "
						+ " join student_point sp on(st.student_id = sp.student_id) where st.school_id = '"
						+ schoolId
						+ "' and sl.label_id = '"
						+ labelId
						+ "' and sp.point_id = '"
						+ pointId + "'";
			}
		}

		String orderbySql = "";
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		// 达人分
		Integer masterScore = instance.findScoreByKey("masterScore");
		if (orderBy.equals("time")) {// 时间排序
			if (filter.equals("master")) {// 达人
				if (sql.indexOf("where") != -1) {
					orderbySql = " and st.praise_count >= "+masterScore;
				} else {
					orderbySql = " st.praise_count >= "+masterScore;
				}
			} else if (filter.equals("general")) {// 普通用户
				if (sql.indexOf("where") != -1) {
					orderbySql = " and st.praise_count <= "+masterScore;
				} else {
					orderbySql += " st.praise_count <="+masterScore;
				}
			}
			orderbySql += " order by st.create_time desc";
		} else if (orderBy.equals("score")) {// 认可度(热度)排序
			if (filter.equals("master")) {// 达人
				if (sql.indexOf("where") != -1) {
					orderbySql = " and st.praise_count >= " + masterScore;
				} else {
					orderbySql = " st.praise_count >= " + masterScore;
				}
			} else if (filter.equals("general")) {// 普通用户
				if (sql.indexOf("where") != -1) {
					orderbySql = " and st.praise_count <= " + masterScore;
				} else {
					orderbySql = " st.praise_count <=" + masterScore;
				}
			}
			orderbySql += " order by st.praise_count desc";
		}
		sql += orderbySql;
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), true,
				Student.class);
		return page2;
	}

	public Page findProductByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			String type, Page page) {
		String ordrebySql = "";
		if (StringUtil.isNotEmpty(type)) {
			if (type.equals("score")) {
				ordrebySql = " order by pr.score desc";
			} else if (type.equals("time")) {
				ordrebySql = " order by pr.create_time desc";
			}
		}
		String sql = null;
		if (StringUtil.isEmpty(labelId)) {
			if (StringUtil.isEmpty(schoolId)) {
				if (StringUtil.isEmpty(pointId)) {
					sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) ";
				} else {
					sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) join product_point pp on(pr.product_id = pp.product_id) where pp.point_id = '"
							+ pointId + "'";
				}
			} else if (StringUtil.isEmpty(pointId)) {
				sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) where st.school_id = '"
						+ schoolId + "' ";
			} else {
				sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) join product_point pp on(pr.product_id = pp.product_id) where st.school_id = '"
						+ schoolId + "' and  pp.point_id = '" + pointId + "'";
			}
		} else {
			if (StringUtil.isEmpty(schoolId)) {
				if (StringUtil.isEmpty(pointId)) {
					sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) where pr.label_id = '"
							+ labelId + "'";
				} else {
					sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) join product_point pp on(pr.product_id = pp.product_id) where pp.point_id = '"
							+ pointId + "'";
				}
			} else if (StringUtil.isEmpty(pointId)) {
				sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) where st.school_id = '"
						+ schoolId + "' and pr.label_id = '" + labelId + "'";
			} else {
				sql = "select pr.* from product pr join student st on(pr.student_id = st.student_id) join product_point pp on(pr.product_id = pp.product_id) where st.school_id = '"
						+ schoolId
						+ "' and pr.label_id = '"
						+ labelId
						+ "' and pp.point_id = '"
						+ pointId + "'";
			}
		}

		sql += ordrebySql;
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), true,
				Product.class);
		return page2;
	}

	public Label findLabelByLabelId(String labelId) {
		if (labelId == null) {
			return null;
		}
		Label label = (Label) baseDao.findEntity(Label.class, labelId);
		return label;
	}

	public Point findPointByPointId(String pointId) {
		if (pointId == null) {
			return null;
		}
		Point point = (Point) baseDao.findEntity(Point.class, pointId);
		return point;
	}

	public List findSchoolsByLabelIdAndPointId(String labelId, String pointId, Page page) {
		String sql = null;
		if (StringUtil.isEmpty(pointId)) {
			sql = "select sc.school_id schoolId,sc.name schoolName,sc.badge badge from school sc join school_label sl on(sl.school_id = sc.school_id) where sl.label_id = '"
					+ labelId + "'";
		} else {
			sql = "select sc.school_id schoolId,sc.name schoolName,sc.badge badge from school sc join school_label sl on(sl.school_id = sc.school_id) "
					+ "join  school_point sp on(sc.school_id = sp.school_id) where sp.point_id = '"
					+ pointId + "' and sl.label_id = '" + labelId + "'";
		}
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2.getResult();
	}

	public List findLabelsBySchoolId(String schoolId, Page page) {
		String sql = "select la.label_id labelId,la.label_name labelName,sl.count studentCount,"
				+ "sl.product_count productCount,la2.label_id pLabelId,la2.label_name pLabelName "
				+ " from label la join school_label sl on(la.label_id = sl.label_id) join label la2 on(la.pid = la2.label_id) where sl.school_id = '"
				+ schoolId + "'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2.getResult();
	}

	public List findProductsBySchoolIdAndLabelId(String schoolId, String labelId) {
		String sql = null;
		if (StringUtil.isEmpty(schoolId)) {
			sql = "select p.content backImaS from product p where p.label_id = '" + labelId
					+ "' order by p.score desc";
		} else
			sql = "select p.content backImaS from product p join student st on(p.student_id = st.student_id) where p.label_id = '"
					+ labelId + "' and st.school_id ='" + schoolId + "' order by p.score desc";
		Page page2 = baseDao.findPageBySql(sql, 0, 4, false, new String[] {});
		return page2.getResult();
	}

	public List findlabelByLabelIdAndSchoolId(String schoolId, String labelId) {
		String sql = "select la.label_id labelId,la.label_name labelName,sl.school_id schoolId from school_label sl join label la on(sl.label_id = la.label_id) where sl.school_id = '"
				+ schoolId + "' and la.label_id = '" + labelId + "'";
		List list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	public List findLabelsF() {
		String sql = "select la.label_id labelId,la.label_name labelName from label la where la.pid is null";
		List list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	public List findHotReplyPointsByLabelId(String labelId, Page page, HttpServletRequest request) {
		String hql = "from ReplyPoint rp where rp.labelId = '" + labelId + "'";
		Page page2 = baseDao.findPageByHql(hql, page.getPageNo(), page.getPageSize(), false);
		return page2.getResult();
	}

	/**
	 * 查询标签下的推荐标题
	 * 
	 * @param labelId
	 * @return
	 */
	public List findRecommendReplyPoint(String labelId) {
		String hql = "from ReplyPoint rp where rp.labelId = '" + labelId + "' order by rp.hot desc";
		Page page = baseDao.findPageByHql(hql, 1, 50, false);
		return page.getResult();
	}

}
