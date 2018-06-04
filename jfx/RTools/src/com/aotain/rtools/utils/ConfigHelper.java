package com.aotain.rtools.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aotain.rtools.model.RedisConfig;
import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;

/**
 * 配置管理工具
 * 
 * @author Administrator
 *
 */
public class ConfigHelper {
	/** 配置文件目录 */
	public static String DIR_ROOT = "rtools";
	/** 系统用户目录 */
	public static String USER_HOME = System.getProperty("user.home");
	/** 配置文件 */
	public static String CLUSTER_CONFIG_FILE = "config.json";

	/**
	 * 读取配置
	 * 
	 * @param path
	 */
	public static List<RedisConfig> read() {
		String path = USER_HOME + File.separator + DIR_ROOT + File.separator + CLUSTER_CONFIG_FILE;
		File file = new File(path);
		if (!file.exists()) {
			// 先创建一个空配置文件
			save(Collections.<RedisConfig>emptyList());
		}

		FileInputStream fis = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = fis.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			String jstr = new String(bos.toByteArray(), "UTF-8");
			List<RedisConfig> list = new ArrayList<RedisConfig>();
			JSONArray ja = JSON.parseArray(jstr);
			for(int i = 0; i < ja.size(); i++) {
				JSONObject obj = ja.getJSONObject(i);
				RedisConfig rc = obj.toJavaObject(RedisConfig.class);
				rc.setCname(obj.getString("cname"));
				rc.setDefaultSelected(obj.getBooleanValue("defaultSelected"));
				rc.setId(obj.getIntValue("id"));
				list.add(rc);
			}
			
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * 保存配置
	 * 
	 * @param cfgs
	 */
	public static void save(List<RedisConfig> cfgs) {
		String path = USER_HOME + File.separator + DIR_ROOT + File.separator + CLUSTER_CONFIG_FILE;
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				System.err.println("用户目录创建失败：" + file.getParent());
				return;
			}
			System.out.println("用户目录创建成功：" + file.getParent());
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			String jstr = JSON.toJSONString(cfgs, true);
			fos.write(jstr.getBytes("UTF-8"));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取默认配置
	 * @param list
	 * @return
	 */
	public static RedisConfig getDefaultValue(List<RedisConfig> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		for (RedisConfig rc : list) {
			if (rc.isDefaultSelected()) {
				return rc;
			}
		}
		return list.get(0);
	}
	
	/**
	 * 获取索引
	 * @param list
	 * @param rc
	 * @return
	 */
	public static int getIndex(List<RedisConfig> list,RedisConfig rc) {
		return list.indexOf(rc);
	}

	public static void main(String[] args) {
		/* List<RedisConfig> rlist = new ArrayList<RedisConfig>(); 
		 String host0 =
		 "192.168.31.20"; String port0 = "8888"; RedisConfig rc0 = new
		 RedisConfig(host0, port0); rc0.setId(1); rc0.setCname("UbuntuRedis"); String
		 host1 =
		 "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		 String port1 = "7000,7000,7000,7000,7000,7000"; RedisConfig rc1 = new
		 RedisConfig(host1, port1); rc1.setId(2); rc1.setCname("研发集群"); String host2 =
		 "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		 String port2 = "8000,8000,8000,8000,8000,8000"; RedisConfig rc2 = new
		 RedisConfig(host2, port2); rc2.setId(3); rc2.setCname("测试集群"); rlist.add(rc0);
		 rlist.add(rc1); rlist.add(rc2); save(rlist);*/

		System.out.println(read());
	}

}
