package com.itsum.jc.extract.gcw;

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

import com.itsum.jc.crawler.jsoup.JSoupSelectU;
import com.itsum.jc.extract.gcw.dto.Gchq_AreaCodeDTO;
import com.itsum.jc.extract.gcw.dto.Gchq_PriceDTO;

import jmini.utils.Str;

/**
 * 
 * 钢材行情提取工具类
 * 
 * @author Jason Ma
 */
public class GchqFetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(GchqFetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
//	public static void main(String[] args) {
//		String sessionId = "8a9aa4b6-5672-44f1-bc4c-b49751604e06";
//		Gchq_AreaCodeDTO areaCode = new Gchq_AreaCodeDTO();
//		areaCode.setName("北京");
//		areaCode.setCode("110100");
//		String nameType = "qbgangcai";
//		String dateStr = "2018-4-3";
//		List<Gchq_PriceDTO> priceList = fetchPriceByDateAndName(sessionId, areaCode, nameType, dateStr, 10);
//		if(priceList != null && priceList.size() > 0){
//			System.out.println("Total:" + priceList.size());
//			for(Gchq_PriceDTO p : priceList){
//				System.out.println(p);
//			}
//		}
//	}
	
	/**
	 * 按日查询多页
	 * @param sessionId
	 * @param areaCode
	 * @param nameType  全部钢材-qbgangcai 全部管材-qbguancai 全部钢板-qbgangban
	 * @param dateStr
	 * @param pageNum
	 * @return
	 */
	public static List<Gchq_PriceDTO> fetchPriceByDateAndName(String sessionId, Gchq_AreaCodeDTO areaCode, String nameType, String dateStr, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		
		List<Gchq_PriceDTO> priceList = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<Gchq_PriceDTO> subs = fetchPriceByDateAndNamePage(sessionId, areaCode, nameType, dateStr, pageNo);
				if(subs != null && subs.size() > 0){
					priceList.addAll(subs);
				}else{
					//无数据就不再继续
					break;
				}
			} catch (IOException e) {
				//如果发生异常，就不继续进行获取
				break;
			}
		}
		return priceList;
	}
	
	/** 
	 * 查询 某日、某种 钢材的行情价格  单页
	 * @throws IOException 
	 */
	public static List<Gchq_PriceDTO> fetchPriceByDateAndNamePage(String sessionId, Gchq_AreaCodeDTO areaCode, String nameType, String dateStr, int pageNo) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("http://hangqing.gldjc.com/daily_price")
		.append("?areacode=").append(areaCode.getCode())
		.append("&name=").append(nameType)
		.append("&date=").append("2018-03-29")
		.append("&pagenum=").append(pageNo)
		.append("&key=gangcai");
		
		Connection conn = Jsoup.connect(sb.toString());
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		Document doc = conn.get();
		Sleep.longSleep();
		//提取页面时间，如果提供的dateStr无数据，则显示的是最后一次报价的时间数据，因此需要获取页面时间为准
		String priceDate = "";
		if(!Str.isEmpty(doc.select("#date").first().val())){{
            priceDate = doc.select("#date").first().val();
        }}
		Elements es = doc.select(".treChaDetTableBox > table > tbody > tr");
		if(es !=null && es.size() > 0){
			List<Gchq_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				Gchq_PriceDTO p = new Gchq_PriceDTO();
				p.setAreaCode(areaCode.getCode());
				p.setAreaName(areaCode.getName());
				p.setDate(priceDate);
				p.setName(JSoupSelectU.owntext(e, "td:eq(0) span"));
				p.setSpec(JSoupSelectU.owntext(e, "td:eq(1) span"));
				p.setTexture(JSoupSelectU.owntext(e, "td:eq(2) span"));
				p.setManufacturer(JSoupSelectU.owntext(e, "td:eq(3) span"));
				
				String price = JSoupSelectU.owntext(e, "td:eq(4) span");
				if(!Str.isEmpty(price)){
					price = price.replace("￥", "");
				}
				p.setPrice(price);
				p.setRemark(JSoupSelectU.owntext(e, "td:eq(8) span"));
				priceList.add(p);
			}
			logger.info("广材网---钢材行情页面---"+areaCode.getName()+"区域爬取数据第"+pageNo+"页成功");
			return priceList;
		}
		return null;
	}
	
}
