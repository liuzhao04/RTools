package com.aotain.rtools.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aotain.rtools.IDoHashKey;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * Redis操作工具箱
 * <p>负责连接redis或者redis集群</p>
 * 
 * 
 * @author liuz@aotian.com
 * @date 2017年12月8日 上午8:49:23
 */
public class RedisUtils {
	private JedisCluster jc = null;
	private Logger log = Logger.getLogger(RedisUtils.class);

	public RedisUtils(String hosts, String ports) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] portArr = ports.split(",");
		int i = 0;
		for (String host : hosts.split(",")) {
			jedisClusterNodes.add(new HostAndPort(host, Integer.parseInt(portArr[i])));
			i++;
		}
		jc = new JedisCluster(jedisClusterNodes);

	}
	
	/**
	 * 查询所有的key
	 * @param pattern
	 * @return
	 */
	public TreeSet<String> keys(String pattern){  
        TreeSet<String> keys = new TreeSet<>();  
        Map<String, JedisPool> clusterNodes = jc.getClusterNodes();  
        for(String k : clusterNodes.keySet()){  
            JedisPool jp = clusterNodes.get(k);  
            Jedis connection = jp.getResource();  
            try {  
                keys.addAll(connection.keys(pattern));  
            } catch(Exception e){  
            	e.printStackTrace();
            } finally{  
                connection.close();//用完一定要close这个链接！！！  
            }  
        }  
        return keys;  
    }
	
	/**
	 * 获取key的类型
	 * @param key
	 * @return
	 */
	public String type(String key) {
		return jc.type(key);
	}

	/**
	 * 遍历hash
	 * @param key
	 * @param condition
	 * @param ido
	 */
	public void foreachHash(String key, String condition, IDoHashKey ido) {
		if (StringUtils.isBlank(key)) {
			log.info("未指定key，无法遍历");
			return;
		}
		Map<String, String> map = jc.hgetAll(key);
		for (String id : map.keySet()) {
			String data = map.get(id);
			if (StringUtils.isBlank(condition) || data.contains(condition)) {
				ido.deal(id, data);
			}
		}
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		if (jc == null) {
			return;
		}
		try {
			jc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 模糊查询
	 * @param text
	 * @return
	 */
	public List<String> fuzzyKeys(String text) {
		List<String> keys = new ArrayList<String>();
		if(text == null || "".equals(text.trim())){
			keys.addAll(keys("*"));
		}
		keys.addAll(keys("*"+text+"*"));
		return keys;
	}

	public String get(String key) {
		return jc.get(key);
	}
}
