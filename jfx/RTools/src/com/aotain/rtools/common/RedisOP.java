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

	public RedisOP(String host, int port,String password) {
		jedis = new Jedis(host, port,10000);
		if(password != null){
			jedis.auth(password);
		}
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
			keyName = keyName.trim();
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

	@Override
	public List<String> hkeys(String key) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isBlank(key)) {
			return list;
		} else {
			key = key.trim();
			list.addAll(jedis.hkeys(key));
		}
		return list;
	}

	@Override
	public String hget(String key, String field) {
		return jedis.hget(key, field);
	}

	@Override
	public boolean isConnectRight() {
		try {
			jedis.ping();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
