package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WebUtils;

@Controller
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	QuestionService questionService;

	@Autowired
	UserService userService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	CommentService commentService;
	
	@Autowired
	LikeService likeService;

	@RequestMapping(value = "/question/add", method = RequestMethod.POST)
	@ResponseBody
	public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCreatedDate(new Date());
			question.setCommentCount(0);
			if (hostHolder.getUser() == null) {
				question.setUserId(WebUtils.ANONYMOUS_USERIL);
			} else {
				question.setUserId(hostHolder.getUser().getId());
			}
			if (questionService.addQuestion(question) > 0) {
				return WebUtils.getJSONString(0);
			}
		} catch (Exception e) {
			logger.error("增加题目失败" + e.getMessage());
		}
		return WebUtils.getJSONString(1, "失败");
	}

	@RequestMapping(value = "/question/{qid}")
	public String questionDetail(Model model, @PathVariable("qid") int qid) {
		Question question = questionService.selectById(qid);
		model.addAttribute("question", question);
		model.addAttribute("user", userService.getUser(question.getUserId()));
		List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> comments=new ArrayList<ViewObject>();
		for(Comment comment:commentList) {
			ViewObject vo=new ViewObject();
			vo.set("comment", comment);
			if(hostHolder.getUser()==null) {
				vo.set("liked", 0);
			}else {
				vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
			}
			vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
			vo.set("user", userService.getUser(comment.getUserId()));
			comments.add(vo);
		}
		model.addAttribute("comments", comments);
		return "detail";
	}

}
