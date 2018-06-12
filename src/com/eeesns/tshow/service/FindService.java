package com.eeesns.tshow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eeesns.tshow.dao.FindDao;
import com.eeesns.tshow.dao.Page;

@Service
public class FindService {
	@Resource
	FindDao findDao;

	public Object initHtml(Page page) {
		Map map = new HashMap();
		//查询最新二级标签
		Page labelsNewPage = findLabelsNew(page);
		List labelsNew = labelsNewPage.getResult();
		//查询热门推荐二级标签(包括内容标签)
		Map labelsAndPoitsHot = findRecommend(page);
		//查询一级标签
		Page labelsFirPage = findLabelsFir(page);
		List<Map> labelsFir = labelsFirPage.getResult();
			//查询此一级标签下的二级标签
			for(Map m:labelsFir){
				String pid = (String) m.get("labelId");
				Page labelsChildrenPage = findLabelsChildren(pid,page);
				List labelsChildren = labelsChildrenPage.getResult();
				m.put("children", labelsChildren);
			}
		map.put("labelsNew", labelsNew);
		map.put("recommend", labelsAndPoitsHot);
		map.put("labelsFir", labelsFir);
		return map;
	}

	/**
	 * 查询一级标签下的二级标签
	 * @param pid
	 * @param page
	 * @return
	 */
	public Page findLabelsChildren(String pid, Page page) {
		Page page2 = findDao.findLabelsChildren(pid,page);
		return page2;
	}

	/**
	 * 查询一级标签
	 * @param page
	 * @return
	 */
	public Page findLabelsFir(Page page) {
		Page labelsFirPage = findDao.findLabelsFir(page);
		List<Map> labelsFir = labelsFirPage.getResult();
		//查询此一级标签下的二级标签
		for(Map m:labelsFir){
			String pid = (String) m.get("labelId");
			Page labelsChildrenPage = findLabelsChildren(pid,page);
			List labelsChildren = labelsChildrenPage.getResult();
			m.put("children", labelsChildren);
		}
		return labelsFirPage;
	}

	/**
	 * 热门推荐
	 * @param page
	 * @return
	 */
	public Map findRecommend(Page page) {
		Page laPage = findDao.findLabelsHot(page);
		Page poPage = findDao.findPointsHot(page);
		Map map = new HashMap();
		map.put("labels", laPage.getResult());
		map.put("points", poPage.getResult());
		return map;
	}

	/**
	 * 最新二级标签
	 * @param page
	 * @return
	 */
	public Page findLabelsNew(Page page) {
		Page page2 = findDao.findLabelsNew(page);
		return page2;
	}
}
