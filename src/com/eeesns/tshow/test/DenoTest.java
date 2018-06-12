package com.eeesns.tshow.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.util.UUUID;

public class DenoTest {

	@Test
	public void saveOrUpdateProduct(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		Product p=(Product) session.get(Product.class, "14395427060234244935655640484161");
		session.evict(p);
		p.setProductId("132123123");
		p.setProductName("论人类社会的进化论22");
		session.saveOrUpdate(p);
		beginTransaction.commit();
		session.close();
	}
	
	@Test
	public void findProduct(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction beginTransaction = session.beginTransaction();
		Product p=(Product)session.get(Product.class,"14393086616557753770837042446831");
		/*System.out.println(p.getNotationReplys());
		System.out.println(p.getNotationReplys().get(0).getProduct().getProductId());*/
		JsonConfig jsonConfig  =new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		//jsonConfig.setExcludes(new String[]{"interest","professionLabel","school","jobLabel","labelId"});
		/*JSONObject json=JSONObject.fromObject(p.getLabel());
		JSONObject json1=JSONObject.fromObject(p.getStudent().getJobLabel());
		JSONObject json3=JSONObject.fromObject(p.getStudent().getProfessionLabel());
		//JSONArray json2=JSONArray.fromObject(p.getStudent().getInterest());
		JSONArray json4=JSONArray.fromObject(p.getStudent().getSchool());*/
		/*System.out.println(json.toString());
		System.out.println(json1.toString());
		//System.out.println(json2.toString());
		System.out.println(json3.toString());
		System.out.println(json4.toString());*/
		System.out.println(p.getStudent().getStudentId());
		Map m=new HashMap();
		JSONObject json5=JSONObject.fromObject(p);
		System.out.println(json5.toString());
		beginTransaction.commit();
		session.close();
	}
	@Test
	public void findStudentByPage(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		BaseDao baseDao=(BaseDao) ac.getBean("baseDao");
		Page page = baseDao.findPageByHql("from Student", 0, 3, true);
		for(int i=0;i<page.getResult().size();i++){
			System.out.println(page.getResult().get(i).toString());
		}
	}
	@Test
	public void findProductByPage(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext-core.xml");
		SessionFactory sessionFactory=(SessionFactory) ac.getBean("sessionFactory");
		BaseDao baseDao=(BaseDao) ac.getBean("baseDao");
		Page page = baseDao.findPageByHql("from Product order by create_time desc", 0, 3, true);
		for(int i=0;i<page.getResult().size();i++){
			System.out.println(page.getResult().get(i).toString());
		}
	}
}
