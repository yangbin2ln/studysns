package com.eeesns.tshow.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.NotationReply;
import com.eeesns.tshow.entity.NotationReplyReply;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Province;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.util.UUUID;

public class TestSchool {
	@Test
	public void findSchool(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		School sc=(School) session.get(School.class, "10056");
		System.out.println(sc);
		JSONObject json=JSONObject.fromObject(sc);
		System.out.println(json);
		session.close();
	}
	@Test
	public void findPageEntitySchool(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		BaseDao baseDao = (BaseDao) ac.getBean("baseDao");
		baseDao.findPageEntityBySql("select sc.*,(select count(*) from school_label sl where sl.school_id =school_id and sl.state='Y') unlockLabelCount,(select count(*) from student st where st.school_id =sc.school_id) studentCount from school sc where sc.school_id = '10056'", 0, 1, true, School.class);
		School sc=(School) session.get(School.class, "10056");
		System.out.println(sc);
		JSONObject json=JSONObject.fromObject(sc);
		System.out.println(json);
		session.close();
	}
	
	@Test
	public void findAllSchool(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		/*String hql2="from School";
		Query query2 = session.createQuery(hql2);
		List list2 = query2.list();*/
		String hql="from Province";
		Query query = session.createQuery(hql).setCacheable(true);
		List list = query.list();
		//JSONArray.fromObject(list);
		Province p=(Province) session.get(Province.class, "1");
		System.out.println("1111111111111111111111111111111111111111");
		//System.out.println(JSONObject.fromObject(p));
		System.out.println("44444444444444444444444444444444444444444");
		School s=(School) session.get(School.class, "10056");
		Session session3 = sessionFactory.openSession();
		String hql3="from Province";
		System.out.println("list3list3list3list3list3list3list3list3list3list3list3list3list3list3list3list3");
		beginTransaction.commit();
		session.close();
		Transaction beginTransaction3 = session3.beginTransaction();
		Query query3 = session3.createQuery(hql3).setCacheable(true);
		List list3 = query3.list();
		
		//JSONArray.fromObject(list);
		//System.out.println(JSONObject.fromObject(list3));
	
		beginTransaction3.commit();
		session3.close();
	}
	
	@Test
	public void findProvinceSecondCach(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		Province p=(Province) session.get(Province.class, "1");
		System.out.println("session1111--------------------------------------");
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.setExcludes(new String[] { "schoolLabels","labels" });  
	       JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(p, jsonConfig);  
	       System.out.println(jsonObject);
		System.out.println(JSONObject.fromObject(p));
		beginTransaction.commit();
		session.close();
		Session session3 = sessionFactory.openSession();
		Transaction beginTransaction3 = session3.beginTransaction();
		Province p3=(Province) session3.get(Province.class, "1");
		System.out.println("session2222--------------------------------------");
		System.out.println(JSONObject.fromObject(p3));
		beginTransaction3.commit();
		session3.close();
		Session session4 = sessionFactory.openSession();
		Transaction beginTransaction4 = session4.beginTransaction();
		Province p4=(Province) session4.get(Province.class, "1");
		System.out.println("session33333333--------------------------------------");
		System.out.println(JSONObject.fromObject(p4));
		beginTransaction4.commit();
		session4.close();
	}
	
	@Test
	public void findSchoolOne(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		Label p=(Label) session.get(Label.class, "01");
		beginTransaction.commit();
		session.close();
		Session session3 = sessionFactory.openSession();
		Transaction beginTransaction3 = session3.beginTransaction();
		session3.get(Label.class, "01");
		//JSONArray.fromObject(list);
		//System.out.println(JSONObject.fromObject(list3));
		
		beginTransaction3.commit();
		session3.close();
		Session session4 = sessionFactory.openSession();
		Transaction beginTransaction4 = session4.beginTransaction();
		session4.get(Label.class, "01");
		//JSONArray.fromObject(list);
		//System.out.println(JSONObject.fromObject(list3));
		
		beginTransaction4.commit();
		session4.close();
	}
	
	@Test
	public void findProvinceOne(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		Province p=(Province) session.get(Province.class, "1");
		
		session.get(Province.class, "1");
		//JSONArray.fromObject(list);
		//System.out.println(JSONObject.fromObject(list3));
		
		session.get(Province.class, "1");
		//JSONArray.fromObject(list);
		//System.out.println(JSONObject.fromObject(list3));
		beginTransaction.commit();
		session.close();
	}
}
