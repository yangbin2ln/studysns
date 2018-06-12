package com.eeesns.tshow.test;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eeesns.tshow.entity.School;

public class SchoolLabelTest {
	@Test
	public void findSchool(){

		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		School s = (School)session.get(School.class, "10056");
		System.out.println("--------------------------------------------------------");
		System.out.println(JSONObject.fromObject(s));
		beginTransaction.commit();
		session.close();
	}
	@Test
	public void findAllSchoolLabel(){
		
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		Query query = session.createQuery("from SchoolLabel");
		List list = query.list();
		System.out.println(JSONArray.fromObject(list));
		beginTransaction.commit();
		session.close();
	}
}
