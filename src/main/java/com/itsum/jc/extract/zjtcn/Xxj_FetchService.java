package com.itsum.jc.extract.zjtcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itsum.jc.Sleep;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.zjtcn.dto.ZJT_CitySiteDTO;
import com.itsum.jc.extract.zjtcn.dto.ZJT_CitySite_AreaDTO;
import com.itsum.jc.extract.zjtcn.dto.ZJT_XXJ_PriceDTO;

import jmini.utils.Str;

/**
 * 造价通 - 信息价 - 获取信息价格
 * 
 * @author Jason Ma
 *
 */
public class Xxj_FetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(Xxj_FetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static Pattern id_p = Pattern.compile(".*gov/info_(\\d*).html$");
	
	public static void main(String[] args) throws IOException {
		ZJT_CitySiteDTO citySite = new ZJT_CitySiteDTO();
		citySite.setCode("hubei");
		citySite.setName("湖北");
		ZJT_CitySite_AreaDTO area = new ZJT_CitySite_AreaDTO();
		area.setCode("548");
		area.setName("武汉市");
		
		List<String> pList = fetchSearch_jcCrawler("4f99c7f5-ec00-4bf1-851f-9777caef6a26", citySite, area, "", 1);
		if(pList != null && pList.size()> 0){
			System.out.println(pList.size());
			for(String p : pList){
				System.out.println(p);
			}
		}
	}
	
	//提取全部信息价格
	public static List<String> fetchSearch_jcCrawler(String sessionId, ZJT_CitySiteDTO citySite, ZJT_CitySite_AreaDTO area, String industry, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		List<String> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<String> subs = fetchSearchPage_jcCrawler(sessionId, citySite, area, industry, pageNo);
				if(subs != null && subs.size() > 0){
					links.addAll(subs);
				}else{
					//无数据就不再继续
					break;
				}
			} catch (IOException e) {
				//如果发生异常，就不继续进行获取
				break;
			}
		}
		return links;
	}
	
	/**
	 * 提取列表数据
	 * @param sessionId
	 * @param citySite
	 * @param area
	 * @param industry 建筑工程-  交通工程-jtgc 水利工程-slgc 铁路轨道-tlgd 人防工程-rfgc
	 * @param pageNo
	 * @return
	 * @throws IOException
	 */
	public static List<String> fetchSearchPage_jcCrawler(String sessionId, ZJT_CitySiteDTO citySite, ZJT_CitySite_AreaDTO area, String industry, int pageNo) throws IOException{
		String q_industry = "";
		if(!Str.isEmpty(industry)){
			q_industry = industry + "_";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("https://")
		.append(citySite.getCode()).append(".zjtcn.com")
		.append("/gov/")
		.append(q_industry)
		.append("c").append(area.getCode())
		.append("_cs_d_t_p").append(pageNo)
		.append(".html");
		
		Connection conn = Jsoup.connect(sb.toString());
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
		Elements es = doc.select("#listbody > tbody > tr.table2");
		if(es != null && es.size() > 0){
			List<String> paricList = new ArrayList<>();
			for(Element e : es){
				String href = e.select("td:eq(0) a").first().attr("href");
				if(!Str.isEmpty(href)){
					//提取id
					Matcher id_matcher = id_p.matcher(href);
					if(id_matcher.matches()){
						String id = id_matcher.group(1);
						paricList.add(id);
					}
				}
			}
			return paricList;
		}
		return null;
	}










	//提取全部信息价格
	public static List<ZJT_XXJ_PriceDTO> fetchSearch(String sessionId, ZJT_CitySiteDTO citySite, ZJT_CitySite_AreaDTO area, String industry, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		List<ZJT_XXJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<ZJT_XXJ_PriceDTO> subs = fetchSearchPage(sessionId, citySite, area, industry, pageNo);
				if(subs != null && subs.size() > 0){
					links.addAll(subs);
				}else{
					//无数据就不再继续
					break;
				}
			} catch (IOException e) {
				//如果发生异常，就不继续进行获取
				break;
			}
		}
		return links;
	}

	/**
	 * 提取列表数据
	 * @param sessionId
	 * @param citySite
	 * @param area
	 * @param industry 建筑工程-  交通工程-jtgc 水利工程-slgc 铁路轨道-tlgd 人防工程-rfgc
	 * @param pageNo
	 * @return
	 * @throws IOException
	 */
	public static List<ZJT_XXJ_PriceDTO> fetchSearchPage(String sessionId, ZJT_CitySiteDTO citySite, ZJT_CitySite_AreaDTO area, String industry, int pageNo) throws IOException{
		String q_industry = "";
		if(!Str.isEmpty(industry)){
			q_industry = industry + "_";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("https://")
				.append(citySite.getCode()).append(".zjtcn.com")
				.append("/gov/")
				.append(q_industry)
				.append("c").append(area.getCode())
				.append("_cs_d_t_p").append(pageNo)
				.append(".html");

		Connection conn = Jsoup.connect(sb.toString());
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
		Elements es = doc.select("#listbody > tbody > tr.table2");
		if(es != null && es.size() > 0){
			List<ZJT_XXJ_PriceDTO> paricList = new ArrayList<>();
			for(Element e : es){
				String href = e.select("td:eq(0) a").first().attr("href");
				if(!Str.isEmpty(href)){
					//提取id
					Matcher id_matcher = id_p.matcher(href);
					if(id_matcher.matches()){
						String id = id_matcher.group(1);
						if(!Str.isEmpty(id)){
							List<ZJT_XXJ_PriceDTO> single_pList = Xxj_FetchByIdService.fetchDetail(sessionId, id, citySite.getCode(), citySite.getName(), area.getName());
							if(single_pList!=null && single_pList.size()>0){
								paricList.addAll(single_pList);
							}
						}
					}
				}
			}
			return paricList;
		}
		return null;
	}

}

