package com.itsum.jc.extract.gcw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.itsum.jc.extract.gcw.dto.XXJ_PriceDTO;

import static com.itsum.jc.service.BaseService.savePageRecord;

/**
 * 信息价查询工具类
 * @author Jason Ma
 *
 */
public class XxjFetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(XxjFetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	//提取城市名称
	private static Pattern city_p = Pattern.compile("^(.+?)\\d{4}.*");
	
	//提取全部信息价格
	public static List<XXJ_PriceDTO> fetchSearch_jcCrawler(String sessionId, String year, String month, int pageNum,int pageAction){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;

		List<XXJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=pageAction; pageNo <= pageNum; pageNo++){
			try {
				Sleep.longSleep();
				List<XXJ_PriceDTO> subs = fetchSearchPage(sessionId, year, month, pageNo);
				if(subs != null && subs.size() > 0){
					links.addAll(subs);
					savePageRecord(pageNo,PageConstant.GCW_XXJ);
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
	public static List<XXJ_PriceDTO> fetchSearchPage(String sessionId, String year, String month, int pageNo) throws IOException{
//		String page_url = "http://www.gldjc.com/xxj/so.html?locationId=&locName=全国&keyword=&year=" + year + "&period="+ month + "&currentPage=" + pageNo;
		String page_url = "http://www.gldjc.com/xxj/so.html?locationId=&locName=全国&keyword=&currentPage=" + pageNo;
		//period=13 近3个月  period=14 全部
		Connection conn = Jsoup.connect(page_url);
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		//主页面内容
		Document doc = conn.get();
		Sleep.longSleep();
		Document jdoc = Jsoup.parse(doc.html());
		Element e_root = jdoc.select(".wdsc > table").first();
		if(e_root != null){
			Elements trs = e_root.select("tr:gt(0)");
			if(trs != null && trs.size() > 0){
				List<XXJ_PriceDTO> links = new ArrayList<>();
				for(Element tr : trs){
					String name = JSoupSelectU.owntext(tr, "td:eq(1) p");
					String guige = JSoupSelectU.owntext(tr, "td:eq(2) p");
					String danwei = JSoupSelectU.owntext(tr, "td:eq(3) p");
					String chushuijia = JSoupSelectU.owntext(tr, "td:eq(4) p");
					String hanshuijia = JSoupSelectU.owntext(tr, "td:eq(5) p");
					String shuilv = JSoupSelectU.owntext(tr, "td:eq(7) p");
					String beizhu = JSoupSelectU.owntext(tr, "td:eq(8) p");
					String zhuanye = JSoupSelectU.owntext(tr, "td:eq(9) p");
					String diqu = JSoupSelectU.owntext(tr, "td:eq(10) p a");
					
					String cityName = "";
					//提取材质
					Matcher city_matcher = city_p.matcher(diqu);
					if(city_matcher.matches()){
						cityName = city_matcher.group(1);
					}
					String categoryname = "";
					String dataValue = tr.select(".infoPriceSCBox a").attr("data-value");//获取a标签下的data-value属性
					//类别名称有时候会出现在_2当中
					Pattern category_p1 = Pattern.compile("^.+?categoryname_1\":\\[\"(.*)\"\\].*");
					Pattern category_p2 = Pattern.compile("^.+?categoryname_2\":\\[\"(.*)\"\\].*");
					Matcher categorynameMatch1 = category_p1.matcher(dataValue);
					Matcher categorynameMatch2 = category_p2.matcher(dataValue);
					if(categorynameMatch1.matches()){
						categoryname = categorynameMatch1.group(1);
					}
					if(categorynameMatch2.matches()){
						categoryname = categorynameMatch2.group(1);
						if(categoryname.contains("、")){
							categoryname = categoryname.split("、")[1];
						}
					}
					XXJ_PriceDTO price = new XXJ_PriceDTO();
					price.setCategoryName(categoryname);
					price.setName(name);//材料名称
					price.setSpec(guige);//规格型号
					price.setUnit(danwei);//单位
					price.setTaxPriceMarket(chushuijia);//除税价
					price.setTaxPrice(hanshuijia);//含税价
					price.setTaxRate(shuilv);//税率
					price.setRemark(beizhu);//备注
					price.setMajor(zhuanye);//专业
					price.setCityPeriod(diqu);//地区期数
					price.setCityName(cityName);//城市名称
					
					links.add(price);
				}
				logger.info("广材网---信息价页面---爬取详情数据第"+pageNo+"页成功");
				return links;
			}
		}
		return null;
	}





	//jc-web项目用
	public static List<XXJ_PriceDTO> fetchSearch(String sessionId, String year, String month, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;

		List<XXJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				Sleep.longSleep();
				List<XXJ_PriceDTO> subs = fetchSearchPage(sessionId, year, month, pageNo);
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

}

