package com.itsum.jc.extract.gcw;

import java.io.IOException;
import java.text.ParseException;
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
import com.itsum.jc.extract.gcw.dto.DLHBJ_PriceDTO;

import jmini.utils.DT;
import jmini.utils.Str;

/**
 * 电缆红本价工具类
 * 
 * @author Jason Ma
 */
public class DlhbjFetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(DlhbjFetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;

	//提取全部电缆红本价
	public static List<DLHBJ_PriceDTO> fetchSearch_jcCrawler(String sessionId, int pageNum,int pageAction){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		
		List<DLHBJ_PriceDTO> priceList = new ArrayList<>();
		for(int pageNo=pageAction; pageNo <= pageNum; pageNo++){
			try {
				List<DLHBJ_PriceDTO> subs = fetchSearchPage(sessionId, pageNo);
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
	
	//提取某页电缆红本价
	public static List<DLHBJ_PriceDTO> fetchSearchPage(String sessionId, int pageNo) throws IOException{
		//第一页数据直接在页面加载
		Elements trs = null;
		List<DLHBJ_PriceDTO> priceList = new ArrayList<>();
		if(pageNo == 1){
			Connection conn = Jsoup.connect("http://www.gldjc.com/cable/index");
			conn.cookie("gldjc_sessionid", sessionId);
			conn.userAgent(user_agent);
			conn.timeout(timeOut);
			
			Document doc = conn.get();
			Sleep.longSleep();
			trs = doc.select(".tableConBox > table > tbody > tr");
		}else if(pageNo > 1){
			Connection conn = Jsoup.connect("http://www.gldjc.com/scroll/more/cables");
			conn.cookie("gldjc_sessionid", sessionId);
			conn.userAgent(user_agent);
			//表单数据
			conn.data("pageStart", "50");
			conn.data("socllNum", String.valueOf(pageNo));
			
			conn.timeout(timeOut);
			
			conn.post();
			//主页面内容
			String resp_body = conn.response().body();
			Document jdoc = Jsoup.parse("<table><tbody>" + resp_body + "</tbody></table>");
			Sleep.longSleep();
			trs = jdoc.select("table > tbody > tr");
		}
		if(trs != null && trs.size() > 0){
//			for(Element tr : trs){
			for(int i = 0; i<trs.size() ; i++){
				String guige = JSoupSelectU.owntext(trs.get(i), "td:eq(1) a");
				String danwei = JSoupSelectU.owntext(trs.get(i), "td:eq(2) div");
				String dianya = JSoupSelectU.owntext(trs.get(i), "td:eq(3) div");
				String hanshui = JSoupSelectU.owntext(trs.get(i), "td:eq(4) div");
				String chushui = JSoupSelectU.owntext(trs.get(i), "td:eq(5) div");
				String chengben = JSoupSelectU.owntext(trs.get(i), "td:eq(6) div");

				String shuilu = JSoupSelectU.owntext(trs.get(i), "td:eq(1) .popSpecModel p:eq(2) .popSpecModelRight");
				String shijian = JSoupSelectU.owntext(trs.get(i), "td:eq(1) .popSpecModel p:eq(3) .popSpecModelRight");

				DLHBJ_PriceDTO price = new DLHBJ_PriceDTO();

				price.setSpec(guige);//规格型号
				price.setUnit(danwei);//计量单位
				price.setVoltage(dianya);//电压等级
				price.setTaxPrice(hanshui);//含税价
				price.setPrice(chushui);//除税价
				price.setTaxCostPrice(chengben);//含税成本价
				price.setTaxRate(shuilu);//增值税税率
				price.setPublishTime(shijian);//时间

				String cableProductId = "";
				String cableProductPriceId = "";
				Element td7_a2 = trs.get(i).select("td:eq(7) > div.viewDet > a:eq(1)").first();
				if(td7_a2 != null){
					cableProductId = td7_a2.attr("cableproductid");
					cableProductPriceId = td7_a2.attr("cableproductpriceid");
				}
				//获取跳转历史价格需要的链接参数
				//modelNameSpecifications = 规格型号, voltage=电压等级 pageIndex=当前页 days=30 selFlag=4(全部，不需要days)
				price.setCableProductId(cableProductId);
				price.setCableProductPriceId(cableProductPriceId);

				if(i == trs.size()-1){
					price.setPageNo(pageNo);
				}
				priceList.add(price);
				//查询该电缆产品的历史价格，目前只查询一页的历史价格
//					List<DLHBJ_PriceDTO> history_list = fetchHistory(sessionId, price, 1);
//					priceList.addAll(history_list);
			}
		}
		logger.info("广材网---电缆红本价---爬取数据第"+pageNo+"页成功");
		return priceList;
	}
	
	
	/**
	 * 查询单个产品的历史价格
	 * @param parentDTO
	 * @param pageNum
	 * @return
	 */
	public static List<DLHBJ_PriceDTO> fetchHistory(String sessionId, DLHBJ_PriceDTO parentDTO , int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		
		List<DLHBJ_PriceDTO> priceList = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<DLHBJ_PriceDTO> subs = fetchHistoryPage(sessionId, parentDTO, pageNo);
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
	

	//某页的历史记录价格
	public static List<DLHBJ_PriceDTO> fetchHistoryPage(String sessionId, DLHBJ_PriceDTO parentDTO, int pageNo) throws IOException{
		String cableProductId = parentDTO.getCableProductId();
		String cableProductPriceId = parentDTO.getCableProductPriceId();
		String modelNameSpecifications = parentDTO.getSpec();
		String voltage = parentDTO.getVoltage();
		StringBuilder sb = new StringBuilder();
		sb.append("http://www.gldjc.com/cable/price/quotation?")
		.append("cableProductId=").append(cableProductId)
		.append("&cableProductPriceId=").append(cableProductPriceId)
		.append("&modelNameSpecifications=").append(modelNameSpecifications)
		.append("&voltage=").append(voltage)
		.append("&pageIndex=").append(pageNo)
		.append("&selFlag=4");//selFlag=4(全部，不需要days)
		//.append("days=30");
		
		Connection conn = Jsoup.connect(sb.toString());
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		//表单数据
		conn.data("pageStart", "0");
		conn.data("socllNum", String.valueOf(pageNo));
		
		conn.timeout(timeOut);
		
		//主页面内容
		Document doc = conn.get();
		Sleep.longSleep();
		Elements trs = doc.select(".tableQuotationBox table.gray_table > tbody > tr");
		if(trs != null && trs.size() > 0){
			List<DLHBJ_PriceDTO> h_prices = new ArrayList<>();
			for(Element tr : trs){
				String publishTime = JSoupSelectU.text(tr, "td:eq(0)");
				if(!Str.isEmpty(publishTime)){
					//数据格式转换
					try {
						publishTime = DT.toStr(DT.parse(publishTime, "yyyyMMdd"), "yyyy-MM-dd") ;
					} catch (ParseException e) {
						logger.error("DLHBJ_Price date format error", e);
					}
				}
				String taxPrice = JSoupSelectU.text(tr, "td:eq(1)");
				String price = JSoupSelectU.text(tr, "td:eq(2)");
				String taxCostPrice = JSoupSelectU.text(tr, "td:eq(3)");
				
				DLHBJ_PriceDTO h_price = copyNew(parentDTO);
				h_price.setPublishTime(publishTime);
				h_price.setTaxPrice(taxPrice);
				h_price.setPrice(price);
				h_price.setTaxCostPrice(taxCostPrice);
				
				h_prices.add(h_price);
			}
			
			return h_prices;
		}
		return null;
	}
	
	//拷贝数据到新DTO
	private static DLHBJ_PriceDTO copyNew(DLHBJ_PriceDTO src){
		DLHBJ_PriceDTO target = new DLHBJ_PriceDTO();
		target.setSpec(src.getSpec());//规格型号
		target.setUnit(src.getUnit());//计量单位
		target.setVoltage(src.getVoltage());//电压等级
		target.setTaxPrice(src.getTaxPrice());//含税价
		target.setPrice(src.getPrice());//除税价
		target.setTaxCostPrice(src.getTaxCostPrice());//含税成本价
		target.setTaxRate(src.getTaxRate());//增值税税率
		target.setPublishTime(src.getPublishTime());//时间
		target.setCableProductId(src.getCableProductId());
		target.setCableProductPriceId(src.getCableProductPriceId());
		
		return target;
	}







    //jc-web项目用
	//提取全部电缆红本价
	public static List<DLHBJ_PriceDTO> fetchSearch(String sessionId, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;

		List<DLHBJ_PriceDTO> priceList = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<DLHBJ_PriceDTO> subs = fetchSearchPage(sessionId, pageNo);
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
