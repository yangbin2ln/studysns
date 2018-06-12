package com.eeesns.tshow.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.ContendDao;
import com.eeesns.tshow.entity.Student;

@Service
public class CeshiService {
	@Resource
	BaseDao baseDao;

	public Object updateStudent(HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		System.out.println(student.getAge());
		student.setAge(student.getAge()+1);
		System.out.println(student.hashCode());
		baseDao.updateEntity(student);
		System.out.println(student.getAge());
		System.out.println(student.hashCode());
		return student;
	}

}
