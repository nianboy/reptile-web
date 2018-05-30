package com.itsum.jc.extract.zjtcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itsum.jc.Sleep;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.zjtcn.dto.ZJT_CitySiteDTO;

/**
 * 造价通 - 获取所有城市站点信息
 * 
 * @author Jason Ma
 *
 */
public class CitySite_FetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(CitySite_FetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static ObjectMapper om = new ObjectMapper();
	
//	public static void main(String[] args) throws IOException {
//		List<ZJT_CitySiteDTO> pList = fetchAll("4f99c7f5-ec00-4bf1-851f-9777caef6a26");
//		if(pList != null && pList.size()> 0){
//			System.out.println(pList.size());
//			for(ZJT_CitySiteDTO p : pList){
//				System.out.println(p);
//			}
//		}
//	}
	
	/**
	 * 提取详情页面数据
	 * @return
	 * @throws IOException 
	 */
	public static List<ZJT_CitySiteDTO> fetchAll(String sessionId) throws IOException{
		Connection conn = Jsoup.connect("https://c2.zjtcn.com/js/zjt/commonJS/city_site.js");
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		conn.ignoreContentType(true);
		//单页面内容提取
		conn.get();
		int statusCode = conn.response().statusCode();
		if(statusCode != 200){
			return null;
		}
		String js_str = conn.response().body();
		js_str = js_str.replace("var siteProvince=", "");
		js_str = js_str.replace(";", "");
		
		JsonNode rootNode = om.readTree(js_str);
		Sleep.longSleep();
		if(rootNode != null && rootNode.size() > 0){
			List<ZJT_CitySiteDTO> csList = new ArrayList<>();
			for(JsonNode node : rootNode){
				ZJT_CitySiteDTO cs = new ZJT_CitySiteDTO();
				cs.setName(node.get(0).asText());
				cs.setCode(node.get(1).asText());
				cs.setLink("http://" + cs.getCode() + ".zjtcn.com" );
				csList.add(cs);
			}
			return csList;
		}
		return null;
	}

}

