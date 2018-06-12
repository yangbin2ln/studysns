package com.eeesns.tshow.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sparknet.starshine.common.util.StringUtil;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Point;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.LabelService;
import com.eeesns.tshow.service.SchoolService;
import com.eeesns.tshow.service.StudentService;
import com.eeesns.tshow.service.TalentListService;

@Controller
@RequestMapping(value = "school")
public class SchoolController {
	@Resource
	private SchoolService schoolService;
	@Resource
	private LabelService labelService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public Object findSchools(HttpServletRequest request) {
		EditJson editJson = schoolService.findSchools();
		return editJson;
	}

	@ResponseBody
	@RequestMapping(value = "/schoolId")
	public Object findSchoolById(String schoolId, HttpServletRequest request) {
		EditJson editJson = schoolService.findSchoolById(schoolId);
		return editJson;
	}

	/**
	 * 模糊检索学校
	 * 
	 * @param key
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/key")
	public Object findSchoolsByKey(String key, HttpServletRequest request) {
		EditJson editJson = schoolService.findSchoolsByKey(key);
		return editJson;
	}

	/**
	 * 初始化学校主页数据
	 * 
	 * @param schoolId
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/initHtml")
	public Object findInitHtml(String schoolId, Page page, HttpServletRequest request) {
		Object object = schoolService.findInitHtml(schoolId, page, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "/initSchoolLabelHtml")
	public Object findSchoolLabelInitHtml(String labelId, String pointId, String schoolId,
			Page page, HttpServletRequest request) {
		Object object = schoolService.findSchoolLabelInitHtml(labelId, pointId, schoolId, page,
				request);
		return object;
	}

	@RequestMapping(value = "/home/{schoolId}", method = RequestMethod.GET)
	public String findJsp(@PathVariable("schoolId") String schoolId, String pointId,
			HttpServletRequest request, HttpServletResponse response) {
		School school = (School) schoolService.findSchoolById(schoolId).getMap().get("school");
		if (school == null) {
			return "jsp/404";
		}
		Point point = labelService.findPointByPointId(pointId);
		if (point != null) {
			try {
				response.sendRedirect("/school/home/" + schoolId + "/" + point.getLabelId()
						+ "?pointId=" + pointId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (request.getSession().getAttribute("student") == null) {
			request.setAttribute("studentJSON", false);
			request.setAttribute("loginningstudent", null);
		} else {
			Student student = (Student) request.getSession().getAttribute("student");
			request.setAttribute("studentJSON", JSONObject.fromObject(student).toString());
			request.setAttribute("loginningstudent", student);
		}
		request.setAttribute("school", school);
		return "jsp/school/school";

	}

	@RequestMapping(value = "/home/{schoolId}/{labelId}", method = RequestMethod.GET)
	public String findJsp(@PathVariable("schoolId") String schoolId,
			@PathVariable("labelId") String labelId, String pointId, HttpServletRequest request) {
		School school = (School) schoolService.findSchoolById(schoolId).getMap().get("school");
		List label = labelService.findlabelByLabelIdAndSchoolId(schoolId, labelId);
		Point point = labelService.findPointByPointId(pointId);
		if (school == null || label.size() == 0) {
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
		request.setAttribute("school", school);
		request.setAttribute("label", label.get(0));
		request.setAttribute("point", point);
		return "jsp/school/schoolLabel";

	}

	@ResponseBody
	@RequestMapping(value = "/students")
	public Object findStudents(String schoolId, String labelId, String pointId, String stType,
			String prType, String filter, Page page, HttpServletRequest request) {
		Object object = schoolService.findStudents(schoolId, labelId, pointId, stType, filter,
				page, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "/products")
	public Object findProducts(String schoolId, String labelId, String pointId, String stType,
			String prType, Page page, HttpServletRequest request) {
		Object object = schoolService.findProducts(schoolId, labelId, pointId, stType, prType,
				page, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "/labels")
	public Object findLabels(String schoolId, Page page, HttpServletRequest request) {
		List labels = labelService.findLabelsBySchoolId(schoolId, page);
		Map map = new HashMap();
		map.put("labels", labels);
		return map;
	}

	/**
	 * 查询学校根据省份
	 * 
	 * @param province
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findSchoolsByProvince")
	public Object findSchoolsByProvince(String provinceId, HttpServletRequest request) {
		Object object = schoolService.findSchoolsByProvince(provinceId, request);
		return object;
	}
}
