package com.aotain.rtools.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * 界面工具箱
 * 
 * @author liuz@aotian.com
 * @date 2017年12月28日 下午2:21:44
 */
public class FrameUtils {
	
	/**
	 * 获取屏幕大小
	 * @return
	 */
	public static Dimension getScreenDimension(){
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * 将src大小的组件，居中放置在target上的location点(左上角坐标)
	 * @param src
	 * @param target
	 * @return
	 */
	public static Point calcLoaction(Dimension src,Dimension target){
		Point point = new Point();
		point.x = 0;
		point.y = 0;
		if(src == null || target == null){
			return point;
		}
		int tmpX = (int) ((target.width - src.width) / 2); 
		int tmpY = (int) ((target.height - src.height) / 2); 
		if(tmpX < 0 || tmpY < 0 ){
			return point;
		}
		point.x = tmpX;
		point.y = tmpY;
		return point;
	}
}
