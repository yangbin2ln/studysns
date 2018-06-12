package com.eeesns.tshow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.service.ProfessionService;
import com.eeesns.tshow.service.SchoolService;

@Controller
@RequestMapping(value = "profession")
public class ProfessionController {
	@Resource
	private ProfessionService professionService;

	@ResponseBody
	@RequestMapping(value = "key")
	public Object findProfessionListByKey(String key, HttpServletRequest request) {
		Object object = professionService.findProfessionListByKey(key, request);
		return object;

	}

}
