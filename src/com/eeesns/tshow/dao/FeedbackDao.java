package com.eeesns.tshow.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.eeesns.tshow.entity.Feedback;

@Component
public class FeedbackDao {
	@Resource
	BaseDao baseDao;

	public Object saveFeedback(Feedback feedback) {
		baseDao.saveEntity(feedback);
		return feedback;
	}
}
