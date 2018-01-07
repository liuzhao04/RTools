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
	public static RedisOP createRedisOp(String host, int port) {
		return new RedisOP(host, port);
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
	public static RedisClusterOP createRedisClusterOP(String hosts, String ports) {
		return new RedisClusterOP(hosts, ports);
	}

	/**
	 * 创建Redis实例
	 * @param hosts
	 * @param ports
	 * @return
	 */
	public static IRedisOP createIRedisOP(String hosts, String ports) {
		if (hosts.contains(",")) {
			return new RedisClusterOP(hosts, ports);
		} else {
			return new RedisOP(hosts, Integer.parseInt(ports));
		}
	}
}
