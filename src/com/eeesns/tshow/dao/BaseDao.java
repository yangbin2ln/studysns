package com.eeesns.tshow.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;

import com.eeesns.tshow.entity.BaseEntity;

import sparknet.starshine.common.util.UUIDUtil;

/**
 * 公用DAO
 * 
 * @author yb
 * 
 */
@Component
public class BaseDao {
	public BaseDao() {
		System.out.println("baseDao");

	}

	private Session session;

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private SessionFactory sessionFactory;

	/**
	 * 获取SessionFactory
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 设置SessionFactory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获取Session
	 */
	public Session getSession() {
		try {
			return this.getSessionFactory().getCurrentSession();
		} catch (HibernateException e) {
			this.session = this.getSessionFactory().openSession();
			return this.session;
		}
	}

	/**
	 * 关闭Session（只针对openSession操作，getCurrentSession无效）
	 */
	public void closeSession() {
		if (null != this.session) {
			this.session.close();
			this.session = null;
		}
	}

	/************************************************ SQL操作 ******************************************************/

	/**
	 * 查询Int
	 * 
	 * @param sql
	 * @return
	 */
	public int findIntBySql(String sql) {
		int count = 0;
		try {
			count = ((Number) this.getSession().createSQLQuery(sql).uniqueResult()).intValue();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return count;
	}

	/**
	 * 查询Map
	 * 
	 * @param sql
	 * @param fields
	 * @return
	 */
	public HashMap findMapBySql(String sql, String[] fields) {
		HashMap hashMap = new HashMap();
		try {
			SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
			for (String field : fields) {
				sqlQuery.addScalar(field, StandardBasicTypes.STRING);
			}
			hashMap = (HashMap) sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return hashMap;
	}

	/**
	 * 查询List
	 * 
	 * @param sql
	 * @param fields
	 * @return
	 */
	public List findListBySql(String sql, String[] fields) {
		System.out.println(this);
		List list = new ArrayList();
		try {
			SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
			for (String field : fields) {
				sqlQuery.addScalar(field, StandardBasicTypes.STRING);
			}
			list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return list;
	}

	public List findListEntityBySql(String sql, String[] fields, Class clazz) {
		System.out.println(this);
		List list = new ArrayList();
		try {
			SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
			for (String field : fields) {
				sqlQuery.addScalar(field, StandardBasicTypes.STRING);
			}
			list = sqlQuery.addEntity(clazz).list();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return list;
	}

	/**
	 * 保存或更新Blob
	 * 
	 * @param sql
	 * @param inputStreams
	 */
	public void saveOrUpdateBlobBySql(String sql, final InputStream[] inputStreams) {
		try {
			final LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				protected void setValues(PreparedStatement ps, LobCreator lobCreator) {
					InputStream is = null;
					for (int i = 0; i < inputStreams.length; i++) {
						try {
							is = inputStreams[i];
							ps.setBlob(i + 1, is);
						} catch (Exception e) {
							throw new RuntimeException(e.getMessage(), e);
						} finally {
							try {
								if (null != is) {
									is.close();
									is = null;
								}
							} catch (IOException e) {
								throw new RuntimeException(e.getMessage(), e);
							}
						}
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 保存或更新Clob
	 * 
	 * @param sql
	 * @param byteList
	 */
	public void saveOrUpdateClobBySql(String sql, final List<byte[]> byteList) {
		try {
			final LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				protected void setValues(PreparedStatement ps, LobCreator lobCreator) {
					ByteArrayInputStream bis = null;
					InputStreamReader clobReader = null;
					for (int i = 0; i < byteList.size(); i++) {
						try {
							bis = new ByteArrayInputStream(byteList.get(i));
							clobReader = new InputStreamReader(bis);
							ps.setClob(i + 1, clobReader);
						} catch (Exception e) {
							throw new RuntimeException(e.getMessage(), e);
						} finally {
							try {
								if (null != clobReader) {
									clobReader.close();
									clobReader = null;
								}
								if (null != bis) {
									bis.close();
									bis = null;
								}
							} catch (IOException e) {
								throw new RuntimeException(e.getMessage(), e);
							}
						}
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 查询(用于查询clob、blob和普通字段)
	 * 
	 * @param sql
	 * @param clobFields
	 * @param blobFields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> findBySql(String sql, String[] clobFields, String[] blobFields) {
		// clob
		final String[] upperClobFields = new String[clobFields.length];
		for (int i = 0; i < clobFields.length; i++) {
			upperClobFields[i] = clobFields[i].toString().toUpperCase();
		}
		// blob
		final String[] upperBlobFields = new String[blobFields.length];
		for (int i = 0; i < blobFields.length; i++) {
			upperBlobFields[i] = blobFields[i].toString().toUpperCase();
		}
		final List<Map> result = new ArrayList<Map>();
		final LobHandler lobHandler = new DefaultLobHandler();
		List list = jdbcTemplate.query(sql, new RowMapper() {
			Map map = null;
			int colCount = 0;
			String colName = "";
			ResultSetMetaData metaData = null;

			public Object mapRow(ResultSet rs, int i) throws SQLException {
				map = new LinkedHashMap();
				metaData = rs.getMetaData();
				colCount = metaData.getColumnCount();
				for (int c = 1; c <= colCount; c++) {
					colName = metaData.getColumnName(c);
					if (ArrayUtils.contains(upperClobFields, colName)) {// clob
						map.put(colName, lobHandler.getClobAsString(rs, c));
					} else if (ArrayUtils.contains(upperBlobFields, colName)) {// blob
						map.put(colName, lobHandler.getBlobAsBinaryStream(rs, c));
					} else {// other
						map.put(colName, rs.getString(colName));
					}
				}
				result.add(map);
				return result;
			}
		});
		return result;
	}

	/**
	 * 分页查询(用于查询clob、blob和普通字段)
	 * 
	 * @param sql
	 * @param clobFields
	 * @param blobFields
	 * @param start
	 * @param limit
	 * @return
	 */
	public Map findPageBySql(String sql, String[] clobFields, String[] blobFields, int start,
			int limit) {
		Map map = new HashMap();
		try {
			if (start == -1 || limit == -1) {
				List<Map> list = findBySql(sql, clobFields, blobFields);
				// 记录数
				map.put("count", list.size());
				// 总页数
				map.put("pageCount", 1);
				// 结果集
				map.put("data", list);
			} else {
				int pageCount = 0;
				int countPage = 1;
				int recordCount = findIntBySql("SELECT COUNT(*) FROM (" + sql + ")");
				if (recordCount > 0) {
					if (start != -1 && limit != -1) {
						countPage = limit;
					}
					if (recordCount % countPage == 0) {
						pageCount = recordCount / countPage;
					} else {
						pageCount = recordCount / countPage + 1;
					}
				}

				sql = "SELECT * FROM (SELECT TMP.*,ROWNUM RN FROM (" + sql + ") TMP) ";
				if (start != -1 && limit != -1) {
					int cpage = start / limit + 1;
					sql += "WHERE RN >" + (cpage - 1) * countPage + " AND RN<= " + cpage
							* countPage;
				}
				// 记录数
				map.put("count", recordCount);
				// 总页数
				map.put("pageCount", pageCount);
				// 结果集
				map.put("data", findBySql(sql, clobFields, blobFields));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	/**
	 * 执行更新
	 * 
	 * @param sql
	 * @return
	 */
	public int executeUpdateBySql(String sql) {
		int count = 0;
		try {
			count = this.getSession().createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return count;
	}

	/**
	 * 保存字典文件
	 * 
	 * @param sql
	 * @return
	 */
	public String saveDicFile(Map map, String fileOrder) throws Exception {
		String fileId = "";
		InputStream is = null;
		try {
			fileId = UUIDUtil.getNextValue();
			StringBuffer sql = new StringBuffer();
			// 保存文件类型表
			File file = new File(map.get("fileContent").toString());

			if (file.exists()) {
				is = new FileInputStream(file);
			}
			// 保存文件信息表
			sql.delete(0, sql.length());
			sql.append("INSERT INTO FL_FILE_DIC(FILEID,FILENAME,FILESIZE,FILESTATE,UIID,UILABEL,FILEORDER,FILECONTENT) VALUES ( ");
			sql.append("'" + fileId + "',");
			sql.append("'" + map.get("fileName").toString() + "',");
			sql.append("'" + map.get("fileSize").toString() + "',");
			sql.append("'" + map.get("fileState").toString() + "',");

			sql.append("'UIID',");
			sql.append("'" + map.get("uiLabel").toString() + "',");
			sql.append("'" + fileOrder + "',");
			sql.append("?");
			sql.append(")");
			saveOrUpdateBlobBySql(sql.toString(), new InputStream[] { is });
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
		}
		return fileId;
	}

	/**
	 * 查询分页
	 * 
	 * @param sql
	 * @param start
	 *            开始页
	 * @param limit
	 *            每页显示条数
	 * @param isAutoCount
	 *            是否自动获取总数
	 * @param fields
	 *            显示字段
	 * @return
	 */
	public Page findPageBySql(String sql, int start, int limit, boolean isAutoCount, String[] fields) {
		Page page = new Page();
		try {
			page.setPageNo(start);
			page.setPageSize(limit);
			page.setAutoCount(isAutoCount);
			page = this.extendFindPageBySql(this.getSession(), page, sql, fields);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}

		return page;
	}

	private Page extendFindPageBySql(Session session, Page page, String sql, String[] fields)
			throws Exception {
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		if (page.isAutoCount()) {
			page.setTotalCount(countSqlResult(sql));
		}
		for (String field : fields) {
			sqlQuery.addScalar(field, StandardBasicTypes.STRING);
		}
		if (page.getFirst() != -1 && page.getPageSize() != -1) {
			sqlQuery.setFirstResult(page.getFirst() - 1);
			sqlQuery.setMaxResults(page.getPageSize());
		}
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		page.setResult(sqlQuery.list());
		return page;
	}

	public Page findPageEntityBySql(String sql, int start, int limit, boolean isAutoCount,
			Class clazz) {
		Page page = new Page();
		try {
			page.setPageNo(start);
			page.setPageSize(limit);
			page.setAutoCount(isAutoCount);
			page = this.extendFindPageEntityBySql(this.getSession(), page, sql, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			// closeSession();
		}

		return page;
	}

	private Page extendFindPageEntityBySql(Session session, Page page, String sql, Class clazz)
			throws Exception {
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		if (page.isAutoCount()) {
			page.setTotalCount(countSqlResultNew(sql));
		}
		if (page.getFirst() != -1 && page.getPageSize() != -1) {
			sqlQuery.setFirstResult(page.getFirst() - 1);
			sqlQuery.setMaxResults(page.getPageSize());
		}
		sqlQuery.addEntity(clazz);
		page.setResult((sqlQuery.list() == null ? new ArrayList() : sqlQuery.list()));
		return page;
	}

	private int countSqlResult(String sql) {
		String countSql = prepareCountSql(sql);
		try {
			return this.findIntBySql(countSql);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private int countSqlResultNew(String sql) {
		String countSql = prepareCountSqlNew(sql);
		try {
			return this.findIntBySql(countSql);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private String prepareCountSql(String sql) {
		String fromSql = sql;
		if (fromSql.indexOf("FROM") > 0) {// 大写
			fromSql = (new StringBuilder()).append("from ")
					.append(StringUtils.substringAfter(fromSql, "FROM")).toString();
		} else {// 小写
			fromSql = (new StringBuilder()).append("from ")
					.append(StringUtils.substringAfter(fromSql, "from")).toString();
		}
		fromSql = StringUtils.substringBefore(fromSql, "order by");
		String countSql = (new StringBuilder()).append("select count(*) ").append(fromSql)
				.toString();
		return countSql;
	}

	private String prepareCountSqlNew(String sql) {
		String fromSql = sql;
		fromSql = StringUtils.substringBefore(fromSql, "order by");
		String countSql = (new StringBuilder()).append("select count(*) from (").append(fromSql)
				.toString();
		countSql += ") tt";
		return countSql;
	}

	public void deleteBySql(String sql, Object[] strs) {
		try {
			Query query = this.getSession().createSQLQuery(sql);
			for (int i = 0; i < strs.length; i++) {
				if (strs[i] instanceof String) {
					query.setString(i, (String) strs[i]);
				} else if (strs[i] instanceof Integer) {
					query.setInteger(i, (Integer) strs[i]);
				}
			}
			query.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
	}

	/************************************************ HQL操作 ******************************************************/

	/**
	 * 查询Int
	 * 
	 * @param hql
	 * @return
	 */
	public int findIntByHql(String hql) {
		int count = 0;
		try {
			count = ((Number) this.getSession().createQuery(hql).uniqueResult()).intValue();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return count;
	}

	/**
	 * 查询Map
	 * 
	 * @param hql
	 * @return
	 */
	public HashMap findMapByHql(String hql) {
		HashMap hashMap = new HashMap();
		try {
			List<Map> list = this.getSession().createQuery(hql).list();
			if (list.size() > 0) {
				hashMap = (HashMap) list.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return hashMap;
	}

	public Object findEntityByHql(String hql) {
		Object object;
		try {
			object = this.getSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return object;
	}

	public List findEntityByHql(String hql, String[] strs) {
		List list;
		try {
			Query query = this.getSession().createQuery(hql);
			for (int i = 0; i < strs.length; i++) {
				query.setString(i, strs[i]);
			}
			list = query.list();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return list;
	}

	public Object findObjByHql(String hql) {
		Object obj = null;
		try {
			List list = this.getSession().createQuery(hql).list();
			if (list.size() > 0) {
				obj = list.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return obj;
	}

	/**
	 * 查询List
	 * 
	 * @param hql
	 * @return
	 */
	public List findListByHql(String hql) {
		List list = new ArrayList();
		try {
			list = this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return list;
	}

	public List findListByHql(String hql, String[] strs) {
		List list = new ArrayList();
		try {
			Query query = this.getSession().createQuery(hql);
			for (int i = 0; i < strs.length; i++) {
				query.setString(i, strs[i]);
				list = query.list();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return list;
	}

	/**
	 * 执行更新
	 * 
	 * @param hql
	 * @return
	 */
	public int executeUpdateByHql(String hql) {
		int count = 0;
		try {
			count = this.getSession().createQuery(hql).executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return count;
	}

	/**
	 * 查询分页
	 * 
	 * @param hql
	 * @param start
	 *            开始页
	 * @param limit
	 *            每页显示条数
	 * @param isAutoCount
	 *            是否自动获取总数
	 * @return
	 */
	public Page findPageByHql(String hql, int start, int limit, boolean isAutoCount) {
		Page page = new Page();
		try {
			page.setPageNo(start);
			page.setPageSize(limit);
			page.setAutoCount(isAutoCount);
			page = this.extendFindPageByHql(this.getSession(), page, hql);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return page;
	}

	private Page extendFindPageByHql(Session session, Page page, String hql) throws Exception {
		Query query = session.createQuery(hql);
		if (page.isAutoCount()) {
			page.setTotalCount(countHqlResult(hql));
		}
		if (page.getFirst() != -1 && page.getPageSize() != -1) {
			query.setFirstResult(page.getFirst() - 1);
			query.setMaxResults(page.getPageSize());
		}
		page.setResult(query.list());
		return page;
	}

	private int countHqlResult(String hql) {
		String countSql = prepareCountSql(hql);
		try {
			return this.findIntByHql(countSql);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/********************************************** Criteria操作 ***************************************************/

	/**
	 * 查询实体
	 * 
	 * @param clazz
	 * @param serializable
	 * @return
	 */
	public Object findEntity(Class clazz, Serializable serializable) {
		Object entity = null;
		try {
			entity = (Object) this.getSession().get(clazz, serializable);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return entity;
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public Serializable saveEntity(Object entity) {
		Serializable serializable = null;
		try {
			serializable = this.getSession().save(entity);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
		return serializable;
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void updateEntity(Object entity) {
		try {
			this.getSession().update(entity);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
	}

	/**
	 * 保存或更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdateEntity(Object entity) {
		try {
			this.getSession().saveOrUpdate(entity);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
	}

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void deleteEntity(Object entity) {
		try {
			this.getSession().delete(entity);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			closeSession();
		}
	}

	/* 新加 start */
	public Object findEntity(BaseEntity baseEntity) {
		Class<? extends Object> class1 = baseEntity.getClass();
		return this.getSession().get(class1, baseEntity.getId());
	}
	/* 新加 end */

}
