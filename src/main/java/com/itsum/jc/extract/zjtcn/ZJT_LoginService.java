package com.itsum.jc.extract.zjtcn;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.Sleep;

/**
 * 造价通 - 检查登录状态
 * 
 * @author Jason Ma
 *
 */
public class ZJT_LoginService {
	
	public static final Logger logger = LoggerFactory.getLogger(ZJT_LoginService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static ObjectMapper om = new ObjectMapper();
	
	public static void main(String[] args) throws IOException {
		boolean isValid = isValidSession("ed24506f-2be8-4e07-97ee-df09c26cded5");
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
		Connection conn = Jsoup.connect("https://gd.zjtcn.com/getCurrentUser.json?t=" + System.currentTimeMillis());
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		conn.ignoreContentType(true);
		//单页面内容提取
		try {
			conn.post();
			Sleep.longSleep();
			//获取json数据
			String json_str = conn.response().body();
			JsonNode rootNode = om.readTree(json_str);
			JsonNode node_currUser = rootNode.get("results").get("currUser");
			//获取到用户信息，表示sessionId有效
			if(!node_currUser.isNull()){
				return true;
			}
		} catch (IOException e) {
			logger.info("zjtcn access fail:", e);
		}
		return false; 
	}

}

