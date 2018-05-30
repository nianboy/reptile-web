package com.itsum.jc.extract.gcw;

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
import com.itsum.jc.extract.gcw.dto.RGXJ_PriceDTO;

import static com.itsum.jc.service.BaseService.savePageRecord;

/**
 * 人工询价工具类
 * @author Jason Ma
 *
 */
public class RgxjFetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(RgxjFetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	//提取全部信息价格
	public static List<RGXJ_PriceDTO> fetchSearch_jcCrawler(String sessionId, int pageNum,int pageAction){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		
		List<RGXJ_PriceDTO> priceList = new ArrayList<>();
		for(int pageNo=pageAction; pageNo <= pageNum; pageNo++){
			try {
				List<RGXJ_PriceDTO> subs = fetchSearchPage(sessionId, pageNo);
				if(subs != null && subs.size() > 0){
					priceList.addAll(subs);
					savePageRecord(pageNo,PageConstant.GCW_RGXJ);
					if(pageNo == pageNum){
						priceList.get(0).setPageOver(true);
					}
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
	
	//提取某页信息价
	public static List<RGXJ_PriceDTO> fetchSearchPage(String sessionId, int pageNo) throws IOException{
		Connection conn = Jsoup.connect("http://xunjia.gldjc.com/index");
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		//表单数据
		conn.data("pageNo", String.valueOf(pageNo));
		conn.data("areaCode", "全国");
		
		conn.timeout(timeOut);
		
		//主页面内容
		Document doc = conn.post();
		Sleep.longSleep();
		Document jdoc = Jsoup.parse(doc.html());
		Element e_root = jdoc.select(".main > table").first();
		if(e_root != null){
			Elements trs = e_root.select("tr:gt(0)");
			if(trs != null && trs.size() > 0){
				List<RGXJ_PriceDTO> priceList = new ArrayList<>();
				for(Element tr : trs){
					String name = JSoupSelectU.owntext(tr, "td:eq(0) p");
					String guige = JSoupSelectU.owntext(tr, "td:eq(1) p");
					String danwei = JSoupSelectU.owntext(tr, "td:eq(2) p");
					String jiage = JSoupSelectU.owntext(tr, "td:eq(3) p");
					String shijian = JSoupSelectU.owntext(tr, "td:eq(4) p");
					String diqu = JSoupSelectU.owntext(tr, "td:eq(5) p");
					String beizhu = JSoupSelectU.owntext(tr, "td:eq(6) p");
					String gongyishang = JSoupSelectU.owntext(tr, "td:eq(7) > div > p");
					
					String lanxiren = JSoupSelectU.owntext(tr, "td:eq(7) > div > div > div > table.mtab.cent > tbody > tr > td.td05");
					String lanxifangshi = JSoupSelectU.owntext(tr, "td:eq(7) > div > div > div > table.mtab.cent > tbody > tr > td.td06");
					
					
					RGXJ_PriceDTO price = new RGXJ_PriceDTO();
					
					price.setName(name);//名称
					price.setSpec(guige);//规格
					price.setUnit(danwei);//计量单位
					price.setPrice(jiage);//价格
					price.setPublishTime(shijian);
					price.setArea(diqu);//地区
					price.setRemark(beizhu);//备注
					price.setSupplierName(gongyishang);//供应商
					price.setSupplierContactName(lanxiren);//联系人
					price.setSupplierContact(lanxifangshi);//联系方式
					
					priceList.add(price);
				}
				logger.info("广材网---人工询价页面---爬取数据第"+pageNo+"页成功");
				return priceList;
			}
		}
		return null;
	}







	//jc-web项目调用
	//提取全部信息价格
	public static List<RGXJ_PriceDTO> fetchSearch(String sessionId, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;

		List<RGXJ_PriceDTO> priceList = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<RGXJ_PriceDTO> subs = fetchSearchPage(sessionId, pageNo);
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

}
