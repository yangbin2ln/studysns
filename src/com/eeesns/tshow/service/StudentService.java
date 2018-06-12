package com.eeesns.tshow.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.common.json.Message;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.dao.StudentDao;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.SchoolLabel;
import com.eeesns.tshow.entity.SchoolLabelKey;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.entity.StudentAct;
import com.eeesns.tshow.entity.StudentStudent;
import com.eeesns.tshow.entity.TitleReply;
import com.eeesns.tshow.util.CommonDataUtil;
import com.eeesns.tshow.util.DateUtil;

@Service
public class StudentService {
	@Resource
	private BaseDao baseDao;
	@Resource
	private StudentDao studentDao;

	public void saveStudent(Student stu) {
		baseDao.saveEntity(stu);

	}

	public Iterator findAllStudent() {
		Session session = baseDao.getSession();
		Query query = session.createQuery("from Student");
		Iterator iterate = query.iterate();
		return iterate;
	}

	public Student findStudentById(String studentId) {
		Session session = baseDao.getSession();
		Student stu = (Student) session.get(Student.class, studentId);
		return stu;
	}

	public Product findProductById() {
		Session session = baseDao.getSession();
		Product p = (Product) session.get(Product.class, "14393086616557753770837042446831");
		return p;
	}

	/**
	 * 学生注册时，插入新增的标签
	 * 
	 * @param stu
	 * 
	 */
	public void insertLabel(Student stu) {
		Session session = baseDao.getSession();
		Label p = (Label) session.get(Label.class, stu.getJobLabel().getLabelId());
		if (p == null) {
			session.save(stu.getJobLabel());
		}
		// List<Label> list = stu.getInterest();
		/*
		 * for (Label l : list) { p = (Label) session.get(Label.class,
		 * l.getLabelId()); if (p == null) { session.save(l); } }
		 */
	}

	/**
	 * 学生注册时，根其所选择的学校和标签，更新school_label表
	 * 
	 * @param stu
	 *            问题：当三大标签出现相同的情况时（保证数据库字段默认值和实体类默认值一致
	 *            (实体类保存时实体类的属性值不一定和数据库字段值一致，因为存在一些没有入库的有默认值的字段),
	 *            即第一次保存的持久态会作为第二次查询时使用一级缓存，这样当实体默认值和数据库默认值不一致时，就会出现问题）
	 */
	public void insertSchoolLabel(Student stu) {
		Session session = baseDao.getSession();
		// slk=new SchoolLabelKey();不能共用，否则om.eeesns.tshow.entity.SchoolLabel
		// was altered from com.eeesns.tshow.entity.SchoolLabelKey@194ed6da to
		// com.eeesns.tshow.entity.SchoolLabelKey@23fd2a96; nested exception is
		// org.hibernate.HibernateException: identifier
		SchoolLabelKey slk = null;
		Label p = null;
		SchoolLabel schoolLabel;
		// 专业标签
		/*
		 * p=(Label)session.get(Label.class,stu.getProfessionLabel().getLabelId()
		 * ); slk=new SchoolLabelKey(); slk.setLabelId(p.getLabelId());
		 * slk.setSchoolId(stu.getSchool().getSchoolId()); schoolLabel =
		 * (SchoolLabel) session.get(SchoolLabel.class, slk);
		 * if(schoolLabel==null){//此标签是否第一次被申请 schoolLabel=new SchoolLabel();
		 * schoolLabel.setLabelId(p.getLabelId());
		 * schoolLabel.setSchoolId(stu.getSchool().getSchoolId());
		 * session.save(schoolLabel); }else{
		 * schoolLabel.setCount(schoolLabel.getCount()+1);
		 * if(schoolLabel.getCount()==30){//是否已经达到解锁人数
		 * schoolLabel.setState("Y"); } session.update(schoolLabel); }
		 */
		// 职业标签
		p = (Label) session.get(Label.class, stu.getJobLabel().getLabelId());
		slk = new SchoolLabelKey();
		slk.setLabelId(p.getLabelId());
		slk.setSchoolId(stu.getSchool().getSchoolId());
		schoolLabel = (SchoolLabel) session.get(SchoolLabel.class, slk);
		if (schoolLabel == null) {// 此标签是否第一次被申请
			schoolLabel = new SchoolLabel();
			schoolLabel.setLabelId(p.getLabelId());
			schoolLabel.setSchoolId(stu.getSchool().getSchoolId());
			session.save(schoolLabel);
		} else {
			schoolLabel.setCount(schoolLabel.getCount() + 1);
			if (schoolLabel.getCount() == 30) {// 是否已经达到解锁人数
				schoolLabel.setState("Y");
			}
			session.update(schoolLabel);
		}
		/*
		 * // 兴趣标签 List<Label> list = stu.getInterest(); for (Label l : list) {
		 * p = (Label) session.get(Label.class, l.getLabelId()); slk = new
		 * SchoolLabelKey(); slk.setLabelId(p.getLabelId());
		 * slk.setSchoolId(stu.getSchool().getSchoolId()); schoolLabel =
		 * (SchoolLabel) session.get(SchoolLabel.class, slk); if (schoolLabel ==
		 * null) {// 此标签是否第一次被申请 schoolLabel = new SchoolLabel();
		 * schoolLabel.setLabelId(p.getLabelId());
		 * schoolLabel.setSchoolId(stu.getSchool().getSchoolId());
		 * session.save(schoolLabel); } else {
		 * schoolLabel.setCount(schoolLabel.getCount() + 1); if
		 * (schoolLabel.getCount() == 30) {// 是否已经达到解锁人数
		 * schoolLabel.setState("Y"); } session.update(schoolLabel); }
		 * 
		 * }
		 */

	}

	public Object findLoginingStudent(String studentId) {
		return baseDao.findEntity(Student.class, studentId);
	}

	public Student findStudentByInvitationId(String invitationId) {
		Student stu = studentDao.findStudentByInvitationId(invitationId);
		return stu;
	}

	public Object saveAttention(String toStudentId, HttpServletRequest request) {
		EditJson eidtJson = new EditJson();
		Session session = baseDao.getSession();
		Student student = (Student) request.getSession().getAttribute("student");
		if (student.getStudentId().equals(toStudentId)) {
			eidtJson.setSuccess(false);
			eidtJson.getMess().setMessage("无需关注自己");
			return eidtJson;
		}
		Student toStudent = (Student) baseDao.findEntity(Student.class, toStudentId);
		Object object = studentDao.saveAttention(toStudentId, student);
		// 同步更新被收藏作品的用户的认可度
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		studentDao.updateStudentPraiseCount(toStudentId, instance.findScoreByKey("attentionStu"));
		return eidtJson;
	}

	public Object deleteAttention(String toStudentId, HttpServletRequest request) {
		Message message = new Message();
		Session session = baseDao.getSession();
		Student student = (Student) request.getSession().getAttribute("student");
		if (student.getStudentId().equals(toStudentId)) {
			message.setState("failed");
			message.setMessage("不能关注自己");
			return message;
		}
		Object object = studentDao.deleteAttention(toStudentId, student);
		// 同步更新被收藏作品的用户的认可度
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		studentDao.updateStudentPraiseCount(toStudentId, -instance.findScoreByKey("attentionStu"));
		return object;
	}

	/* 消息 */
	@Transactional(readOnly = false)
	public Object findMessageProductList(Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		// 记录当前用户的当前浏览消息的时间
		if (page.getPageNo() == 1) {
			studentDao.saveLastViewMessage(studentId);
		}
		// 查询当前用户的最后一次浏览消息的时间
		String lastTime = studentDao.findLastSecondViewMessageTime(studentId);
		// 查询当前用户的所有作品(按照last_act_time排序)
		Page page2 = studentDao.findMessageProductList(page, studentId);
		List list = page2.getResult();
		// 查询每一个作品的最新消息数量(评论、收藏、喜爱、投票、建议)
		for (int i = 0; i < list.size(); i++) {
			Map m = (Map) list.get(i);
			String productId = (String) m.get("productId");
			studentDao.findMessageCount(productId, lastTime, m);
		}
		editJson.setBean(page2.getResult());
		return editJson;
	}

	/**
	 * 查询自己作品的消息总数
	 * 
	 * @param request
	 * @return
	 */
	public Object findMessageProductCount(HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		if (student == null) {
			return 0;
		}
		String studentId = student.getStudentId();
		Object object = studentDao.findMessageProductCount(studentId);
		return object;
	}

	/**
	 * 查询好友请求数量
	 * 
	 * @param request
	 * @return
	 */
	public Object findMessageStudentStudentCoun(HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Object object = studentDao.findMessageStudentStudentCoun(studentId);
		return object;
	}

	/**
	 * 查询新增粉丝数量
	 * 
	 * @param request
	 * @return
	 */
	public Object findMessageStudentFollowerCoun(HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Object object = studentDao.findMessageStudentFollowerCoun(studentId);
		return object;
	}

	public Object findMessageProductInfo(String productId, String type, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		List list = studentDao.findMessageProductInfo(productId, type, studentId);
		editJson.setBean(list);
		return editJson;
	}

	/**
	 * 查询自己参与的消息总数(被二级回复，被采纳)
	 * 
	 * @param request
	 * @return
	 */
	public Object findMessageReplyCount(HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		if (student == null) {
			return 0;
		}
		String studentId = student.getStudentId();
		String lastTime = studentDao.findLastFirstViewMessageTime(studentId);
		Object object = studentDao.findMessageReplyCount(request, studentId, lastTime);
		return object;
	}

	/**
	 * 查询用户参与的消息详情（被二级回复）
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	public Object findMessageReplyList(Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Page page2 = studentDao.findMessageReplyList(page, studentId, request);
		String lastTime = studentDao.findLastSecondViewMessageTime(studentId);
		List list = page2.getResult();
		for (int i = 0; i < list.size(); i++) {
			Map m = (Map) list.get(i);
			String productId = (String) m.get("productId");
			// 回复的数量
			int replyCount = studentDao.findMessageReplyCountByProduct(productId, studentId,
					lastTime);
			m.put("newReplyCount", replyCount);
			// 采纳的数量
			int adoptCount = studentDao.findAdoptCount(productId, studentId, lastTime);
			m.put("newAdoptCount", adoptCount);
			// 点赞的数量
			int praiseCount = studentDao.findPraiseCount(productId, studentId, lastTime);
			m.put("newPraiseCount", praiseCount);
			// 有用的数量
			int usefulCount = studentDao.finduUsefulCount(productId, studentId, lastTime);
			m.put("newUsefulCount", usefulCount);
			m.put("newCount", replyCount + adoptCount + praiseCount + usefulCount);
		}
		editJson.setBean(page2.getResult());
		return editJson;
	}

	public Object findMessageReplyInfo(String productId, String type, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Object object = studentDao.findMessageReplyInfo(productId, studentId, type);
		return object;
	}

	/**
	 * 记录用户此次活动
	 * 
	 * @param studentId
	 * @param actType
	 * @param objType
	 * @param objId
	 * @return
	 */
	public Object saveAct(String studentId, String actType, String objType, String objId,
			String actId, String toProductId, String toStudentId) {
		Object object = studentDao.saveAct(studentId, actType, objType, objId, actId, toProductId,
				toStudentId);
		return object;
	}

	public Object saveProduct(Product product, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Session session = baseDao.getSession();
		Student student2 = (Student) session.load(Student.class, studentId);
		product.setStudent(student2);
		studentDao.saveProduct(product);
		return "success";
	}

	public Object findPersonCenter(String type, String labelId, Page page,
			HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		List<Product> list = new ArrayList();
		if (type.equals("all")) {
			list = studentDao.findPersonCenterByAll(student, page);
		} else if (type.equals("attention")) {
			list = studentDao.findPersonCenterByAttent(student, page);
		} else if (type.equals("studentwbm")) {
			list = studentDao.findWbm(student, page);
		} else if (type.equals("studentybm")) {
			list = studentDao.findYbm(student, page);
		} else if (type.equals("studentyby")) {
			list = studentDao.findYby(student, page);
		}
		for (Product pro : list) {
			String productId = pro.getProductId();
			List praises = baseDao.findListByHql("from Praise p where p.productId = '" + productId
					+ "' and p.studentId = '" + studentId + "'");
			pro.setPraises(praises);
			List votes = baseDao.findListByHql("from Vote v where v.productId = '" + productId
					+ "' and v.studentId = '" + studentId + "'");
			pro.setVotes(votes);
			List collections = baseDao.findListByHql("from Collection c where c.productId = '"
					+ productId + "' and c.studentId = '" + studentId + "'");
			pro.setCollections(collections);
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys",
				"interest", "studentLabels", "studentSuggestionLabels", "studentCollectLabels",
				"role" });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
		String products = jsonObject.toString();
		EditJson editJson = new EditJson();
		editJson.getMap().put("products", products);
		return editJson;
	}

	/**
	 * 查询当前用户的动态
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	public Object findAct(Page page, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		List list = studentDao.findAct(page, studentId);
		for (int i = 0; i < list.size(); i++) {
			Map m = (Map) list.get(i);
			String actId = (String) m.get("actId");
			String objId = (String) m.get("objId");
			String actType = (String) m.get("actType");
			String objType = (String) m.get("objType");
			if (actType.equals("Z")) {
				if (objType.equals("0")) {
					studentDao.findPraiseProduct(objId);
				} else if (objType.equals("1")) {
					studentDao.findPraiseReply(objId);
				} else if (objType.equals("2")) {
					studentDao.findPraiseReplyReply(objId);
				}
			} else if (actType.equals("S")) {
				studentDao.findCollectProduct(objId);
			} else if (actType.equals("T")) {
				studentDao.findVoteProduct(objId);
			} else if (actType.equals("C")) {
				studentDao.findAdoptReply(objId);
			} else if (actType.equals("P")) {
				if (objType.equals("0")) {
					studentDao.findPLProduct(objId, actId);
				} else if (objType.equals("1")) {
					studentDao.findPlReply(objId, actId);
				} else if (objType.equals("2")) {
					studentDao.findPLReplyReplyt(objId, actId);
				}
			}
		}
		return list;
	}

	public Object findYjAct(Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		List list = studentDao.findYjAct(page, studentId);
		editJson.setBean(list);
		return editJson;
	}

	public Object findHomeProducts(String studentId, String type, String labelId, Page page,
			HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List<Product> list = null;
		Student student = null;
		JsonConfig jsonConfig = new JsonConfig();
		if (request.getSession().getAttribute("student") != null) {
			student = (Student) request.getSession().getAttribute("student");
		}
		if (type.equals("product")) {
			jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "titleReplys",
					"interest", "pid", "studentLabels", "studentSuggestionLabels",
					"studentCollectLabels" });
			list = studentDao.findHomeProductsByProduct(studentId, type, labelId, page);
		} else if (type.equals("suggestion")) {
			jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys",
					"interest", "pid", "studentLabels", "studentSuggestionLabels",
					"studentCollectLabels" });
			list = studentDao.findHomeProductsBySuggestion(studentId, type, labelId, page);
			for (Product p : list) {
				List ll = new ArrayList();
				for (TitleReply tr : p.getTitleReplys()) {
					List list2 = studentDao.findHomeProductsNotationBySuggestion(
							tr.getTitleReplyId(), studentId);
					if (list2.size() == 0) {
						ll.add(tr);
					} else {
						tr.setNrPersonList(list2);
					}
				}
				p.getTitleReplys().removeAll(ll);
			}
		} else if (type.equals("collect")) {
			jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "titleReplys",
					"interest", "pid", "studentLabels", "studentSuggestionLabels",
					"studentCollectLabels" });
			list = studentDao.findHomeProductsByCollect(studentId, type, labelId, page);
		}
		for (Product pro : list) {
			if (student != null) {
				String productId = pro.getProductId();
				List praises = baseDao.findListByHql("from Praise p where p.productId = '"
						+ productId + "' and p.studentId = '" + student.getStudentId() + "'");
				pro.setPraises(praises);
				List votes = baseDao.findListByHql("from Vote v where v.productId = '" + productId
						+ "' and v.studentId = '" + student.getStudentId() + "'");
				pro.setVotes(votes);
				List collections = baseDao.findListByHql("from Collection c where c.productId = '"
						+ productId + "' and c.studentId = '" + student.getStudentId() + "'");
				pro.setCollections(collections);
				List reports = baseDao.findListByHql("from Report r where r.productId = '"
						+ productId + "' and r.studentId = '" + student.getStudentId() + "'");
				pro.setReports(reports);
				// 查看是否被当前用户关注
				if (pro.getStudent() == null) {// 学生为空的原因可能是(用户被删除)
					list.remove(pro);
					continue;
				}
				String toStudentId = pro.getStudent().getStudentId();
				String sql = "select * from attention a where a.student_id = '"
						+ student.getStudentId() + "' and a.to_student_id = '" + toStudentId + "'";
				List list2 = baseDao.findListBySql(sql, new String[] {});
				if (list2.size() > 0) {// 此用户被当前用户关注
					pro.getStudent().setIsAttention("Y");
				} else {
					pro.getStudent().setIsAttention("N");
				}
			}
		}
		// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
		String products = jsonObject.toString();
		editJson.getMap().put("products", products);
		return editJson;
	}

	public Object findHomeStudents(String studentId, String type, Page page,
			HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List<Student> list = null;
		Student student = null;
		if (request.getSession().getAttribute("student") != null) {
			student = (Student) request.getSession().getAttribute("student");
		}
		if (type.equals("attention")) {
			list = studentDao.findHomeProductsByAttention(studentId, type, page);
		} else if (type.equals("student")) {
			list = studentDao.findHomeProductsByStudent(studentId, type, page);
		}
		for (Student stu : list) {
			if (student != null) {
				// 查看是否被当前用户关注
				if (stu == null) {// 学生为空的原因可能是(用户被删除)
					list.remove(stu);
					continue;
				}
				String toStudentId = stu.getStudentId();
				String sql = "select * from attention a where a.student_id = '"
						+ student.getStudentId() + "' and a.to_student_id = '" + toStudentId + "'";
				List list2 = baseDao.findListBySql(sql, new String[] {});
				if (list2.size() > 0) {// 此用户被当前用户关注
					stu.setIsAttention("Y");
				} else {
					stu.setIsAttention("N");
				}
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
		jsonConfig.setExcludes(new String[] { "studentSuggestionLabels", "studentCollectLabels",
				"school", });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
		String studentsJSON = jsonObject.toString();
		editJson.getMap().put("students", studentsJSON);
		return editJson;
	}

	public void saveViewCount(Student student) {
		student.setViewCount(student.getViewCount() + 1);
		baseDao.updateEntity(student);
	}

	public Object saveStudentStudent(String invitationId, String telephone,
			HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		EditJson editJson = new EditJson();
		// 被加的人是否为老师（type==QY/DR）
		Student toStudent = studentDao.findStudentByInvitationId(invitationId);
		if (toStudent == null
				|| (!toStudent.getRole().getRoleType().equals("company_manager_user")
						&& !toStudent.getRole().getRoleType().equals("company_common_user")
						&& !toStudent.getRole().getRoleType().equals("nc_company_manager_user") && !toStudent
						.getRole().getRoleType().equals("nc_company_common_user"))) {
			editJson.setSuccess(false);
			editJson.setMessage("no person");
			return editJson;
		}
		// 如果第一次发请求则入库student_student
		StudentStudent ss = studentDao.findStudentStudentByRelation(student.getStudentId(),
				toStudent.getStudentId());
		if (ss == null) {
			studentDao.saveStudentStudent(student.getStudentId(), toStudent.getStudentId(),
					telephone);
		} else {// 如果已经发送了请求则直接更新时间和状态
			ss.setState("D");
			ss.setCreateTime(DateUtil.formatDate());
			studentDao.updateStudentStudent(ss);
		}
		// 保存用户操作到student_act中
		StudentAct sa = new StudentAct();
		sa.setActType("ADD");
		sa.setObjId(toStudent.getStudentId());
		sa.setObjType("3");
		sa.setActId(null);
		sa.setToStudentId(toStudent.getStudentId());
		sa.setStudentId(student.getStudentId());
		baseDao.saveEntity(sa);
		return editJson;
	}

	public Object saveStudentStudentMessage(String studentId, String type,
			HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		// 拒绝或者同意的处理
		Object object = studentDao.updateStudentStudent(studentId, student.getStudentId(), type);
		// 用户操作日志记录
		// 保存用户操作到student_act中
		String actType = null;
		if (type.equals("N")) {
			actType = "ADDN";
		} else {
			if (type.equals("Y")) {
				actType = "ADDY";
			} else if (type.equals("YBM")) {
				actType = "ADDYBM";
			} else if (type.equals("YBY")) {
				actType = "ADDYBY";
			}
			// 更新用户的学生数量
			student.setStudentCount(student.getStudentCount() + 1);
			studentDao.updateStudent(student);
		}
		// 当前用户(student.getStudentId())同意(actType)或者拒绝了某人(toStudentId)的师生（objType）关系请求
		StudentAct sa = new StudentAct();
		sa.setActType(actType);
		sa.setObjId(studentId);
		sa.setObjType("3");
		sa.setActId(null);
		sa.setToStudentId(studentId);
		sa.setStudentId(student.getStudentId());
		baseDao.saveEntity(sa);
		return editJson;
	}

	public Object findMessageStudentFlowerList(Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Object object = studentDao.findMessageStudentFlowerList(page, studentId);
		editJson.setBean(object);
		return editJson;
	}

	public Object findMessageStudentStudentList(Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		Object object = studentDao.findMessageStudentStudentList(page, studentId);
		editJson.setBean(object);
		return editJson;
	}

	/**
	 * 记录用户日志
	 * 
	 * @param studentId
	 *            用户id，未登录则职位null
	 * @param methodName
	 *            controller中的方法名称
	 * @return
	 */
	public void saveStudentLog(String studentId, String methodName, String mapping) {
		studentDao.saveStudentLog(studentId, methodName, mapping);
	}
}
