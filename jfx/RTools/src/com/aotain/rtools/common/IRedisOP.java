package com.aotain.rtools.common;

import java.util.List;

/**
 * Redis操作接口
 * @author Administrator
 *
 */
public interface IRedisOP {
	/**
	 * 初始化
	 */
	public void initialization();
	
	/**
	 * 根据部分keyName模糊匹配出满足条件的所有key
	 * @param keyName 模糊匹配值，当keyName为空时，返回所有的key
	 * @return
	 */
	public List<String> keys(String keyName);
	
	/**
	 * 返回key的类型，包括RedisTypeConstant中定义的类型
	 * @param key
	 * @return key不存在或者为空时，返回null
	 */
	public String type(String key);
	
	/**
	 * 获取某个key的value，key为string类型时有效
	 * @param key
	 * @return
	 */
	public String get(String key);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hkeys(String key);
	
	/**
	 * 销毁操作对象
	 * @return
	 */
	public void destory();

	/**
	 * 获取HashValue
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key,String field);

	
}
