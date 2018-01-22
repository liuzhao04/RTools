package com.aotain.rtools.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 集群操作工具
 * 
 * @author Administrator
 *
 */
public class RedisClusterOP implements IRedisOP {
	private JedisCluster jc = null;

	public RedisClusterOP(String hosts, String ports) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] portArr = ports.split(",");
		int i = 0;
		for (String host : hosts.split(",")) {
			jedisClusterNodes.add(new HostAndPort(host, Integer.parseInt(portArr[i])));
			i++;
		}
		jc = new JedisCluster(jedisClusterNodes,2000); // 2秒钟超时
	}

	@Override
	public void initialization() {

	}

	/**
	 * 实现keys正则匹配
	 * 
	 * @param pattern
	 * @return
	 */
	private TreeSet<String> matchKeys(String pattern) {
		TreeSet<String> keys = new TreeSet<>();
		Map<String, JedisPool> clusterNodes = jc.getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();// 用完一定要close这个链接！！！
			}
		}
		return keys;
	}

	@Override
	public List<String> keys(String keyName) {
		List<String> keys = new ArrayList<String>();
		if (keyName == null || "".equals(keyName.trim())) {
			keys.addAll(matchKeys("*"));
			return keys;
		}
		keyName = keyName.trim();
		keys.addAll(matchKeys("*" + keyName + "*"));
		return keys;
	}

	@Override
	public String type(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return jc.type(key);
	}

	@Override
	public String get(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return jc.get(key);
	}

	@Override
	public void destory() {
		if (jc == null) {
			return;
		}
		try {
			jc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> hkeys(String key) {
		List<String> keys = new ArrayList<String>();
		if (key == null || "".equals(key.trim())) {
			return keys;
		}
		key = key.trim();
		keys.addAll(jc.hkeys(key));
		return keys;
	}

	@Override
	public String hget(String key, String field) {
		return jc.hget(key, field);
	}

	@Override
	public boolean isConnectRight() {
		try {
			jc.set("ping-test-key", "1111");
			jc.get("ping-test-key");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
