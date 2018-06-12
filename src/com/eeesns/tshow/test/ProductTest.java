package com.eeesns.tshow.test;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

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
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.util.DateUtil;

public class ProductTest {
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
		Query query = session.createQuery("from Product ");
		List list = query.list();
		JsonConfig jsonConfig = new JsonConfig();
		// 不需要查询出学校标签
		// jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		// jsonConfig.setExcludes(new String[] { "titleReplys" });
		JSONArray json = JSONArray.fromObject(list, jsonConfig);
		System.out.println(json.toString());
	}

	@Test
	public void findPageProduct() {
		// Page page =
		// baseDao.findPageByHql("from Product p left join fetch p.praises s  where s.studentId='2011228122' ",
		// 1, 2, true);
		// Page page =
		// baseDao.findPageByHql("from Product p left join p.praises s  where p.productId = '132123123' ",
		// 1, 2, true);
		List page = session.createQuery("from Product p  ").setString("nowStudentId", "2011228122")
				.list();
		/*
		 * for(int i=0;i<page.size();i++){ List list =
		 * session.createFilter(((Product)page.get(i)).getPraises(),
		 * "where this.studentId = '2011228122'").list();
		 * System.out.println(list); }
		 */
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys" });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(page, jsonConfig);
		System.out.println(jsonObject.toString());

	}

	@Test
	public void findPageProductVersion() {
		// Page page =
		// baseDao.findPageByHql("from Product p left join fetch p.praises s  where s.studentId='2011228122' ",
		// 1, 2, true);
		// Page page =
		// baseDao.findPageByHql("from Product p left join p.praises s  where p.productId = '132123123' ",
		// 1, 2, true);
		List page = session.createQuery("from ProductVersion").list();
		/*
		 * for(int i=0;i<page.size();i++){ List list =
		 * session.createFilter(((Product)page.get(i)).getPraises(),
		 * "where this.studentId = '2011228122'").list();
		 * System.out.println(list); }
		 */
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys" });
		JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(page, jsonConfig);
		System.out.println(jsonObject.toString());

	}

	@Test
	public void updateLastTimeProduct() {
		Student s = (Student) session.get(Student.class, "2011228122");
		/* 更新当前作品的最后一次活动时间 */
		String titleReplyId = "14449101513648547399327430414315";
		String sql = "select tr.product_id productId from title_reply tr where tr.title_reply_id ='"
				+ titleReplyId + "'";
		Map map = baseDao.findMapBySql(sql, new String[] { "productId" });
		String productId = (String) map.get("productId");
		Product product = (Product) session.get(Product.class, productId);
		String strDate = DateUtil.formatDate();
		System.out.println(strDate);
		product.setLastActTime(strDate);
		session.update(product);
	}

	@Test
	public void findIntTest() {
		String sql = "select count(1) from product p where p.product_id = '123'";
		int count = baseDao.findIntBySql(sql);
		System.out.println(count);
	}

	@Test
	public void findProducts() {
		String hql = "from Product p where p.student.studentId is not null ";
		Page page = baseDao.findPageByHql(hql, 1, 10, false);
		System.out.println(page.getResult().size());
	}
}
