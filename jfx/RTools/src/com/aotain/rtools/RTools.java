package com.aotain.rtools;

import java.util.List;

import com.aotain.rtools.common.RedisUtils;

public class RTools {
	
	
	public static void main(String[] args) {
		String host1 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port1 = "7000,7000,7000,7000,7000,7000";
		String host2 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port2 = "8000,8000,8000,8000,8000,8000";
		final RedisUtils dao = new RedisUtils(host1,port1,"qsQq#3Mx");
		List<String> keyList = dao.fuzzyKeys("JCDM");
		keyList.forEach(key -> {System.out.println("key :"+key); try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
////		dao.foreachHash("jobstatus", "jobtype\\\":200", new IDoHashKey() {
//		dao.foreachHash("jobstatus", "", new IDoHashKey() {
//			@Override
//			public void deal(String key, String value) {
//				//if(value == null || "".equals(value.trim())){
//				System.out.println(key +"->" +"("+dao.type("jobstatus")+") "+ value);
//				//}
//			}
//		});
	}
}
