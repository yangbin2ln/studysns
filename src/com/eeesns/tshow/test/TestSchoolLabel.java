package com.eeesns.tshow.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.SchoolLabel;
import com.eeesns.tshow.entity.SchoolLabelKey;

public class TestSchoolLabel {
	@Test
	public void savaSchoolLabel(){

		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		SchoolLabel schoolLabel=new SchoolLabel();
		schoolLabel.setLabelId("010101");
		schoolLabel.setSchoolId("10014");
//		schoolLabel.setState("Y");
		session.save(schoolLabel);
		beginTransaction.commit();
	}
	@Test
	public void findSchoolLabel(){
		
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		SchoolLabelKey slk=new SchoolLabelKey();
		slk.setLabelId("010101");
		slk.setSchoolId("10014");
		SchoolLabel schoolLabel = (SchoolLabel) session.load(SchoolLabel.class, slk);
		schoolLabel.setState("Y");
		schoolLabel.setCount(schoolLabel.getCount()+1);
		beginTransaction.commit();
	}
	@Test
	public void updateSchoolLabel(){
		
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		SchoolLabelKey slk=new SchoolLabelKey();
		slk.setLabelId("010101");
		slk.setSchoolId("10014");
		SchoolLabel schoolLabel = (SchoolLabel) session.get(SchoolLabel.class, slk);
		schoolLabel.setState("Y");
		schoolLabel.setCount(schoolLabel.getCount()+1);
		session.update(schoolLabel);	
		 schoolLabel = (SchoolLabel) session.get(SchoolLabel.class, slk);
		schoolLabel.setCount(schoolLabel.getCount()+1);
		session.update(schoolLabel);	
		beginTransaction.commit();
	}
	@Test
	public void updateSchoolLabel2(){
		
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		SchoolLabelKey slk=new SchoolLabelKey();
		slk.setLabelId("010101");
		slk.setSchoolId("10014");
		System.out.println(slk);
		SchoolLabel schoolLabel = (SchoolLabel) session.get(SchoolLabel.class, slk);
		System.out.println(slk);
		System.out.println(schoolLabel);
		schoolLabel.setState("Y");
		schoolLabel.setCount(schoolLabel.getCount()+1);
		session.update(schoolLabel);	
		slk.setLabelId("0301");
		slk.setSchoolId("10056");
		schoolLabel = (SchoolLabel) session.get(SchoolLabel.class, slk);//不能修改主键
		schoolLabel.setCount(schoolLabel.getCount()+1);
		session.update(schoolLabel);	
		beginTransaction.commit();
	}

}
