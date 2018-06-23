package com.nowcoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;

@Service
public class LikeService {

	@Autowired
	JedisAdapter jedisAdapter;

	public long like(int userId, int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.sadd(likeKey, String.valueOf(entityId));

		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
		jedisAdapter.srem(disLikeKey, String.valueOf(entityId));

		return jedisAdapter.scard(likeKey);
	}

	public long disLike(int userId, int entityType, int entityId) {
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
		jedisAdapter.sadd(disLikeKey, String.valueOf(entityId));

		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.srem(likeKey, String.valueOf(entityId));

		return jedisAdapter.scard(likeKey);
	}
	
	public int getLikeStatus(int userId, int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		if(jedisAdapter.sismember(likeKey, String.valueOf(entityId))) {
			return 1;
		}
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
		return jedisAdapter.sismember(disLikeKey, String.valueOf(entityId))?-1:0;
	}
	
	public long getLikeCount(int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		return jedisAdapter.scard(likeKey);
	}
}
