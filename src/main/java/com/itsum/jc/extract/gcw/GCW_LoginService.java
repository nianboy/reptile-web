package com.itsum.jc.extract.gcw;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.Sleep;

/**
 * 广材网 - 检查登录状态
 * 
 * @author Jason Ma
 *
 */
public class GCW_LoginService {
	
	public static final Logger logger = LoggerFactory.getLogger(GCW_LoginService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	public static void main(String[] args) throws IOException {
		boolean isValid = isValidSession("5c58e925-39b4-4684-ae54-c69b40110efa");
		if(isValid){
			System.out.println("有效");
		}else{
			System.out.println("无效");
		}
	}
	
	/**
	 * 检查sessionId 是否有效
	 * 
	 * @param sessionId
	 * @return
	 */
	public static boolean isValidSession(String sessionId){
		Connection conn = Jsoup.connect("http://www.gldjc.com/getUserInfo.ajax?callback=");
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		conn.ignoreContentType(true);
		//单页面内容提取
		try {
			conn.get();
			Sleep.longSleep();
			//获取json数据
			String json_str = conn.response().body();
			if(json_str.indexOf("accountName") > 0){
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			logger.info("zjtcn access fail:", e);
		}
		return false; 
	}

}

