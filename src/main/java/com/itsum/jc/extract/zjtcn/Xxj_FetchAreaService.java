package com.itsum.jc.extract.zjtcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itsum.jc.Sleep;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.zjtcn.dto.ZJT_CitySite_AreaDTO;

/**
 * 造价通 - 信息价 - 获取站点下说有报价城市
 * 
 * @author Jason Ma
 *
 */
public class Xxj_FetchAreaService {
	
	public static final Logger logger = LoggerFactory.getLogger(Xxj_FetchAreaService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	public static void main(String[] args) throws IOException {
		List<ZJT_CitySite_AreaDTO> pList = fetchAll("4f99c7f5-ec00-4bf1-851f-9777caef6a26", "http://gd.zjtcn.com/gov/c_cs_d_t_p1.html");
		if(pList != null && pList.size()> 0){
			System.out.println(pList.size());
			for(ZJT_CitySite_AreaDTO p : pList){
				System.out.println(p);
			}
		}
	}
	
	/**
	 * 提取详情页面数据
	 * @return
	 * @throws IOException 
	 */
	public static List<ZJT_CitySite_AreaDTO> fetchAll(String sessionId, String citySiteUrl) throws IOException{
		Connection conn = Jsoup.connect(citySiteUrl);
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		Document doc = conn.get();
		Sleep.longSleep();
		//单页面内容提取
		int statusCode = conn.response().statusCode();
		if(statusCode != 200){
			return null;
		}
		Elements es = doc.select("#areaUl a[tip]");
		if(es != null && es.size() > 0){
			List<ZJT_CitySite_AreaDTO> areaList = new ArrayList<>();
			for(Element e : es){
				ZJT_CitySite_AreaDTO area = new ZJT_CitySite_AreaDTO();
				area.setCode(e.attr("tip"));
				area.setName(e.ownText());
				areaList.add(area);
			}
			return areaList;
		}
		return null;
	}

}

