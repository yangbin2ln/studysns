package com.eeesns.tshow.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.common.json.Message;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.entity.Privilege;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.StudentService;

@Component
@Aspect
public class LogAop {
	@Resource
	StudentService studentService;
	@Resource
	BaseDao baseDao;

	@Around("within(com.eeesns.tshow.controller..*)")
	public Object saveStudentActLog(ProceedingJoinPoint pj) throws Throwable {
		System.out.println("logAop");
		System.out.println(((MethodSignature) pj.getSignature()).getMethod().getName());
		String methodName = ((MethodSignature) pj.getSignature()).getMethod().getName();
		EditJson editJson = new EditJson();
		boolean flag = accessNext(methodName, pj, editJson);
		if (!flag) {
			editJson.setSuccess(false);
			editJson.getMess().setState("noprivilege");
			return editJson;
		}
		Object retVal;
		try {
			retVal = pj.proceed();
		} catch (org.hibernate.StaleObjectStateException e) {
			baseDao.getSession().clear();
			System.out.println("同步异常2");
			retVal = pj.proceed();
			e.printStackTrace();
		}
		return retVal;
	}

	private boolean accessNext(String methodName, ProceedingJoinPoint pj, EditJson editJson) {
		// 记录日志
		String mapping = null;// url请求
		HttpServletRequest request = null;
		Object[] args = pj.getArgs();
		for (Object obj : args) {
			if (obj instanceof HttpServletRequest) {
				request = (HttpServletRequest) obj;
				String contextPath = request.getContextPath();
				String url = request.getRequestURL().toString();
				if (contextPath.equals("")) {
					int indexOf = url.indexOf("/", 7);
					System.out.println(url.substring(indexOf + 1));
					mapping = url.substring(indexOf + 1);
				} else {
					int indexOf = url.indexOf(contextPath);
					System.out.println(url.substring(indexOf + contextPath.length() + 1));
					mapping = url.substring(indexOf + contextPath.length() + 1);
				}
			}
		}
		studentService.saveStudentLog((request.getSession().getAttribute("student") == null ? null
				: ((Student) request.getSession().getAttribute("student")).getStudentId()),
				methodName, mapping);
		// 特殊处理
		if (teshuchuli(methodName)) {
			System.out.println("放行");
		} else {
			// 其他的find方法放行
			if (methodName.startsWith("find")) {
				System.out.println("放行");
			} else {
				// 权限过滤
				boolean boo = filterUrl(request, mapping, editJson);
				if (!boo) {
					return false;
				}

			}
		}
		return true;
	}

	private boolean teshuchuli(String methodName) {
		ArrayList<String> arr = new ArrayList();
		arr.add("saveStudent");
		arr.add("login");
		arr.add("destoryLogin");
		arr.add("saveRegister");
		arr.add("toLogin");
		for (String str : arr) {
			if (str.equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	private boolean filterUrl(HttpServletRequest request, String mapping, EditJson editJson) {
		/* start 判断请求，类似拦截器 */
		if (request.getSession().getAttribute("student") == null) {
			editJson.getMess().setMessage("请先登录");
			return false;
		}
		Student student = (Student) request.getSession().getAttribute("student");
		List<Privilege> privileges = student.getRole().getPrivileges();
		System.out.println("mapping" + mapping);
		for (Privilege pv : privileges) {
			System.out.println(pv.getUrl());
			if (pv.getUrl() != null && pv.getUrl().equals(mapping)) {
				return true;
			}
		}
		if (student.getPerfectInfo().equals("N")) {
			editJson.getMess().setMessage("请先完善信息");
		} else {
			editJson.getMess().setMessage("无权限");
		}
		/* end 判断请求，类似拦截器 */
		return false;
	}
}
