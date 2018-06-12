package com.eeesns.tshow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.service.SearchService;
import com.eeesns.tshow.service.StudentService;

@Controller
@RequestMapping(value = "search")
public class SearchController {
	@Resource
	private SearchService searchService;

	@ResponseBody
	@RequestMapping(value = "")
	public Object findByKey(String key, HttpServletRequest request) {
		Page page = new Page();
		page.setPageNo(1);
		page.setPageSize(5);
		Object object = searchService.findByKey(key, page, request);
		// 记录此次用户检索行为
		searchService.saveSearchLog(key, request);
		return object;

	}

	/**
	 * 校内检索
	 * 
	 * @param key
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "school")
	public Object findBySchoolIdAndKey(String schoolId, String key, HttpServletRequest request) {
		Page page = new Page();
		page.setPageNo(1);
		page.setPageSize(5);
		Object object = searchService.findBySchoolIdAndKey(schoolId, key, page, request);
		// 记录此次用户检索行为
		searchService.saveSearchLog(schoolId + "," + key, request);
		return object;

	}

}
