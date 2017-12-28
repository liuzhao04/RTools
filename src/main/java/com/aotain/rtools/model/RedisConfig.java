package com.aotain.rtools.model;

/**
 * Redis连接配置
 * 
 * @author liuz@aotian.com
 * @date 2017年12月28日 下午5:35:12
 */
public class RedisConfig {
	private String ipStrs;
	private String portStrs;

	public RedisConfig(String ipStrs, String portStrs) {
		this.ipStrs = ipStrs;
		this.portStrs = portStrs;
	}

	private String name;
	private int id;

	public String getIpStrs() {
		return ipStrs;
	}

	public void setIpStrs(String ipStrs) {
		this.ipStrs = ipStrs;
	}

	public String getPortStrs() {
		return portStrs;
	}

	public void setPortStrs(String portStrs) {
		this.portStrs = portStrs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RedisConfig [ipStrs=" + ipStrs + ", portStrs=" + portStrs + ", name=" + name + ", id=" + id + "]";
	}

}
