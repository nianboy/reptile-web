package com.itsum.jc.extract.zjtcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itsum.jc.Sleep;
import com.itsum.jc.moudles.PageConstant;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.crawler.jsoup.JSoupSelectU;
import com.itsum.jc.extract.zjtcn.dto.ZJT_CKJ_PriceDTO;

/**
 * 造价通 - 参考价
 * 
 * @author Jason Ma
 *
 */
public class Ckj_FetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(Ckj_FetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static String root_url = "https://gd.zjtcn.com/";
	
	
	
//	public static void main(String[] args) throws IOException {
//		List<ZJT_CKJ_PriceDTO> pList = fetchSearch("bbc6a298-153d-4325-8134-7d7aef1f09ea", "2018" , 2);
//		if(pList != null && pList.size()> 0){
//			System.out.println(pList.size());
//			for(ZJT_CKJ_PriceDTO p : pList){
//				System.out.println(p);
//			}
//		}
//	}
	
	//提取全部信息价格
	public static List<String> fetchSearch_jcCralwer(String sessionId, String year, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		
		List<String> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<String> subs = fetchSearchPage_jcCrawler(sessionId, year, pageNo);
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
	
	//提取某页信息价
	public static List<String> fetchSearchPage_jcCrawler(String sessionId, String year, int pageNo) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(root_url).append("ration/")
		.append("d").append(year)
		.append("_t_p").append(pageNo).append(".html");
		
		Connection conn = Jsoup.connect(sb.toString());
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		Elements es = doc.select("#rationTable > tbody > tr:gt(0)");
		if(es != null && es.size() > 0){
			List<String> priceList = new ArrayList<>();
			for(Element e : es){
				Element a = e.select("td:eq(1) > a").first();
				if(a != null){
					String abs_url = a.absUrl("href");
					priceList.add(abs_url);
//					List<ZJT_CKJ_PriceDTO> innerPriceList = fetchDetail(sessionId, abs_url);
//					if(innerPriceList != null && innerPriceList.size() > 0){
//						priceList.addAll(innerPriceList);
//					}
				}
			}
			logger.info("造价通---参考价页面---爬取Spu链接数量第"+pageNo+"页");
			return priceList;
		}
		return null;
	}


	//jc-web项目调用
	public static List<ZJT_CKJ_PriceDTO> fetchSearch(String sessionId, String year, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;

		List<ZJT_CKJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<ZJT_CKJ_PriceDTO> subs = fetchSearchPage(sessionId, year, pageNo);
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

	//jc-web项目用
	//提取某页信息价
	public static List<ZJT_CKJ_PriceDTO> fetchSearchPage(String sessionId, String year, int pageNo) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(root_url).append("ration/")
				.append("d").append(year)
				.append("_t_p").append(pageNo).append(".html");

		Connection conn = Jsoup.connect(sb.toString());
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);

		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		Elements es = doc.select("#rationTable > tbody > tr:gt(0)");
		if(es != null && es.size() > 0){
			List<ZJT_CKJ_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				Element a = e.select("td:eq(1) > a").first();
				if(a != null){
					String abs_url = a.absUrl("href");
					List<ZJT_CKJ_PriceDTO> innerPriceList = fetchDetail(sessionId, abs_url);
					if(innerPriceList != null && innerPriceList.size() > 0){
						priceList.addAll(innerPriceList);
					}
				}
			}
			return priceList;
		}
		return null;
	}


	
	/**
	 * 提取详情页面数据
	 * @return
	 * @throws IOException 
	 */
	public static List<ZJT_CKJ_PriceDTO> fetchDetail(String sessionId, String pageUrl) throws IOException{
		Connection conn = Jsoup.connect(pageUrl);
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		//防止进入无法访问页面
		int statusCode = conn.response().statusCode();
		if(statusCode != 200){
			logger.info("request fail statusCode:{}", statusCode);
			return null;
		}
		//获取产品公共信息
		String id = doc.select("#materialRef_id").first().val();
		String name = doc.select("#materialRef_name").first().val();
		String spec = doc.select("#materialRef_spec").first().val();
		String subcidNames = doc.select("#subcidNames").first().val();
		
		//获取价格列表信息
		Elements es = doc.select("#rationTable > tbody > tr:gt(0)");
		if(es != null && es.size() > 0){
			List<ZJT_CKJ_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				String unit = JSoupSelectU.owntext(e, "td:eq(2)");
				Element p_e = e.select("td:eq(3) input").first();
				String price = "";
				if(p_e != null){
					price = p_e.val();
				}
				String province = JSoupSelectU.owntext(e, "td:eq(5)");
				String source = JSoupSelectU.owntext(e, "td:eq(6)");
				String publishTime = JSoupSelectU.owntext(e, "td:eq(7)");
				
				ZJT_CKJ_PriceDTO p = new ZJT_CKJ_PriceDTO();
				p.setId(id);
				p.setName(name);
				p.setSpec(spec);
				p.setSubcidNames(subcidNames);
				p.setUnit(unit);
				p.setSource(source);
				p.setProvince(province);
				p.setPublishTime(publishTime);
				p.setPrice(price);
				
				priceList.add(p);
			}
			return priceList;
		}
		return null;
	}

}

