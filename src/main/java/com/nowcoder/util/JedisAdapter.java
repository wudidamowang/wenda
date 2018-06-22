package com.nowcoder.util;

import redis.clients.jedis.Jedis;

public class JedisAdapter {
	
	public static void print(int index,String val) {
		System.out.println(String.format("%d,%s", index,val));
	}
	
	public static void main(String[] args) {
		Jedis jedis=new Jedis();
		jedis.flushDB();
		jedis.set("hello", "world");
		print(1,jedis.get("hello"));
		jedis.rename("hello", "newHello");
		print(2,jedis.get("newHello"));
		
	}
}
