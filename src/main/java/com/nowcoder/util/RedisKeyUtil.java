package com.nowcoder.util;

public class RedisKeyUtil {
	private static String SPIT = ":";
	private static String BIZ_LIKE = "LIKE";
	private static String BIZ_DISLIKE = "DISLIKE";
	private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

	public static String getLikeKey(int entityType, int entityId) {
		return BIZ_LIKE + SPIT + String.valueOf(entityType) + SPIT + String.valueOf(entityId);
	}
	
	public static String getDisLikeKey(int entityType, int entityId) {
		return BIZ_DISLIKE + SPIT + String.valueOf(entityType) + SPIT + String.valueOf(entityId);
	}
	
	public static String getEventQueueKey() {
		return BIZ_EVENTQUEUE;
	}
}
