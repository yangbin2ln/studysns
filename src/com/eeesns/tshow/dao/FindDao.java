package com.eeesns.tshow.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class FindDao {

	@Resource
	BaseDao baseDao;

	public Page findLabelsNew(Page page) {
		String sql = "select la.label_id labelId,la.label_name labelName,la2.label_id pLabelId,la2.label_name pLabelName,DATE_FORMAT(la.create_time , '%Y-%m-%d %H:%i:%S') createTime,"
				+ " la.background_image_small backImaS,la.product_count productCount,la.student_count studentCount,la.background_image_big backImgB"
				+ " from label la join label la2 on(la.pid = la2.label_id) order by la.create_time";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2;

	}

	public Page findLabelsHot(Page page) {
		// 被推荐的二级标签
		String sql = "select la.label_id labelId,la.label_name labelName,DATE_FORMAT(re.create_time , '%Y-%m-%d %H:%i:%S') reCreateTime,"
				+ " DATE_FORMAT(la.create_time , '%Y-%m-%d %H:%i:%S') laCreateTime,la.product_count productCount,la.student_count studentCount,la.background_image_small backImaS,la.background_image_big backImgB"
				+ " from recommend re join label la on(re.label_id = la.label_id) where re.state='0' and re.type ='LA'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2;
	}

	public Page findPointsHot(Page page) {
		// 被推荐的内容标签
		String sql = "select po.point_id pointId,po.point_name pointName,DATE_FORMAT(po.create_time , '%Y-%m-%d %H:%i:%S') poCreateTime,"
				+ " la.label_id labelId,la.label_name labelName,DATE_FORMAT(la.create_time , '%Y-%m-%d %H:%i:%S') laCreateTime,po.background_image_small backImaS,po.background_image_big backImgB,"
				+ " po.hot hot from recommend re join point po on(re.label_id = po.point_id) join label la on(po.label_id = la.label_id)"
				+ " where re.state='0' and re.type = 'PO' order by re.create_time";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2;
	}

	public Page findLabelsFir(Page page) {
		String sql = "select la.label_id labelId,la.label_name labelName,DATE_FORMAT(la.create_time , '%Y-%m-%d %H:%i:%S') createTime,la.product_count productCount,la.student_count studentCount"
				+ " from label la where la.pid is null";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2;
	}

	public Page findLabelsChildren(String pid, Page page) {
		String sql = "select la.label_id labelId,la.label_name labelName,DATE_FORMAT(la.create_time , '%Y-%m-%d %H:%i:%S') createTime,la.product_count productCount,la.student_count studentCount,"
				+ " la.background_image_small backImaS,la.background_image_big backImgB from label la where la.pid = '"
				+ pid + "'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2;
	}

}
