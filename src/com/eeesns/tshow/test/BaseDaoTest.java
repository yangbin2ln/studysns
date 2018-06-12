package com.eeesns.tshow.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.entity.ProductVersion;
import com.eeesns.tshow.entity.Role;

public class BaseDaoTest {
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
	public void findRoleByType() {
		String hql = "from Role ro where ro.roleType = 'talent_user'";
		Role role = (Role) baseDao.findEntityByHql(hql);
		System.out.println(role.getRoleName());
	}

	@Test
	public void findProductVersionBySeriesId() {
		String hql = "from ProductVersion pv where pv.productSeriesId = ? "
				+ " and pv.version = (select max(pv1.version) from ProductVersion pv1 where pv1.productSeriesId = '14520871229388347746422446146459')";
		List list = baseDao.findListByHql(hql, new String[] { "14520871229388347746422446146459" });
		System.out.println(list.get(0));
		System.out.println(list.size());
	}

	@Test
	public void deleteProductByVersionAfter() {
		String hql = "from ProductVersion pv where pv.productSeriesId = ? "
				+ " and pv.version = (select max(pv1.version) from ProductVersion pv1 where pv1.productSeriesId = productSeriesId)";
		List list = baseDao.findListByHql(hql, new String[] { "14520871229388347746422446146459" });
		if (list.size() > 0) {
			ProductVersion pv = (ProductVersion) list.get(0);
			pv.setPoints(null);
			System.out.println(pv);
			session.delete(pv);
		}
	}
}
