package com.nowcoder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class WebUtils {
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
	
	public static int ANONYMOUS_USERIL=3;
	public static int SYSTEM_USERIL=4;
	
	public static String getJSONString(int code,String msg) {
		JSONObject json=new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		return json.toJSONString();
	}
	
	public static String getJSONString(int code) {
		JSONObject json=new JSONObject();
		json.put("code", code);
		return json.toJSONString();
	}
}
