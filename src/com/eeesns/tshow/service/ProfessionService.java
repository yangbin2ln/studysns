package com.eeesns.tshow.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.ProfessionDao;

@Service
public class ProfessionService {
	@Resource
	private BaseDao baseDao;
	@Resource
	private ProfessionDao professionDao;

	public Object findProfessionListByKey(String key, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List list = professionDao.findProfessionListByKey(key);
		editJson.setBean(list);
		return editJson;
	}
}
