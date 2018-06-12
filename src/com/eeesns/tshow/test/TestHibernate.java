package com.eeesns.tshow.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;

public class TestHibernate {
	/**
	 * 原生sql
	 */
	@Test
	public void createQuery(){

		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
//		SQLQuery sqlQuery = session.createSQLQuery("select stu.* from student stu").addEntity(Student.class);
		SQLQuery sqlQuery = session.createSQLQuery("select stu.*,ll.*  from student stu LEFT JOIN label ll on(ll.label_id=stu.professional_id) where ll.job_id='0301' ").addEntity(Student.class);
		List<Student> list = sqlQuery.list();
		/*for(Student l:list){
			System.out.println(l.getInterest().get(0).getLabelName());
		}*/
		beginTransaction.commit();
		session.close();
	
	}

}
