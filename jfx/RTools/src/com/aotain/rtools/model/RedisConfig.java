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
	private String cname;
	private int id;
	private boolean defaultSelected;

	public RedisConfig(){
		
	}
	
	public RedisConfig(String ipStrs, String portStrs) {
		this.ipStrs = ipStrs;
		this.portStrs = portStrs;
	}
	
	public void addIpAndPort(String ip,String port){
		this.ipStrs += "," + ip;
		this.portStrs += "," + port;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}

	public void setDefaultSelected(boolean defaultSelected) {
		this.defaultSelected = defaultSelected;
	}

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Override
	public String toString() {
		return "RedisConfig [ipStrs=" + ipStrs + ", portStrs=" + portStrs + ", cname=" + cname + ", id=" + id
				+ ", defaultSelected=" + defaultSelected + "]";
	}
	

	public int calcHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		result = prime * result + (defaultSelected ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((ipStrs == null) ? 0 : ipStrs.hashCode());
		result = prime * result + ((portStrs == null) ? 0 : portStrs.hashCode());
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedisConfig other = (RedisConfig) obj;
		if (cname == null) {
			if (other.cname != null)
				return false;
		} else if (!cname.equals(other.cname))
			return false;
		return true;
	}
	
	/**
	 * 对象是否被改变
	 * @param other
	 * @return 已改变返回true，否则返回false
	 */
	public boolean isChanged(RedisConfig other) {
		if(this.id != other.id){
			return true;
		}
		
		if(!cname.equals(other.cname)){
			return true; 
		}
		
		if(!ipStrs.equals(other.ipStrs)){
			return true;
		}
		
		if(!portStrs.equals(other.portStrs)){
			return true;
		}
		
		if(defaultSelected != other.defaultSelected){
			return true;
		}
		return false;
	}
	
	@Override
	public RedisConfig clone(){
		RedisConfig rc = new RedisConfig(this.ipStrs, this.portStrs);
		rc.cname = this.cname;
		rc.id = this.id;
		rc.defaultSelected = this.defaultSelected;
		return rc;
	}
}
