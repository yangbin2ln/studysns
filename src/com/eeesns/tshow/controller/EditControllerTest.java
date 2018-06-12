package com.eeesns.tshow.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eeesns.tshow.entity.NotationReply;
import com.eeesns.tshow.entity.Product;

@Controller
@RequestMapping(value="editControllerTest.do")
public class EditControllerTest {
	@RequestMapping(params="findEdit")
	public void findProduct(String productId,HttpServletRequest request,HttpServletResponse response){
		try {
			request.getRequestDispatcher("WEB-INF/controller.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
