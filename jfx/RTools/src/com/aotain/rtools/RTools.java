package com.aotain.rtools;

import com.aotain.rtools.common.RedisUtils;

public class RTools {
	
	public static void main(String[] args) {
		String host1 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port1 = "7000,7000,7000,7000,7000,7000";
		String host2 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port2 = "8000,8000,8000,8000,8000,8000";
		final RedisUtils dao = new RedisUtils(host2,port2);
//		dao.foreachHash("jobstatus", "jobtype\\\":200", new IDoHashKey() {
		dao.foreachHash("jobstatus", "", new IDoHashKey() {
			@Override
			public void deal(String key, String value) {
				//if(value == null || "".equals(value.trim())){
				System.out.println(key +"->" +"("+dao.type("jobstatus")+") "+ value);
				//}
			}
		});
	}
}
