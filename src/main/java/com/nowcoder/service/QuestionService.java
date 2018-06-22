package com.nowcoder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;

@Service
public class QuestionService {
	@Autowired
	QuestionDAO questionDAO;
	
	@Autowired
	SensitiveService sensitiveService;

	public int addQuestion(Question question) {
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		
		question.setContent(sensitiveService.filter(question.getContent()));
		question.setTitle(sensitiveService.filter(question.getTitle()));

		return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
	}

	
	public Question selectById(int id) {
		return questionDAO.selectById(id);
	}
	
	public int updateCommentCount(int commentCount,int id) {
		return questionDAO.updateCommentCount(commentCount, id);
	}
	
	public List<Question> getLatestQuestions(int userId, int offset, int limit) {
		return questionDAO.selectLatestQuestions(userId, offset, limit);
	}
}
