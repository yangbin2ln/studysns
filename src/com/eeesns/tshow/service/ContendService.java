package com.eeesns.tshow.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Service;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.ContendDao;
import com.eeesns.tshow.dao.Page;

@Service
public class ContendService {
	@Resource
	ContendDao contendDao;

	public EditJson findSchoolLabelUnlock() {
		EditJson editJson = new EditJson();
		try {
			List list = contendDao.findSchoolLabelUnlock();
			editJson.getMap().put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询最新解锁学校标签异常");
		}

		return editJson;
	}

	public EditJson findAllProducts(Page page, String type, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		try {
			List list = contendDao.findAllProducts(page, type, request);
			JsonConfig jsonConfig = new JsonConfig();
			// 不需要查询出学校标签,并且为了加载速度的考虑，评论详情无需查出来
			jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "notationReplys",
					"interest", "pid", "studentLabels", "studentSuggestionLabels",
					"studentCollectLabels","role" });
			JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(list, jsonConfig);
			String products = jsonObject.toString();
			editJson.getMap().put("products", products);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("争鸣查询异常");
		}
		return editJson;
	}
}
