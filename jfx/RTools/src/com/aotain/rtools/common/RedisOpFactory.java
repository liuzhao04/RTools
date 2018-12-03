package com.aotain.rtools.common;

/**
 * ReidsOp构造工厂
 * 
 * @author Administrator
 *
 */
public class RedisOpFactory {
	/**
	 * 构造单节点Redis操作工具
	 * 
	 * @param host
	 *            redis服务器地址
	 * @param port
	 *            端口
	 * @return
	 */
	public static RedisOP createRedisOp(String host, int port,String password) {
		return new RedisOP(host, port,password);
	}

	/**
	 * 构造集群Redis操作工具
	 * 
	 * @param hosts
	 *            redis服务器地址，用","号分隔
	 * @param ports
	 *            端口，用","号分隔
	 * @return
	 */
	public static RedisClusterOP createRedisClusterOP(String hosts, String ports,String password) {
		return new RedisClusterOP(hosts, ports,password);
	}

	/**
	 * 创建Redis实例
	 * @param hosts
	 * @param ports
	 * @return
	 */
	public static IRedisOP createIRedisOP(String hosts, String ports,String password) {
		if (hosts.contains(",")) {
			return new RedisClusterOP(hosts, ports,password);
		} else {
			return new RedisOP(hosts, Integer.parseInt(ports),password);
		}
	}
}
