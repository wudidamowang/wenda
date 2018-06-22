package com.nowcoder.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WebUtils;

@Controller
public class CommentController {
	
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	QuestionService questiontService;
	
	@Autowired
	HostHolder hostHolder;
	
	@RequestMapping(path= {"/addComment"},method=RequestMethod.POST)
	public String addComment(@RequestParam("questionId") int questionId,
								@RequestParam("content") String content) {
		try {
		Comment comment=new Comment();
		comment.setContent(content);
		if(hostHolder.getUser()!=null) {
			comment.setUserId(hostHolder.getUser().getId());
		}else {
			comment.setUserId(WebUtils.ANONYMOUS_USERIL);
		}
		comment.setCreatedDate(new Date());
		comment.setEntityType(EntityType.ENTITY_QUESTION);
		comment.setEntityId(questionId);
		commentService.addComment(comment);
		int count=commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
		questiontService.updateCommentCount(count, comment.getEntityId());
		}catch(Exception e) {
			logger.error("添加评论失败"+e.getMessage());
		}
		return "redirect:/question/"+questionId;
	}
}
