package com.eeesns.tshow.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.NotationReply;
import com.eeesns.tshow.entity.NotationReplyReply;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.TitleReply;
import com.eeesns.tshow.service.ProductService;

@Controller
@RequestMapping(value = "product")
public class ProductController {
	@Resource
	private ProductService productService;

	/**
	 * 保存作品
	 * 
	 * @param pro
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveProduct")
	public Object saveProduct(Product pro, HttpServletRequest request) {
		Object object = productService.saveProduct(pro, request);
		return object;

	}

	/**
	 * 修改作品
	 * 
	 * @param pro
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateProduct")
	public Object updateProduct(Product pro, HttpServletRequest request) {
		Object object = productService.updateProduct(pro, request);
		return object;

	}

	/**
	 * 保存作品的一个打点
	 * 
	 * @param titleReply
	 * @param notationContent
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveTitleReply")
	public Object saveTitleReply(TitleReply titleReply, String notationContent,
			HttpServletRequest request) {
		Object object = productService.saveTitleReply(titleReply, notationContent, request);
		return object;
	}

	/**
	 * 删除作品
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteProduct")
	public Object deleteProduct(Product product, HttpServletRequest request) {
		Object object = productService.deleteProduct(product, request);
		return object;
	}

	/**
	 * 保存点的一级回复
	 * 
	 * @param notationReply
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveNotationReply")
	public Object saveNotationReply(NotationReply notationReply, HttpServletRequest request) {
		System.out.println(notationReply.getProductContent());
		Object object = productService.saveNotationReply(notationReply, request);
		return object;

	}

	@ResponseBody
	@RequestMapping(value = "/saveNotationReplyReply")
	public Object saveNotationReplyReply(NotationReplyReply notationReplyReply,
			HttpServletRequest request) {
		Object object = productService.saveNotationReplyReply(notationReplyReply, request);
		return object;

	}

	/**
	 * 作品点赞
	 * 
	 * @param productId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savePraiseProduct")
	public Object savePraiseProduct(String productId, HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.savePraiseProduct(productId, request);
			return state;
		}
	}

	/**
	 * 收藏作品
	 * 
	 * @param productId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveCollectionProduct")
	public Object saveCollectionProduct(String productId, HttpServletRequest request) {
		Object object = productService.saveCollectionProduct(productId, request);
		return object;
	}

	/**
	 * 为作品投票
	 * 
	 * @param productId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveVoteProduct")
	public Object saveVoteProduct(String productId, HttpServletRequest request) {
		Object object = productService.saveVoteProduct(productId, request);
		return object;
	}

	/**
	 * 一级评论点赞(意见)
	 * 
	 * @param replyId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savePraiseNotationReply")
	public Object savepraiseNotationReply(String replyId, HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.savepraiseNotationReply(replyId, request);
			return state;
		}

	}

	/**
	 * @param id
	 *            一级评论的id
	 * @param type
	 *            1代表一级评论
	 * @param saveOrDelete
	 *            s代表采纳 d代表取消采纳
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrDeleteAdopt")
	public Object saveOrDeleteAdopt(String id, String type, String saveOrDelete,
			HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.saveOrDeleteAdopt(id, type, saveOrDelete, request);
			return state;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/savePraiseNotationReplyReply")
	public Object savepraiseNotationReplyReply(String replyReplyId, HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.savepraiseNotationReplyReply(replyReplyId, request);
			return state;
		}

	}

	@ResponseBody
	@RequestMapping(value = "/deletePraiseNotationReplyReply")
	public Object deletePraiseNotationReplyReply(String replyReplyId, HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.deletePraiseNotationReplyReply(replyReplyId, request);
			return state;
		}

	}

	@ResponseBody
	@RequestMapping(value = "/deletePraiseNotationReply")
	public Object deletePraiseNotationReply(String replyId, HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.deletePraiseNotationReply(replyId, request);
			return state;
		}

	}

	/**
	 * 作品取消点赞
	 * 
	 * @param productId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePraiseProduct")
	public Object deletePraiseProduct(String productId, HttpServletRequest request) {
		synchronized (request.getSession().getAttribute("student")) {// 当前用户再连续点击赞和取消赞时同步
			String state = productService.deletePraiseProduct(productId, request);
			return state;
		}

	}

	/**
	 * 取消收藏作品
	 * 
	 * @param productId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCollectionProduct")
	public Object deleteCollectionProduct(String productId, HttpServletRequest request) {
		String state = productService.deleteCollectionProduct(productId, request);
		return state;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteTitleReply")
	public Object deleteTitleReply(TitleReply tr, HttpServletRequest request) {
		Object object = productService.deleteTitleReply(tr, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteNotationReply")
	public Object deleteReply(NotationReply nr, HttpServletRequest request) {
		Object object = productService.deleteReply(nr, request);
		return object;
	}

	/**
	 * 删除二级评论
	 * 
	 * @param replyId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteNotationReplyReply")
	public Object deleteReplyReply(NotationReplyReply nrr, HttpServletRequest request) {
		Object object = productService.deleteReplyReply(nrr, request);
		return object;
	}

	/**
	 * @param productId
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findProduct")
	public Object findProduct(String productId, HttpServletRequest request) {
		Object object = productService.findProduct(productId, request);
		return object;
	}

	/**
	 * 根据版本系列号查询最新版本作品
	 * 
	 * @param productSeriesId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findProductBySeriesId")
	public Object findProductBySeriesId(String productSeriesId, HttpServletRequest request) {
		Object object = productService.findProductBySeriesId(productSeriesId, request);
		return object;
	}

	/**
	 * 查询作品的历史版本
	 * 
	 * @param productSeriesId
	 * @param version
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findProductVersion")
	public Object findProductVersion(String productSeriesId, Integer version, HttpServletRequest res) {
		Object object = productService.findProductVersion(productSeriesId, version, res);
		return object;
	}

	/**
	 * 查询作品的所有点
	 * 
	 * @param productId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findTtileReply")
	public Object findTtileReply(String productId, HttpServletRequest request) {
		Object obj = productService.findTtileReply(productId, request);
		return obj;
	}

	/**
	 * 查看对标题的一级评论
	 * 
	 * @param replyId
	 *            标题发起者评论的id
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findNotationReply")
	public Object findNotationReply(String replyId, HttpServletRequest request) {
		Object object = productService.findNotationReply(replyId, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "/findNotationReplyReply")
	public Object findNotationReplyReply(String notationReplyId, HttpServletRequest request) {
		Object obj = productService.findNotationReplyReply(notationReplyId, request);
		return obj;
	}

	/**
	 * 标签常用标题的推荐
	 * 
	 * @param labelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findReplyPoint")
	public Object findRecommendReplyPoint(String labelId, HttpServletRequest request) {
		Object obj = productService.findRecommendReplyPoint(labelId, request);
		return obj;
	}

}
