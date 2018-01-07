package com.aotain.rtools.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

/**
 * Redis单节点操作
 * 
 * @author Administrator
 *
 */
public class RedisOP implements IRedisOP {
	private Jedis jedis;

	public RedisOP(String host, int port) {
		jedis = new Jedis(host, port);
	}

	@Override
	public void initialization() {

	}

	@Override
	public List<String> keys(String keyName) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isBlank(keyName)) {
			list.addAll(jedis.keys("*"));
		} else {
			list.addAll(jedis.keys("*" + keyName + "*"));
		}
		return list;
	}

	@Override
	public String type(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return jedis.type(key);
	}

	@Override
	public String get(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return jedis.get(key);
	}

	@Override
	public void destory() {
		if (jedis != null) {
			jedis.close();
		}
	}

}
