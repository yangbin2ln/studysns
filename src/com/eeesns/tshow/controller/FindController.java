package com.eeesns.tshow.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.FindService;
import com.eeesns.tshow.service.LabelService;

@Controller
@RequestMapping(value = "find")
public class FindController {
	@Resource
	private FindService findService;

	@RequestMapping(value = "")
	public String findJsp(HttpServletRequest request) {
		if(request.getSession().getAttribute("student")==null){
			request.setAttribute("studentJSON",false);
			request.setAttribute("loginningstudent",null);
		}else{
			Student student = (Student) request.getSession().getAttribute("student");
			request.setAttribute("studentJSON", JSONObject.fromObject(student).toString());
			request.setAttribute("loginningstudent", student);
		}
		
		return "jsp/find/find";
	}

	@ResponseBody
	@RequestMapping(value = "initHtml")
	public Object findInitHtml(Page page, HttpServletRequest request) {
		page.setPageSize(6);//和find.js前台一致
		page.setPageNo(1);
		Object object = findService.initHtml(page);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "labelsFir")
	public Object findLabelsFir(Page page, HttpServletRequest request) {
		Map map = new HashMap();
		Page page2 = findService.findLabelsFir(page);
		map.put("labelsFir", page2.getResult());
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "labelChildren")
	public Object findLabelsChildren(String pid, Page page, HttpServletRequest request) {
		Map map = new HashMap();
		Page page2 = findService.findLabelsChildren(pid, page);
		map.put("children", page2.getResult());
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "recommend")
	public Object findRecommend(Page page, HttpServletRequest request) {
		Object object = findService.findRecommend(page);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "labelNew")
	public Object findLabelNew(Page page, HttpServletRequest request) {
		Map map = new HashMap();
		Page page2 = findService.findLabelsNew(page);
		map.put("labelsNew", page2.getResult());
		return map;
	}
}
