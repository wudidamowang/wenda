package com.nowcoder.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.nowcoder.controller.QuestionController;

@Service
public class SensitiveService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

	private class TrieNode {
		private boolean end = false;

		private Map<Character, TrieNode> subNodes = new HashMap<Character, SensitiveService.TrieNode>();

		public void addSubNode(Character key, TrieNode node) {
			subNodes.put(key, node);
		}

		TrieNode getSubNode(Character key) {
			return subNodes.get(key);
		}

		boolean isKeywordEnd() {
			return end;
		}

		void setKeywordEnd(boolean end) {
			this.end = end;
		}
	}

	private TrieNode rootNode = new TrieNode();

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sensitivewords.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String lineTxt;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				addWord(lineTxt.trim());
			}
			bufferedReader.close();
		} catch (Exception e) {
			logger.error("读取文件错误" + e.getMessage());
		}

	}

	private void addWord(String lineTxt) {
		TrieNode tempNode = rootNode;

		for (int i = 0; i < lineTxt.length(); ++i) {
			Character c = lineTxt.charAt(i);
			if (isSymbol(c)) {
				continue;
			}
			TrieNode node = tempNode.getSubNode(c);
			if (node == null) {
				node = new TrieNode();
				tempNode.addSubNode(c, node);
			}
			tempNode = node;
			if (i == lineTxt.length() - 1) {
				node.setKeywordEnd(true);
			}
		}
	}

	public String filter(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		StringBuilder result = new StringBuilder();
		String replacement = "***";
		TrieNode tempNode = rootNode;
		int begin = 0;
		int position = 0;
		while (position < text.length()) {
			char c = text.charAt(position);

			if (isSymbol(c)) {
				if (tempNode == rootNode) {
					result.append(c);
					++begin;
				}
				++position;
				continue;
			}

			tempNode = tempNode.getSubNode(c);
			if (tempNode == null) {
				result.append(text.charAt(begin));
				position = begin + 1;
				begin = position;
				tempNode = rootNode;
			} else if (tempNode.isKeywordEnd()) {
				result.append(replacement);
				position = position + 1;
				begin = position;
				tempNode = rootNode;
			} else {
				++position;
			}
		}
		result.append(text.substring(begin));
		return result.toString();
	}

	private boolean isSymbol(char c) {
		int ic = (int) c;
		return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
	}

	public static void main(String[] args) {
		SensitiveService s = new SensitiveService();
		s.addWord("色情");
		s.addWord("赌博");
		s.filter("你好色情");
		System.out.println(s.filter("hello 你好色 情fds堵赌  的博"));
	}

}
