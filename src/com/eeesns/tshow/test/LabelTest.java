package com.eeesns.tshow.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Student;

public class LabelTest {
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-core.xml");
	Transaction beginTransaction;
	Session session;
	BaseDao baseDao;

	@Before
	public void before() {
		SessionFactory sessionFactory = (SessionFactory) ac.getBean("sessionFactory");
		baseDao = (BaseDao) ac.getBean("baseDao");
		session = sessionFactory.openSession();
		beginTransaction = session.beginTransaction();
	}

	@After
	public void after() {
		beginTransaction.commit();
		session.close();
	}

	@Test
	public void findProduct() {
		Query query = session.createQuery("from Label ");
		List list = query.list();
		JsonConfig jsonConfig = new JsonConfig();
		// 不需要查询出学校标签
		// jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		// jsonConfig.setExcludes(new String[] { "titleReplys" });
		JSONArray json = JSONArray.fromObject(list, jsonConfig);
		System.out.println(json.toString());
	}

	@Test
	public void findLabelIdByName() {
		String sql = "select p.point_id  pointId from point p where p.point_name = '" + 123 + "'";
		Map map = baseDao.findMapBySql(sql, new String[] {});
		if (map == null) {
			return;
		}
		String pointId = (String) map.get("pointId");
	}

	@Test
	public void findLabelIdByKey() {
		String labelId = "sd";
		String sql = "select s.school_id schoolId,s.name schoolName,l.label_id labelId,l.label_name labelName,sl.product_count productCount,sl.count studentCount "
				+ " from school_label sl join school s on(sl.school_id = s.school_id) join label l on(l.label_id = sl.label_id) where sl.label_id = '"
				+ labelId + "'";
		List list = baseDao.findListBySql(sql, new String[] {});
		System.out.println(list.size());
	}
	
	@Test
	public void findLabelbyId() {
		Label label= (Label) session.get(Student.class, "1");
		System.out.println(label);
	}
}
