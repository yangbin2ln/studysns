package com.eeesns.tshow.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import com.eeesns.tshow.entity.Point;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.util.UUUID;

public class TestNotationReply {
	@Test
	public void testFindAllLeader(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		NotationReplyReply n=new NotationReplyReply();
		n.setNotationReplyId("14393086616555182744550829811664");
		Student s=new Student();
		Student ts=new Student();
		s.setStudentId("2011228122");
		ts.setStudentId("2011228107");
		n.setReplyStudent(s);
		n.setToreplyStudent(ts);
		n.setReplyContent("恩，我们永远在一起！");
		session.save(n);
		beginTransaction.commit();
		session.close();
	}
	
	@Test
	public void saveOrUpdate(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		NotationReply n=new NotationReply();
		n.setNotationContent("123");
		n.setNotationContent("恩，我们永远在一起！");
		n.setProductContent("asdfads");
		n.setStart("4");
		n.setEnd("6");
		n.setNotationReplyId("14398906150513474127322011048115");
		String createTime=(new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
		n.setCreateTime(createTime);
		 System.out.println(createTime);
		session.saveOrUpdate(n);
		beginTransaction.commit();
		session.close();
	}
	
	
}
