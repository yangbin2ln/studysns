package com.eeesns.tshow.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.StudentService;
import com.eeesns.tshow.service.TalentListService;

/**
 * @author yb
 * 达人榜控制层
 *
 */
@Controller
@RequestMapping(value="talentList")
public class TalentListController {
	@Resource
	private TalentListService talentListService;
	@Resource
	private StudentService studentService;
	@ResponseBody
	@RequestMapping(value="/findStudent")
	public Object findStudent(Page page,String schoolId,String labelId){
		EditJson editJson = talentListService.findStudent(page,schoolId,labelId);
		return editJson;
	}
	/**
	 * 达人榜默认页面
	 * @param page
	 * @param schoolId
	 * @param labelId
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String findStudentList(Model model,HttpSession session){
		String schoolId="10056";
		String labelId="0301";
		//从session中获取参数
		Student student = (Student) session.getAttribute("student");
		if(student!=null){
			model.addAttribute("student", student);
			JSONArray jsonArray = JSONArray.fromObject(student.getSchool().getSchoolLabels());
			model.addAttribute("schoolLabels", jsonArray.toString());
		}else{
			Student stu = studentService.findStudentById("2011228122");
			JSONArray jsonArray = JSONArray.fromObject(stu.getSchool().getSchoolLabels());
			model.addAttribute("schoolLabels", jsonArray.toString());
			model.addAttribute("student", stu);
		}
		return "jsp/talentList/talentList";
	}
}
