package com.eeesns.tshow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.LabelDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.dao.ProductDao;
import com.eeesns.tshow.dao.StudentDao;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.NotationReply;
import com.eeesns.tshow.entity.NotationReplyReply;
import com.eeesns.tshow.entity.Point;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.ProductVersion;
import com.eeesns.tshow.entity.ReplyPoint;
import com.eeesns.tshow.entity.SchoolLabel;
import com.eeesns.tshow.entity.SchoolLabelKey;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.entity.StudentCollectLabel;
import com.eeesns.tshow.entity.StudentLabel;
import com.eeesns.tshow.entity.StudentSuggestionLabel;
import com.eeesns.tshow.entity.TitleReply;
import com.eeesns.tshow.entity.Vote;
import com.eeesns.tshow.util.CommonDataUtil;
import com.eeesns.tshow.util.CommonUtil;
import com.eeesns.tshow.util.DateUtil;
import com.eeesns.tshow.util.UUUID;
import com.eeesns.tshow.util.ZhengzeUtil;

@Service
public class ProductService {
	@Resource
	private BaseDao baseDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private StudentDao studentDao;
	@Resource
	private StudentService studentService;
	@Resource
	private SchoolService schoolService;
	@Resource
	private LabelService labelService;
	@Resource
	private LabelDao labelDao;

	public Object saveProduct(Product pro, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Session session = baseDao.getSession();
		String studentId = student.getStudentId();
		Student stu = (Student) baseDao.findEntity(Student.class, studentId);
		stu.setStudentId(studentId);
		// 压缩图片处理
		String path = pro.getContent();
		int pointIndex = path.lastIndexOf(".");
		String smallContent = path.substring(0, pointIndex) + "_s1" + path.substring(pointIndex);
		CommonUtil.imgScaleRequest(path, smallContent, 500, request);
		pro.setSmallContent(smallContent);
		// 判断是否是第一版(第一版前台是不传productSeriesId参数的)
		if (pro.getProductSeriesId() == null) {
			pro.setProductSeriesId(pro.getProductId());
		}
		// 判断更新用户student_label表
		int flag = -1;
		if ((stu.getStudentLabels() != null)) {// Studentlabel和LabelhashCodef方法一致
			for (int i = 0; i < stu.getStudentLabels().size(); i++) {
				if (stu.getStudentLabels().get(i).getLabel().equals(pro.getLabel())) {
					flag = i;
				}
			}
			if (flag != -1) {
				stu.getStudentLabels().get(flag)
						.setProductCount(stu.getStudentLabels().get(flag).getProductCount() + 1);
				stu.getStudentLabels().get(flag).setBackgroundImage(pro.getSmallContent());
				// 更新学校标签的作品数
				schoolService.updateLabelCountBySchoolLabel(student.getSchool().getSchoolId(), pro
						.getLabel().getLabelId());
			} else {
				StudentLabel studentLabel = new StudentLabel();
				studentLabel.setLabel(pro.getLabel());
				studentLabel.setStudentId(stu.getStudentId());
				studentLabel.setBackgroundImage(pro.getSmallContent());
				baseDao.saveEntity(studentLabel);
				// 判断保存学校标签的关系，判断更新学校对应的标签数，更新标签的总人数，更新学校标签总人数
				saveLabelCount(student, pro);
			}
		} else {// 更新student_label表product_count字段
			StudentLabel studentLabel = new StudentLabel();
			studentLabel.setLabel(pro.getLabel());
			studentLabel.setStudentId(stu.getStudentId());
			studentLabel.setBackgroundImage(pro.getSmallContent());
			baseDao.saveEntity(studentLabel);
			// 保存学校标签的关系，判断更新学校对应的标签数，更新标签的总人数
			saveLabelCount(student, pro);
		}
		// 更新标签的总作品数
		updateProductCount(student, pro);
		// 更新此用户总作品数
		String sql2 = "update student set product_count = product_count+1 where student_id = '"
				+ student.getStudentId() + "'";
		session.createSQLQuery(sql2).executeUpdate();
		pro.setStudent(stu);
		List<Point> points = pro.getPoints();
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			Point point2 = labelDao.findPointIdByName(p.getPointName(), p.getLabelId());
			if (point2 == null) {
				p.setBackImgB(pro.getSmallContent());
				p.setBackImgS(pro.getSmallContent());
			} else {
				point2.setHot(point2.getHot() + 1);
				points.set(i, point2);
			}
		}
		baseDao.saveEntity(pro);
		// 发表作品
		String actType = "F";
		String objType = "0";
		String objId = pro.getProductId();
		String actId = pro.getProductId();
		studentService.saveAct(studentId, actType, objType, objId, actId, pro.getProductId(),
				studentId);
		return editJson;
	}

	/**
	 * 更新标签的总作品数
	 * 
	 * @param student
	 * @param pro
	 */
	private void updateProductCount(Student student, Product pro) {
		labelService.updateProductCount(pro.getLabel().getLabelId());

	}

	/**
	 * 判断保存学校标签的关系，判断更新学校对应的标签数，更新标签的总人数，更新学校标签总人数
	 * 
	 * @param student
	 * @param pro
	 */
	private void saveLabelCount(Student student, Product pro) {
		SchoolLabelKey slk = new SchoolLabelKey();
		slk.setLabelId(pro.getLabel().getLabelId());
		slk.setSchoolId(student.getSchool().getSchoolId());
		SchoolLabel entity = (SchoolLabel) baseDao.findEntity(SchoolLabel.class, slk);
		if (entity == null) {
			String schoolId = student.getSchool().getSchoolId();
			String labelId = pro.getLabel().getLabelId();
			SchoolLabel sl = new SchoolLabel();
			sl.setLabelId(labelId);
			sl.setSchoolId(schoolId);
			baseDao.saveEntity(sl);
			schoolService.updateLabelCount(schoolId);
		} else {
			entity.setCount(entity.getCount() + 1);
			baseDao.saveOrUpdateEntity(entity);
		}
		labelService.updateStudentCount(pro.getLabel().getLabelId());
	}

	public Object updateProduct(Product pro, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		EditJson editJson = new EditJson();
		String productSeriesId = pro.getProductSeriesId();
		if (productSeriesId == null) {
			editJson.setSuccess(false);
			editJson.setMessage("前台参数错误");
			return editJson;
		}
		// 安全验证，此作品是否是当前用户的作品
		Product oldProduct = productDao.findProductByProductSeriesId(productSeriesId);
		if (oldProduct != null
				&& !oldProduct.getStudent().getStudentId().equals(student.getStudentId())) {
			editJson.setSuccess(false);
			editJson.setMessage("您无此作品");
			return editJson;
		}
		pro.setProductId(UUUID.getNextIntValue());
		pro.setCopyright(oldProduct.getCopyright());// 版权
		pro.setLabel(oldProduct.getLabel());// 标签
		pro.setOriginal(oldProduct.getOriginal());// 原创or转载
		List points = new ArrayList();
		for (Point po : oldProduct.getPoints()) {
			try {
				// Point newPo = (Point) po.clone();
				points.add(po);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		pro.setPoints(points);// 内容元素
		pro.setPortfolio(oldProduct.getPortfolio());// 作品集
		pro.setProductName(oldProduct.getProductName());// 作品名称
		pro.setType(oldProduct.getType());// 作品类型
		pro.setStudent(oldProduct.getStudent());// 作者
		pro.setVersion(oldProduct.getVersion() + 1);// 版本号
		pro.setVersionCount(oldProduct.getVersionCount() + 1);// 总版本数
		pro.setProductSeriesId(oldProduct.getProductSeriesId());
		pro.setScore(pro.getScore());
		// 将旧版本的作品存入product_version中
		productDao.saveProductVersion(productSeriesId);
		// 将旧版本作品从product_versio中删除
		productDao.deleteProductByProductId(productSeriesId);
		// 将新作品存入product中
		pro.setStudent(student);
		productDao.saveNewVersionProduct(pro);
		String sql = "select ps.student_id studentId from product_student ps where ps.product_id = '"
				+ oldProduct.getProductId() + "'";
		List<Map> students = baseDao.findListBySql(sql, new String[] {});
		for (Map m : students) {
			String studentId = (String) m.get("studentId");
			String productStudentSql = "insert into product_student(product_student_id,product_id,student_id,type) values(?,?,?,?)";
			SQLQuery sqlQuery = baseDao.getSession().createSQLQuery(productStudentSql);
			sqlQuery.setString(0, UUUID.getNextIntValue());
			sqlQuery.setString(1, pro.getProductId());
			sqlQuery.setString(2, studentId);
			sqlQuery.setString(3, "CU");
			sqlQuery.executeUpdate();
		}
		// 更新历史版本的总版本数量
		String upSql = "update product_version set version_count = " + pro.getVersionCount()
				+ " where product_series_id = '" + pro.getProductSeriesId() + "'";
		baseDao.executeUpdateBySql(upSql);
		editJson.setBean(pro);
		return editJson;
	}

	public Product findProduct(String productId) {
		Product pro = (Product) baseDao.findEntity(Product.class, productId);
		return pro;
	}

	/**
	 * 保存点的一级回复
	 * 
	 * @param notationReply
	 * @param request
	 */
	public Object saveNotationReply(NotationReply notationReply, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		if (!safeJcCheck(notationReply.getNotationContent())) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("存在非法字符");
			return editJson;
		}
		/* 待修改，从session中获取 */
		Session session = baseDao.getSession();
		Student student = (Student) request.getSession().getAttribute("student");
		notationReply.setReplyStudent(student);
		/* 更新当前作品的最后一次活动时间 */
		String titleReplyId = notationReply.getTitleReplyId();
		String sql = "select tr.product_id productId from title_reply tr where tr.title_reply_id ='"
				+ titleReplyId + "'";
		Map map = baseDao.findMapBySql(sql, new String[] { "productId" });
		String productId = (String) map.get("productId");
		Product product = (Product) baseDao.findEntity(Product.class, productId);
		if (product == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("历史版本不允许在评论了");
			return editJson;
		}
		product.setLastActTime(DateUtil.formatDate());
		product.setReplyCount(product.getReplyCount() + 1);
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		System.out.println(product.getScore());
		System.out.println(instance.findScoreByKey("reply"));
		System.out.println(product.getScore() + instance.findScoreByKey("reply"));
		product.setScore(product.getScore() + instance.findScoreByKey("reply"));
		session.update(product);
		baseDao.saveEntity(notationReply);
		// 同步更新此用户的总参与（意见）数
		String sql3 = "update student set suggestion_count = suggestion_count+1 where student_id = '"
				+ student.getStudentId() + "'";
		session.createSQLQuery(sql3).executeUpdate();
		// 记录此次活动(回复一级评论)
		String studentId = student.getStudentId();
		String actType = "P";
		String objType = "0";
		String actId = notationReply.getNotationReplyId();
		studentService.saveAct(studentId, actType, objType, productId, actId, productId, product
				.getStudent().getStudentId());
		// 同步此表student_suggestion_label
		Student student2 = (Student) session.load(Student.class, student.getStudentId());
		List<StudentSuggestionLabel> studentSuggestionLabels = student2
				.getStudentSuggestionLabels();
		int flag = -1;
		for (int i = 0; i < studentSuggestionLabels.size(); i++) {
			if (studentSuggestionLabels.get(i).getLabel().getLabelId()
					.equals(product.getLabel().getLabelId())) {
				flag = i;
			}
		}
		if (flag != -1) {
			String sql2 = "update student_suggestion_label set product_count = product_count+1 "
					+ "where student_id ='" + student.getStudentId() + "' and label_id ='"
					+ product.getLabel().getLabelId() + "'";
			session.createSQLQuery(sql2).executeUpdate();
		} else {
			StudentSuggestionLabel ssl = new StudentSuggestionLabel();
			ssl.setLabel(product.getLabel());
			ssl.setStudentId(student.getStudentId());
			ssl.setBackgroundImage(product.getContent());
			session.save(ssl);
		}
		editJson.setBean(notationReply);
		return editJson;
	}

	public Object saveNotationReplyReply(NotationReplyReply notationReplyReply,
			HttpServletRequest request) {
		EditJson editJson = new EditJson();
		if (!safeJcCheck(notationReplyReply.getReplyContent())) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("存在非法字符");
			return editJson;
		}
		Student student = (Student) request.getSession().getAttribute("student");
		Session session = baseDao.getSession();
		String replyId = notationReplyReply.getNotationReplyId();
		String sql = "select tr.product_id productId from  notation_reply nr join title_reply tr on(tr.title_reply_id = nr.title_reply_id) where nr.notation_reply_id = '"
				+ replyId + "'";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		String toProductId = (String) map.get("productId");
		Product product = (Product) baseDao.findEntity(Product.class, toProductId);
		if (product == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("历史版本不允许在评论了");
			return editJson;
		}
		Student s = (Student) session.get(Student.class, student.getStudentId());
		notationReplyReply.setReplyStudent(s);
		/*
		 * Student ts = (Student) session.get(Student.class, notationReplyReply
		 * .getToreplyStudent().getStudentId());
		 */
		Student ts = new Student();
		ts.setStudentId(notationReplyReply.getToreplyStudent().getStudentId());
		notationReplyReply.setToreplyStudent(ts);
		/**/
		baseDao.saveEntity(notationReplyReply);
		// 记录此次活动(回复二级评论)
		String studentId = student.getStudentId();
		String actType = "P";
		String objType = null;
		if (notationReplyReply.getToNotationReplyReplyId() == null) {
			objType = "1";
			String toStudentId = notationReplyReply.getToreplyStudent().getStudentId();
			String actId = notationReplyReply.getNotationReplyReplyId();
			studentService.saveAct(studentId, actType, objType,
					notationReplyReply.getNotationReplyId(),
					notationReplyReply.getNotationReplyReplyId(), toProductId, toStudentId);
		} else {
			objType = "2";
			String toStudentId = notationReplyReply.getToreplyStudent().getStudentId();
			String actId = notationReplyReply.getNotationReplyReplyId();
			studentService
					.saveAct(studentId, actType, objType,
							notationReplyReply.getToNotationReplyReplyId(), actId, toProductId,
							toStudentId);
		}
		// 作品热度更新
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		productDao.updateProductHot(toProductId, instance.findScoreByKey("replyreply"));
		editJson.setBean(notationReplyReply);
		return editJson;
	}

	public EditJson findProductPage(Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Page p = baseDao.findPageByHql("from Product", page.getPageNo(), page.getPageSize(), true);
		if (request.getSession().getAttribute("student") != null) {
			String studentId = request.getSession().getAttribute("student") != null ? ((Student) request
					.getSession().getAttribute("student")).getStudentId() : "";
			List<Product> list = p.getResult();
			if (!studentId.equals("")) {
				for (Product pro : list) {
					String productId = pro.getProductId();
					List praises = baseDao.findListByHql("from Praise p where p.productId = '"
							+ productId + "' and p.studentId = '" + studentId + "'");
					pro.setPraises(praises);
					List votes = baseDao.findListByHql("from Vote v where v.productId = '"
							+ productId + "' and v.studentId = '" + studentId + "'");
					pro.setVotes(votes);
					List collections = baseDao
							.findListByHql("from Collection c where c.productId = '" + productId
									+ "' and c.studentId = '" + studentId + "'");
					pro.setCollections(collections);
				}
			}
		}
		editJson.getMap().put("list", p.getResult());
		return editJson;
	}

	public Object findTtileReply(String productId, HttpServletRequest request) {
		List<TitleReply> list = productDao.findTtileReply(productId);
		// 查询每个点的热度参数
		for (TitleReply tr : list) {
			Map map = productDao.findHotParamsByTitleReply(tr.getTitleReplyId(), tr.getProductId());
			tr.setHotParams(map);
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig
				.setExcludes(new String[] { "notationReplys", "studentLabels",
						"studentSuggestionLabels", "studentCollectLabels", "school", "profession",
						"role" });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
		EditJson editJson = new EditJson();
		editJson.getMap().put("list", jsonObject.toString());
		return editJson;
	}

	public String savepraiseNotationReplyReply(String replyReplyId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		String state = productDao.savepraiseNotationReplyReply(replyReplyId, student);
		if (!state.equals("success")) {
			return state;
		}
		// 记录此次活动(点赞二级评论)
		String studentId = student.getStudentId();
		String actType = "Z";
		String objType = "2";
		Session session = baseDao.getSession();
		String sql = "select tr.product_id productId,nrr.reply_student_id replyStudentId from title_reply tr join notation_reply nr on(tr.title_reply_id = nr.title_reply_id)"
				+ " join notation_reply_reply nrr on(nr.notation_reply_id = nrr.notation_reply_id) "
				+ " where nrr.notation_reply_reply_id = '" + replyReplyId + "'";
		Map map = baseDao.findMapBySql(sql, new String[] {});
		String toProductId = (String) map.get("productId");
		String toStudentId = (String) map.get("replyStudentId");
		studentService.saveAct(studentId, actType, objType, replyReplyId, null, toProductId,
				toStudentId);
		// 作品热度更新
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		productDao.updateProductHot(toProductId, instance.findScoreByKey("praise"));
		return state;
	}

	public Object findNotationReplyReply(String notationReplyId, HttpServletRequest request) {
		String studentId = request.getSession().getAttribute("student") != null ? ((Student) request
				.getSession().getAttribute("student")).getStudentId() : "";
		List<NotationReplyReply> list = productDao.findNotationReplyReply(notationReplyId);
		if (!studentId.equals("")) {
			for (NotationReplyReply nrr : list) {
				List praises2 = baseDao
						.findListByHql("from Praise p where p.productId = '"
								+ nrr.getNotationReplyReplyId() + "' and p.studentId = '"
								+ studentId + "'");
				nrr.setMyPraiseCount(praises2.size());
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig
				.setExcludes(new String[] { "schoolLabels", "labels", "studentLabels",
						"studentSuggestionLabels", "studentCollectLabels", "school", "profession",
						"role" });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
		EditJson editJson = new EditJson();
		editJson.getMap().put("list", jsonObject.toString());
		return editJson;
	}

	public String deletePraiseNotationReplyReply(String replyReplyId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		return productDao.deletePraiseNotationReplyReply(replyReplyId, student);
	}

	public String savepraiseNotationReply(String replyId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		String state = productDao.savepraiseNotationReply(replyId, student);
		if (!state.equals("success")) {
			return state;
		}
		// 记录此次活动(点赞一级评论)
		String studentId = student.getStudentId();
		String actType = "Z";
		String objType = "1";
		String sql = "select tr.product_id productId,nr.reply_student_id replyStudentId from title_reply tr join "
				+ " notation_reply nr on(tr.title_reply_id = nr.title_reply_id)"
				+ " where nr.notation_reply_id = '" + replyId + "'";
		Map map = baseDao.findMapBySql(sql, new String[] {});
		String toProductId = (String) map.get("productId");
		String toStudentId = (String) map.get("replyStudentId");
		// 作品热度更新
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		productDao.updateProductHot(toProductId, instance.findScoreByKey("userful"));
		studentService
				.saveAct(studentId, actType, objType, replyId, null, toProductId, toStudentId);
		return state;
	}

	public String deletePraiseNotationReply(String replyId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		return productDao.deletePraiseNotationReply(replyId, student);
	}

	public String savePraiseProduct(String productId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		String state = productDao.savePraiseProduct(productId, student);
		if (!state.equals("success")) {
			return state;
		}
		// 记录此次活动(点赞作品)
		String studentId = student.getStudentId();
		String actType = "Z";
		String objType = "0";
		Session session = baseDao.getSession();
		Product product = (Product) session.get(Product.class, productId);
		if (product == null) {
			ProductVersion pv = (ProductVersion) session.get(ProductVersion.class, productId);
			product = productDao.findProductBySeriesId(pv.getProductSeriesId());
			pv.setPraiseCount(pv.getPraiseCount() + 1);
			studentService.saveAct(studentId, actType, objType, productId, null, productId, pv
					.getStudent().getStudentId());
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			productDao.updateProductVersionHot(productId, instance.findScoreByKey("love"));
		} else {
			product.setPraiseCount(product.getPraiseCount() + 1);
			studentService.saveAct(studentId, actType, objType, productId, null, productId, product
					.getStudent().getStudentId());
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			productDao.updateProductHot(productId, instance.findScoreByKey("love"));
		}
		productDao.updateProductLastActTime(session, product);
		return state;
	}

	public String deletePraiseProduct(String productId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		return productDao.deletePraiseProduct(productId, student);
	}

	public String saveOrDeleteAdopt(String id, String type, String saveOrDelete,
			HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		String state = "前台参数错误";
		String studentId = student.getStudentId();
		if (type.equals("1")) {
			if (saveOrDelete.equals("s")) {
				state = productDao.saveAdopt(id, type, student);
				if (!state.equals("success")) {
					return state;
				}
				// 记录此次活动(采纳一级评论)
				String actType = "C";
				String objType = "1";
				Session session = baseDao.getSession();
				String sql = "select nr.reply_student_id replyStudentId,tr.product_id productId from title_reply tr join notation_reply nr on(tr.title_reply_id = nr.title_reply_id) where nr.notation_reply_id = '"
						+ id + "'";
				Map map = baseDao.findMapBySql(sql, new String[] {});
				String toProductId = (String) map.get("productId");
				String toStudentId = (String) map.get("replyStudentId");
				studentService.saveAct(studentId, actType, objType, id, null, toProductId,
						toStudentId);
			} else if (saveOrDelete.equals("d")) {
				state = productDao.deleteAdopt(id, type, res);
			}
		} else if (type.equals("2")) {
			if (saveOrDelete.equals("s")) {
				state = productDao.saveAdopt(id, type, student);
			} else if (saveOrDelete.equals("d")) {
				state = productDao.deleteAdopt(id, type, res);
			}
		}
		return state;
	}

	public Object deleteReplyReply(NotationReplyReply nrr, HttpServletRequest res) {
		EditJson editJson = new EditJson();
		Student student = (Student) res.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Session session = baseDao.getSession();
		NotationReplyReply notationReplyReply = (NotationReplyReply) session.get(
				NotationReplyReply.class, nrr.getNotationReplyReplyId());
		if (notationReplyReply != null
				&& notationReplyReply.getReplyStudent().getStudentId().equals(studentId)) {// 判断此评论是否是当前用户所发表的,并且还未被采纳
			productDao.deleteReplyReply(notationReplyReply);
		}
		return editJson;
	}

	public Object deleteReply(NotationReply nr, HttpServletRequest res) {
		EditJson editJson = new EditJson();
		Student student = (Student) res.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Session session = baseDao.getSession();
		NotationReply notationReply = (NotationReply) session.get(NotationReply.class,
				nr.getNotationReplyId());
		if (notationReply != null
				&& notationReply.getReplyStudent().getStudentId().equals(studentId)
				&& notationReply.getIsadopt().equals("N")) {// 判断此评论是否是当前用户所发表的,并且还未被采纳
			productDao.deleteReply(notationReply);
		} else {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("此评论已经被采纳无法删除");
		}
		return editJson;
	}

	/**
	 * 查询莫一个点的发起者的一级评论和其他回复者的一级评论（不同于根据produc_id查询作品的所有发起者的一级评论）
	 * 
	 * @param replyId
	 * @param stuId
	 * @param res
	 * @return
	 */
	public Object findNotationReply(String replyId, HttpServletRequest res) {
		List<NotationReply> list = productDao.findNotationReply(replyId);
		// if (request.getSession().getAttribute("student") != null) {
		String studentId = res.getSession().getAttribute("student") != null ? ((Student) res
				.getSession().getAttribute("student")).getStudentId() : "";
		if (!studentId.equals("")) {
			for (NotationReply nr : list) {
				String notationReplyId = nr.getNotationReplyId();
				List praises = baseDao.findListByHql("from Praise p where p.productId = '"
						+ notationReplyId + "' and p.studentId = '" + studentId + "'");
				nr.setMyPraiseCount(praises.size());
				/*
				 * for(NotationReplyReply nrr:nr.getNotationReplyReplys()){ List
				 * praises2 = baseDao
				 * .findListByHql("from Praise p where p.productId = '" +
				 * notationReplyId + "' and p.studentId = '" + studentId + "'");
				 * nrr.setMyPraiseCount(praises2.size()); }
				 */
			}
			// }
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "notationReplyReplys", "schoolLabels", "labels",
				"studentLabels", "studentSuggestionLabels", "studentCollectLabels", "school",
				"profession", "role" });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
		EditJson editJson = new EditJson();
		editJson.getMap().put("list", jsonObject.toString());
		return editJson;
	}

	public Object findProductVersion(String productSeriesId, Integer version,
			HttpServletRequest request) {

		EditJson editJson = new EditJson();
		try {
			ProductVersion obj = (ProductVersion) productDao.findProductVersion(productSeriesId,
					version);
			if (obj == null) {
				editJson.setSuccess(false);
				editJson.getMess().setMessage("无此版本作品");
				return editJson;
			}
			if (request.getSession().getAttribute("student") != null) {
				String studentId = ((Student) request.getSession().getAttribute("student"))
						.getStudentId();
				String productId = obj.getProductId();
				List praises = baseDao.findListByHql("from Praise p where p.productId = '"
						+ productId + "' and p.studentId = '" + studentId + "'");
				obj.setPraises(praises);
				List votes = baseDao.findListByHql("from Vote v where v.productId = '" + productId
						+ "' and v.studentId = '" + studentId + "'");
				obj.setVotes(votes);
				List collections = baseDao.findListByHql("from Collection c where c.productId = '"
						+ productId + "' and c.studentId = '" + studentId + "'");
				obj.setCollections(collections);
				List reports = baseDao.findListByHql("from Report r where r.productId = '"
						+ productId + "' and r.studentId = '" + studentId + "'");
				obj.setReports(reports);
			}
			JsonConfig jsonConfig = new JsonConfig();
			// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
			jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys",
					"interest" });
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(obj, jsonConfig);
			String productVersion = jsonObject.toString();
			editJson.getMap().put("productVersion", productVersion);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("版本作品查询异常");
		}
		return editJson;

	}

	public Object saveTitleReply(TitleReply titleReply, String notationContent,
			HttpServletRequest request) {
		EditJson editJson = new EditJson();
		// 安全监测
		if (!safeJcCheck(titleReply.getTitleName() + notationContent)) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("存在非法字符");
			return editJson;
		}
		Session session = baseDao.getSession();
		NotationReply notationReply = new NotationReply();
		notationReply.setTitleReplyId(titleReply.getTitleReplyId());
		Student student = (Student) request.getSession().getAttribute("student");
		titleReply.setReplyStudent(student);
		baseDao.saveEntity(titleReply);
		notationReply.setReplyStudent(student);
		notationReply.setNotationContent(notationContent);
		session.save(notationReply);
		// 记录此次活动(一级回复作品)
		String actType = "P";
		String objType = "0";
		Product product = (Product) session.get(Product.class, titleReply.getProductId());
		if (product == null) {
			editJson.setSuccess(false);
			editJson.setMessage("历史版本不允许在评论了");
			return editJson;
		} else {
			studentService.saveAct(student.getStudentId(), actType, objType, titleReply
					.getProductId(), null, titleReply.getProductId(), product.getStudent()
					.getStudentId());
			/* 更新当前作品的最后一次活动时间以及评论数量 */
			String productId = titleReply.getProductId();
			product.setLastActTime(DateUtil.formatDate());
			product.setReplyCount(product.getReplyCount() + 1);
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			product.setScore(product.getScore() + instance.findScoreByKey("point"));
			session.update(product);
			// 保存point（标签的元素拆分）
			String titleName = titleReply.getTitleName();
			// 同步更新此用户的总参与（意见）数
			String sql3 = "update student set suggestion_count = suggestion_count+1 where student_id = '"
					+ student.getStudentId() + "'";
			session.createSQLQuery(sql3).executeUpdate();
			// 同步此表student_suggestion_label
			Student student2 = (Student) session.load(Student.class, student.getStudentId());
			List<StudentSuggestionLabel> studentSuggestionLabels = student2
					.getStudentSuggestionLabels();
			int flag = -1;
			for (int i = 0; i < studentSuggestionLabels.size(); i++) {
				if (studentSuggestionLabels.get(i).getLabel().getLabelId()
						.equals(product.getLabel().getLabelId())) {
					flag = i;
				}
			}
			if (flag != -1) {
				String sql2 = "update student_suggestion_label set product_count = product_count+1 "
						+ "where student_id ='"
						+ student.getStudentId()
						+ "' and label_id ='"
						+ product.getLabel().getLabelId() + "'";
				session.createSQLQuery(sql2).executeUpdate();
			} else {
				StudentSuggestionLabel ssl = new StudentSuggestionLabel();
				ssl.setLabel(product.getLabel());
				ssl.setStudentId(student.getStudentId());
				ssl.setBackgroundImage(product.getContent());
				session.save(ssl);
			}
			String sql = "select p.point_id pointId from reply_point p where p.point_name ='"
					+ titleName + "' and p.label_id ='" + product.getLabel().getLabelId()
					+ "' and p.type = 'T'";
			List list = session.createSQLQuery(sql).list();
			if (list.size() > 0) {
				updatePointHot((String) list.get(0));
			} else {
				ReplyPoint point = new ReplyPoint();
				point.setLabelId(product.getLabel().getLabelId());
				point.setPointName(titleName);
				point.setType("T");
				session.save(point);
			}
		}
		editJson.setBean(titleReply);
		return editJson;
	}

	private boolean safeJcCheck(String titleName) {
		if (titleName.toLowerCase().contains("<script")) {
			return false;
		}
		return true;
	}

	private void updatePointHot(String pointId) {
		productDao.updatePointHot(pointId);

	}

	public String saveCollectionProduct(String productId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		// 判断此作品是否已经被其收藏
		String studentId = student.getStudentId();
		String sql = "select * from collection c where c.product_id = ? and c.student_id = ?";
		Session session = baseDao.getSession();
		Query query = session.createSQLQuery(sql);
		query.setString(0, productId);
		query.setString(1, studentId);
		List list = query.list();
		if (list.size() > 0) {
			return "already collectioned";
		}
		// 为收藏清单表里插入数据
		String collectionId = UUUID.getNextIntValue();
		// 同步更新此作品的被收藏数量
		Product product = (Product) session.get(Product.class, productId);
		if (product == null) {
			ProductVersion pv = (ProductVersion) session.get(ProductVersion.class, productId);
			pv.setCollectionCount(pv.getCollectionCount() + 1);
			pv.setLastActTime(DateUtil.formatDate());
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			pv.setScore(pv.getScore() + instance.findScoreByKey("collect"));
			baseDao.updateEntity(pv);
			session.update(pv);
			// 同步更新被收藏作品的用户的认可度
			studentDao.updateStudentPraiseCount(pv.getStudent().getStudentId(),
					instance.findScoreByKey("collectStu"));
			// 同步更新此用户的总收藏数
			String sql3 = "update student set collect_count = collect_count+1 where student_id = '"
					+ student.getStudentId() + "'";
			session.createSQLQuery(sql3).executeUpdate();
			// 记录此次活动(收藏作品)
			String actType = "S";
			String objType = "0";
			studentService.saveAct(studentId, actType, objType, productId, null, productId, pv
					.getStudent().getStudentId());
			// 收藏分类 StudentCollectLabel
			String labelId = pv.getLabel().getLabelId();
			Student student2 = (Student) session.load(Student.class, student.getStudentId());
			List<StudentCollectLabel> studentCollectLabels = student2.getStudentCollectLabels();
			int flag = -1;
			for (int i = 0; i < studentCollectLabels.size(); i++) {
				if (studentCollectLabels.get(i).getLabel().getLabelId().equals(labelId)) {
					flag = i;
				}
			}
			if (flag != -1) {
				String sql2 = "update student_collect_label set product_count = product_count+1 "
						+ "where student_id = '" + student.getStudentId() + "' and label_id = '"
						+ labelId + "'";
				session.createSQLQuery(sql2).executeUpdate();
			} else {
				StudentCollectLabel scl = new StudentCollectLabel();
				scl.setStudentId(student.getStudentId());
				scl.setLabel(pv.getLabel());
				scl.setBackgroundImage(pv.getContent());
				session.save(scl);
			}
		} else {
			product.setCollectionCount(product.getCollectionCount() + 1);
			product.setLastActTime(DateUtil.formatDate());
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			product.setScore(product.getScore() + instance.findScoreByKey("collect"));
			baseDao.updateEntity(product);
			session.update(product);
			// 同步更新被收藏作品的用户的认可度
			studentDao.updateStudentPraiseCount(product.getStudent().getStudentId(),
					instance.findScoreByKey("collectStu"));
			// 同步更新此用户的总收藏数
			String sql3 = "update student set collect_count = collect_count+1 where student_id = '"
					+ student.getStudentId() + "'";
			session.createSQLQuery(sql3).executeUpdate();
			// 记录此次活动(收藏作品)
			String actType = "S";
			String objType = "0";
			studentService.saveAct(studentId, actType, objType, productId, null, productId, product
					.getStudent().getStudentId());
			// 收藏分类 StudentCollectLabel
			String labelId = product.getLabel().getLabelId();
			Student student2 = (Student) session.load(Student.class, student.getStudentId());
			List<StudentCollectLabel> studentCollectLabels = student2.getStudentCollectLabels();
			int flag = -1;
			for (int i = 0; i < studentCollectLabels.size(); i++) {
				if (studentCollectLabels.get(i).getLabel().getLabelId().equals(labelId)) {
					flag = i;
				}
			}
			if (flag != -1) {
				String sql2 = "update student_collect_label set product_count = product_count+1 "
						+ "where student_id = '" + student.getStudentId() + "' and label_id = '"
						+ labelId + "'";
				session.createSQLQuery(sql2).executeUpdate();
			} else {
				StudentCollectLabel scl = new StudentCollectLabel();
				scl.setStudentId(student.getStudentId());
				scl.setLabel(product.getLabel());
				scl.setBackgroundImage(product.getContent());
				session.save(scl);
			}
		}
		baseDao.executeUpdateBySql("insert  into collection(collection_id,student_id,product_id) values('"
				+ collectionId + "','" + studentId + "','" + productId + "')");

		return "success";
	}

	public String deleteCollectionProduct(String productId, HttpServletRequest res) {
		Student student = (Student) res.getSession().getAttribute("student");
		// 判断此作品是否已经被其收藏
		String studentId = student.getStudentId();
		String sql = "select * from  collection c where c.product_id = ? and c.student_id = ?";
		Session session = baseDao.getSession();
		Query query = session.createSQLQuery(sql);
		query.setString(0, productId);
		query.setString(1, studentId);
		List list = query.list();
		if (list.size() == 0) {
			return "never collectioned";
		}
		// 从收藏清单表里删除数据
		String delSql = "delete from collection where product_id=? and student_id=?";
		SQLQuery sqlQuery = session.createSQLQuery(delSql);
		;
		sqlQuery.setString(0, productId);
		sqlQuery.setString(1, studentId);
		sqlQuery.executeUpdate();
		// 同步更新此用户的总收藏数
		String sql3 = "update student set collect_count = collect_count-1 where student_id = '"
				+ student.getStudentId() + "'";
		session.createSQLQuery(sql3).executeUpdate();
		// 同步更新此作品的被收藏数量
		Product product = (Product) session.get(Product.class, productId);
		if (product == null) {
			ProductVersion pv = (ProductVersion) session.get(ProductVersion.class, productId);
			pv.setCollectionCount(pv.getCollectionCount() - 1);
			pv.setLastActTime(DateUtil.formatDate());
			session.update(pv);
			// 收藏分类 StudentCollectLabel
			String labelId = pv.getLabel().getLabelId();
			Student student2 = (Student) session.load(Student.class, student.getStudentId());
			List<StudentCollectLabel> studentCollectLabels = student2.getStudentCollectLabels();
			int flag = -1;
			for (int i = 0; i < studentCollectLabels.size(); i++) {
				if (studentCollectLabels.get(i).getLabel().getLabelId().equals(labelId)) {
					flag = i;
				}
			}
			if (flag != -1) {
				String sql2 = "update student_collect_label set product_count = product_count-1 "
						+ "where student_id = '" + student.getStudentId() + "' and label_id = '"
						+ labelId + "'";
				session.createSQLQuery(sql2).executeUpdate();
			}
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			pv.setScore(pv.getScore() + -instance.findScoreByKey("collect"));
			baseDao.updateEntity(pv);
			// 同步更新被收藏作品的用户的认可度
			studentDao.updateStudentPraiseCount(pv.getStudent().getStudentId(),
					-instance.findScoreByKey("collectStu"));
		} else {
			product.setCollectionCount(product.getCollectionCount() - 1);
			product.setLastActTime(DateUtil.formatDate());
			session.update(product);
			// 收藏分类 StudentCollectLabel
			String labelId = product.getLabel().getLabelId();
			Student student2 = (Student) session.load(Student.class, student.getStudentId());
			List<StudentCollectLabel> studentCollectLabels = student2.getStudentCollectLabels();
			int flag = -1;
			for (int i = 0; i < studentCollectLabels.size(); i++) {
				if (studentCollectLabels.get(i).getLabel().getLabelId().equals(labelId)) {
					flag = i;
				}
			}
			if (flag != -1) {
				String sql2 = "update student_collect_label set product_count = product_count-1 "
						+ "where student_id = '" + student.getStudentId() + "' and label_id = '"
						+ labelId + "'";
				session.createSQLQuery(sql2).executeUpdate();
			}
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			product.setScore(product.getScore() + -instance.findScoreByKey("collect"));
			baseDao.updateEntity(product);
			// 同步更新被收藏作品的用户的认可度
			studentDao.updateStudentPraiseCount(product.getStudent().getStudentId(),
					-instance.findScoreByKey("collectStu"));
		}
		return "success";
	}

	/**
	 * 投票规则 1、一天投票此处为3次 2、每个标签下每天最多能投1次 3、单个作品只能投一次
	 * 
	 * @param productId
	 * @param res
	 * @return
	 */
	public Object saveVoteProduct(String productId, HttpServletRequest res) {
		EditJson editJson = new EditJson();
		Student student = (Student) res.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Session session = baseDao.getSession();
		String sql = "select * from vote v where v.student_id = ? and create_time >= DATE_SUB(NOW(),INTERVAL 1 day)";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		List list = sqlQuery.list();
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		// 每人每天可投票总数
		Integer voteCountApply = instance.findScoreByKey("voteCountApply");
		if (list.size() > voteCountApply) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("您今天的可投票数量已经用完");
			return editJson;
		}
		String sql2 = "select * from vote v where v.student_id =? and v.product_id = ?";
		SQLQuery sqlQuery2 = session.createSQLQuery(sql2);
		sqlQuery2.setString(0, studentId);
		sqlQuery2.setString(1, productId);
		List list2 = sqlQuery2.list();
		if (list2.size() > 0) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("已经投过票啦");
			return editJson;
		}
		Product product = (Product) session.get(Product.class, productId);
		if (product == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("历史版本不能投票啦");
			return editJson;
		}
		String sql3 = "SELECT\n" + "	v.*, p.label_id" + " FROM" + "	vote v"
				+ " JOIN product p ON (v.product_id = p.product_id)" + " WHERE\n"
				+ "	p.label_id = ? and v.student_id = ?"
				+ " AND v.create_time >= DATE_SUB(NOW(), INTERVAL 1 DAY)";
		SQLQuery sqlQuery3 = session.createSQLQuery(sql3);
		sqlQuery3.setString(0, product.getLabel().getLabelId());
		sqlQuery3.setString(1, studentId);
		List list3 = sqlQuery3.list();
		// 同一标签的当天可投票总数
		Integer voteLabelApply = instance.findScoreByKey("voteLabelApply");
		if (list3.size() > voteLabelApply) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("同一标签的作品一天只能" + voteLabelApply + "投次票");
			return editJson;
		}
		// 投票
		Vote vote = new Vote();
		vote.setStudentId(studentId);
		vote.setProductId(productId);
		session.save(vote);
		// 同步更新此作品的被投票数量
		product.setVoteCount(product.getVoteCount() + 1);
		product.setLastActTime(DateUtil.formatDate());
		// 记录此次活动(投票作品)
		String actType = "T";
		String objType = "0";
		studentService.saveAct(studentId, actType, objType, productId, null,
				product.getProductId(), product.getStudent().getStudentId());
		// 作品热度更新
		product.setScore(product.getScore() + instance.findScoreByKey("vote"));
		baseDao.updateEntity(product);
		session.update(product);
		// 同步更新被收藏作品的用户的认可度
		studentDao.updateStudentPraiseCount(product.getStudent().getStudentId(),
				instance.findScoreByKey("voteStu"));
		return editJson;
	}

	public Object findProduct(String productId, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		String studentId = request.getSession().getAttribute("student") != null ? ((Student) request
				.getSession().getAttribute("student")).getStudentId() : "";
		Product obj = productDao.findProduct(productId);
		if (obj == null) {
			ProductVersion obj2 = productDao.findProductVersion(productId);
			if (obj2 == null) {
				editJson.setSuccess(false);
				editJson.getMess().setMessage("此作品已经不存在");
				return editJson;
			}
			if (!studentId.equals("")) {
				try {
					String toStudentId = obj2.getStudent().getStudentId();
					String sql = "select * from attention a where a.student_id = '" + studentId
							+ "' and a.to_student_id = '" + toStudentId + "'";
					List list = baseDao.findListBySql(sql, new String[] {});
					if (list.size() > 0) {// 此用户被当前用户关注
						obj2.getStudent().setIsAttention("Y");
					} else {
						obj2.getStudent().setIsAttention("N");
					}
					// if (request.getSession().getAttribute("student") != null)
					// {
					List praises = baseDao.findListByHql("from Praise p where p.productId = '"
							+ productId + "' and p.studentId = '" + studentId + "'");
					obj2.setPraises(praises);
					List votes = baseDao.findListByHql("from Vote v where v.productId = '"
							+ productId + "' and v.studentId = '" + studentId + "'");
					obj2.setVotes(votes);
					List collections = baseDao
							.findListByHql("from Collection c where c.productId = '" + productId
									+ "' and c.studentId = '" + studentId + "'");
					obj2.setCollections(collections);
					List reports = baseDao.findListByHql("from Report r where r.productId = '"
							+ productId + "' and r.studentId = '" + studentId + "'");
					obj2.setReports(reports);
					// }
					JsonConfig jsonConfig = new JsonConfig();
					// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
					jsonConfig.setExcludes(new String[] { "labels", "schoolLabels",
							"notationReplys", "interest" });
					JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(obj2, jsonConfig);
					String product = jsonObject.toString();
					editJson.getMap().put("product", product);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("版本作品查询异常");
				}
			}
		} else {
			if (!studentId.equals("")) {
				try {
					String toStudentId = obj.getStudent().getStudentId();
					String sql = "select * from attention a where a.student_id = '" + studentId
							+ "' and a.to_student_id = '" + toStudentId + "'";
					List list = baseDao.findListBySql(sql, new String[] {});
					if (list.size() > 0) {// 此用户被当前用户关注
						obj.getStudent().setIsAttention("Y");
					} else {
						obj.getStudent().setIsAttention("N");
					}
					// if (request.getSession().getAttribute("student") != null)
					// {
					List praises = baseDao.findListByHql("from Praise p where p.productId = '"
							+ productId + "' and p.studentId = '" + studentId + "'");
					obj.setPraises(praises);
					List votes = baseDao.findListByHql("from Vote v where v.productId = '"
							+ productId + "' and v.studentId = '" + studentId + "'");
					obj.setVotes(votes);
					List collections = baseDao
							.findListByHql("from Collection c where c.productId = '" + productId
									+ "' and c.studentId = '" + studentId + "'");
					obj.setCollections(collections);
					List reports = baseDao.findListByHql("from Report r where r.productId = '"
							+ productId + "' and r.studentId = '" + studentId + "'");
					obj.setReports(reports);
					// }

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("版本作品查询异常");
				}
			}
			JsonConfig jsonConfig = new JsonConfig();
			// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
			jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys",
					"interest" });
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(obj, jsonConfig);
			String product = jsonObject.toString();
			editJson.getMap().put("product", product);
		}

		return editJson;

	}

	public Object findProductBySeriesId(String productSeriesId, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Product obj = productDao.findProductBySeriesId(productSeriesId);
		String studentId = request.getSession().getAttribute("student") != null ? ((Student) request
				.getSession().getAttribute("student")).getStudentId() : "";
		if (!studentId.equals("")) {
			try {
				String toStudentId = obj.getStudent().getStudentId();
				String sql = "select * from attention a where a.student_id = '" + studentId
						+ "' and a.to_student_id = '" + toStudentId + "'";
				List list = baseDao.findListBySql(sql, new String[] {});
				if (list.size() > 0) {// 此用户被当前用户关注
					obj.getStudent().setIsAttention("Y");
				} else {
					obj.getStudent().setIsAttention("N");
				}
				// if (request.getSession().getAttribute("student") != null) {
				List praises = baseDao.findListByHql("from Praise p where p.productId = '"
						+ obj.getProductId() + "' and p.studentId = '" + studentId + "'");
				obj.setPraises(praises);
				List votes = baseDao.findListByHql("from Vote v where v.productId = '"
						+ obj.getProductId() + "' and v.studentId = '" + studentId + "'");
				obj.setVotes(votes);
				List collections = baseDao.findListByHql("from Collection c where c.productId = '"
						+ obj.getProductId() + "' and c.studentId = '" + studentId + "'");
				obj.setCollections(collections);
				List reports = baseDao.findListByHql("from Report r where r.productId = '"
						+ obj.getProductId() + "' and r.studentId = '" + studentId + "'");
				obj.setReports(reports);
				// }
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("版本作品查询异常");
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys",
				"interest" });
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(obj, jsonConfig);
		String product = jsonObject.toString();
		editJson.getMap().put("product", product);
		return editJson;
	}

	public EditJson deleteTitleReply(TitleReply tr, HttpServletRequest res) {
		EditJson editJson = new EditJson();
		Student student = (Student) res.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Session session = baseDao.getSession();
		TitleReply titleReply = (TitleReply) session.get(TitleReply.class, tr.getTitleReplyId());
		if (!titleReply.getReplyStudent().getStudentId().equals(studentId)) {
			editJson.getMess().setMessage("no role");
			editJson.setSuccess(false);
			return editJson;
		}
		if (titleReply.getNotationReplys().size() > 1) {
			editJson.getMess().setMessage("已经有太多评论不能删除了");
			editJson.setSuccess(false);
			return editJson;
		}
		productDao.deleteTitleReply(titleReply);
		return editJson;
	}

	public Object deleteProduct(Product product2, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		EditJson editJson = new EditJson();
		Product product = productDao.findProduct(product2.getProductId());
		if (product == null) {// 不是最新版
			// 判断是否是旧版本
			ProductVersion pv = productDao.findProductVersion(product2.getProductId());
			if (pv == null) {
				editJson.setSuccess(false);
				editJson.getMess().setMessage("未找到作品");
				return editJson;
			}
			if (pv != null) {// 删除的是历史版本，需要将此版本之后的版本全部删除
				// 安全验证
				if (!student.getStudentId().equals(pv.getStudent().getStudentId())) {
					editJson.setSuccess(false);
					editJson.getMess().setMessage("非法操作");
					return editJson;
				}
				// 删除此系列当前版本号以及之后的所有作品
				productDao.deleteProductByVersionAfter(pv.getProductSeriesId(), pv.getVersion());
			}
			// 迁移
			if (pv.getVersion() > 1) {
				productDao.saveProductFromProductVersion(pv.getProductSeriesId(),
						pv.getVersion() - 1);
				// 更新最新的总版本号
				productDao.updateVersionCount(pv.getProductSeriesId(), pv.getVersion() - 1);
				ProductVersion pvNew = (ProductVersion) productDao.findProductVersion(
						pv.getProductSeriesId(), pv.getVersion() - 1);
				JSONObject.fromObject(pvNew).toString();
				editJson.setBean(pvNew);
				baseDao.deleteEntity(pvNew);
			}
			// 同步更新作者的相应二级标签的作品数量
			if (pv.getVersion().equals(1)) {
				student = (Student) baseDao.findEntity(Student.class, student.getStudentId());
				student.setProductCount(student.getProductCount() - 1);
				pv.getLabel().setProductCount(pv.getLabel().getProductCount() - 1);
				SchoolLabelKey slk = new SchoolLabelKey();
				slk.setLabelId(pv.getLabel().getLabelId());
				slk.setSchoolId(student.getSchool().getSchoolId());
				SchoolLabel entity = (SchoolLabel) baseDao.findEntity(SchoolLabel.class, slk);
				entity.setCount(entity.getCount() - 1);
				StudentLabel sl = new StudentLabel();
				sl.setProductCount(-1);
				sl.setStudentId(student.getStudentId());
				Label label = new Label();
				label.setLabelId(pv.getLabel().getLabelId());
				sl.setLabel(label);
				studentDao.updateStudentLabelSQL(sl);
				baseDao.updateEntity(student);
				baseDao.updateEntity(pv.getLabel());
				baseDao.updateEntity(entity);
				JSONObject.fromObject(student).toString();
				request.getSession().setAttribute("student", student);
			}
		} else {// 是最新版
			// 安全验证
			if (!student.getStudentId().equals(product.getStudent().getStudentId())) {
				editJson.setSuccess(false);
				editJson.getMess().setMessage("非法操作");
				return editJson;
			}
			// 查询是否有历史版本，若有则将历史版本的最后一版迁过来
			ProductVersion pv = productDao.findProductVersionByProductSeriesId(product
					.getProductSeriesId());
			if (pv != null) {
				int versionNew = pv.getVersion();
				// 迁移
				productDao.saveProductFromProductVersion(pv.getProductId());
				// 删除老版本
				baseDao.deleteEntity(pv);
				pv.setVersionCount(versionNew);
				JSONObject.fromObject(pv).toString();
				editJson.setBean(pv);
				// 更新最新的总版本号
				productDao.updateVersionCount(pv.getProductSeriesId(), versionNew);
			}
			// 删除当前的最新版本
			productDao.deleteProduct(product);
			// 同步更新作者的相应二级标签的作品数量
			if (product.getVersion().equals(1)) {
				student = (Student) baseDao.findEntity(Student.class, student.getStudentId());
				student.setProductCount(student.getProductCount() - 1);
				product.getLabel().setProductCount(product.getLabel().getProductCount() - 1);
				SchoolLabelKey slk = new SchoolLabelKey();
				slk.setLabelId(product.getLabel().getLabelId());
				slk.setSchoolId(student.getSchool().getSchoolId());
				SchoolLabel entity = (SchoolLabel) baseDao.findEntity(SchoolLabel.class, slk);
				entity.setCount(entity.getCount() - 1);
				StudentLabel sl = new StudentLabel();
				sl.setProductCount(-1);
				sl.setStudentId(student.getStudentId());
				Label label = new Label();
				label.setLabelId(product.getLabel().getLabelId());
				sl.setLabel(label);
				studentDao.updateStudentLabelSQL(sl);
				baseDao.updateEntity(student);
				baseDao.updateEntity(product.getLabel());
				baseDao.updateEntity(entity);
				JSONObject.fromObject(student).toString();
				request.getSession().setAttribute("student", student);
			}
		}
		return editJson;
	}

	public Object findRecommendReplyPoint(String labelId, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List list = labelDao.findRecommendReplyPoint(labelId);
		editJson.setBean(list);
		return editJson;
	}

}
