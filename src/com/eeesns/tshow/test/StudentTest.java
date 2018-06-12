package com.eeesns.tshow.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Formula;
import org.hibernate.transform.Transformers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;

public class StudentTest {
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
	public void findStudent() {
		Query query = session.createQuery("from Student s where s.studentId = '2011228122'");
		List list = query.list();
		JsonConfig jsonConfig = new JsonConfig();
		JSONArray json = JSONArray.fromObject(list, jsonConfig);
		System.out.println(json.toString());
	}

	@Test
	public void findStudent1() {
		Student s = (Student) session.get(Student.class, "2011228122");
		JSONArray json = JSONArray.fromObject(s);
		System.out.println(json.toString());
	}

	@Test
	public void findStudent2() {
		String userName = "465471659@qq.com";
		String password = "135798765long";
		String hql = "from Student s where s.email = ? or s.invitationId = ?";
		Session session = baseDao.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, userName);
		query.setString(1, password);
		Student student = (Student) query.uniqueResult();
		System.out.println(student);
	}
}
