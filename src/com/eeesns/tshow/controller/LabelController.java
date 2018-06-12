package com.eeesns.tshow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Point;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.LabelService;
import com.eeesns.tshow.service.ProductService;
import com.eeesns.tshow.service.SchoolService;

@Controller
@RequestMapping(value = "label")
public class LabelController {
	@Resource
	private LabelService labelService;
	@Resource
	private SchoolService schoolService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public Object findLabels(String replyId, HttpServletRequest request) {
		Object object = labelService.findLabels(request);
		return object;
	}

	/**
	 * 查询一级标签
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listF")
	public Object findLabelsF() {
		Object object = labelService.findLabelsF();
		return object;
	}

	@RequestMapping(value = "/home/{labelId}", method = RequestMethod.GET)
	public String findLabelJsp(@PathVariable("labelId") String labelId, String pointId,
			String schoolId, HttpServletRequest request) {
		if (labelId == null) {
			return "jsp/404";
		}
		if (request.getSession().getAttribute("student") == null) {
			request.setAttribute("studentJSON", false);
			request.setAttribute("loginningstudent", null);
		} else {
			Student student = (Student) request.getSession().getAttribute("student");
			request.setAttribute("studentJSON", JSONObject.fromObject(student).toString());
			request.setAttribute("loginningstudent", student);
		}
		Label label = labelService.findLabelByLabelId(labelId);
		School school = schoolService.findSchoolBySchoolId(schoolId);
		Point point = labelService.findPointByPointId(pointId);
		request.setAttribute("label", label);
		request.setAttribute("school", school);
		request.setAttribute("point", point);
		return "jsp/label/label";
	}

	@ResponseBody
	@RequestMapping(value = "initHtml")
	public Object findInitHtml(String labelId, String pointId, String schoolId, Page page,
			HttpServletRequest request) {
		// 初始化html
		Map map = labelService.findInitHtml(labelId, pointId, schoolId, page, request);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "points")
	public Object findPointByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			Page page, HttpServletRequest request) {
		List points = labelService.findPointByLabelIdorSchoolId(labelId, pointId, schoolId, page,
				request);
		Map map = new HashMap();
		map.put("points", points);
		return map;
	}

	/**
	 * 查询二级标签下的热门标签
	 * 
	 * @param labelId
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "hotPoints")
	public Object findHotPointsByLabelId(String labelId, Page page, HttpServletRequest request) {
		Object object = labelService.findHotPointsByLabelId(labelId, page, request);
		return object;
	}

	/**
	 * 热门评论元素
	 * 
	 * @param labelId
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "hotReplyPoints")
	public Object findHotReplyPointsByLabelId(String labelId, Page page, HttpServletRequest request) {
		Object object = labelService.findHotReplyPointsByLabelId(labelId, page, request);
		return object;
	}

	/**
	 * 达人查询(标签主页和学校标签主页会用到)
	 * 
	 * @param labelId
	 * @param pointId
	 * @param schoolId
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "students")
	public Object findStudentByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			Page page, HttpServletRequest request) {
		Page studentsPage = labelService.findStudentByLabelIdorSchoolId(labelId, pointId, schoolId,
				"score", "master", page, request);
		List students = studentsPage.getResult();
		Long studentsCount = studentsPage.getTotalCount();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "interest",
				"studentSuggestionLabels", "studentCollectLabels", "role" });
		JSONArray jsonObject = JSONArray.fromObject(students, jsonConfig);
		String studentsJSON = jsonObject.toString();
		JsonConfig jsonConfig2 = new JsonConfig();
		Map map = new HashMap();
		map.put("students", studentsJSON);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "products")
	public Object findProductByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			String type, Page page, HttpServletRequest request) {
		Page productsPage = labelService.findProductByLabelIdorSchoolId(labelId, pointId, schoolId,
				type, page, request);
		List products = productsPage.getResult();
		Long productsCount = productsPage.getTotalCount();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "titleReplys", "interest",
				"studentLabels", "studentSuggestionLabels", "studentCollectLabels", "role" });
		JSONArray jsonObject = JSONArray.fromObject(products, jsonConfig);
		String productsJSON = jsonObject.toString();
		Map map = new HashMap();
		map.put("products", productsJSON);
		map.put("productsCount", productsCount);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "schools")
	public Object findSchoolsByLabelIdAndPointId(String labelId, String pointId, Page page,
			HttpServletRequest request) {
		Map map = new HashMap();
		page.setPageSize(5);
		page.setPageNo(1);
		List schools = labelService.findSchoolsByLabelIdAndPointId(labelId, pointId, page);
		map.put("schools", schools);
		return map;
	}

}
