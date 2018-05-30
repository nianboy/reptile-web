package com.itsum.jc;

import com.anapp.fw.conf.Conf;

/**
 * 
 * 休眠工具类
 * 
 * @author Jason Ma
 *
 */
public class Sleep {
	
	/** 造价通 长休眠  */
	public static void longSleep(){
		if("prod".equals(Conf.p("env"))){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
	}
	/** 广材网 短休眠  */
	public static void shortSleep(){
		if("prod".equals(Conf.p("env"))){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {

			}
		}
	}


	public static void longlongSheep(){
		try {
			Thread.sleep(1800000);
		} catch (InterruptedException e) {

		}
	}

}
