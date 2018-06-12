package com.eeesns.tshow.test;

import java.io.Serializable;
import java.util.List;

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
import com.eeesns.tshow.entity.Course;
import com.eeesns.tshow.entity.InvitationCode;
import com.eeesns.tshow.entity.Student;

public class CourseTest {
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-core.xml");
	Transaction beginTransaction;
	Session session;
	BaseDao baseDao;

	public static void main(String[] args) {
		CourseTest c = new CourseTest();
		System.out.println(c.ac.getBean("baseDao"));
	}

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
	public void saveCourse() {
		Course course = new Course();
		course.setContent("手绘课程");
		course.setPlace("书香大地");
		course.setTelephone("12345678911");
		course.setStudentId("2011228122");
		Serializable save = session.save(course);
		System.out.println(save);
	}

	@Test
	public void deleteCourse() {
		Course course = new Course();
		course.setCourseId("14491271063959966656196416740108");
		session.delete(course);
	}

	@Test
	public void saveOrUpdateCourse() {
		Course course = (Course) session.get(Course.class, "14491273148822181859129495674413");
		course.setContent("啦啦啦啦");
		session.update(course);
	}

	@Test
	public void saveInvitationCode() {
		InvitationCode ic = new InvitationCode();
		ic.setEmail("adsfd");
		ic.setInvitationId("00017d");
		session.save(ic);
	}
}
