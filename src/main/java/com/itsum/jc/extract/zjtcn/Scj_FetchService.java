package com.itsum.jc.extract.zjtcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itsum.jc.Sleep;
import com.itsum.jc.moudles.PageConstant;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.crawler.jsoup.JSoupSelectU;
import com.itsum.jc.extract.zjtcn.dto.ZJT_SCJ_PriceDTO;

import static com.itsum.jc.service.BaseService.savePageRecord;

/**
 * 造价通 - 市场价格
 * 
 * @author Jason Ma
 *
 */
public class Scj_FetchService {
	
	public static final Logger logger = LoggerFactory.getLogger(Scj_FetchService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static String root_url = "https://gd.zjtcn.com/";
	
//	public static void main(String[] args) throws IOException {
//		List<ZJT_SCJ_PriceDTO> pList = fetchSearch("bbc6a298-153d-4325-8134-7d7aef1f09ea", "2018", "04", 1);
//		if(pList != null && pList.size()> 0){
//			System.out.println(pList.size());
//			for(ZJT_SCJ_PriceDTO p : pList){
//				System.out.println(p);
//			}
//		}
//	}
	
	//提取全部信息价格
	public static List<ZJT_SCJ_PriceDTO> fetchSearch_jcCrawler(String sessionId, String year, String month, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		List<ZJT_SCJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<ZJT_SCJ_PriceDTO> subs = fetchSearchPage_jcCrawler(sessionId, year, month, pageNo);
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
	public static List<ZJT_SCJ_PriceDTO> fetchSearchPage_jcCrawler(String sessionId, String year, String month, int pageNo) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(root_url).append("facx/")
		.append("c0000_t_d")//全国c0000
		.append(year).append(month)
		.append("_p").append(pageNo).append("_k_qa_qi.html");
		
		Connection conn = Jsoup.connect(sb.toString());
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		Elements es = doc.select("#listbody > tbody > tr");
		if(es != null && es.size() > 0){
			List<ZJT_SCJ_PriceDTO> priceList = new ArrayList<>();
			StringBuilder builder = new StringBuilder();
			String issueDate = "";
			for(int i = 0;i<es.size() ; i++){
				Element e = es.get(i);
				Element a = e.select("td:eq(1) > a").first();
				String abs_url = "";
				if(a != null){
					abs_url = a.absUrl("href");
					String trId = e.select("td:eq(9)").attr("tip").split("_")[1];//获取一条链接中的id
					if(i != es.size()-1){//拼接当前数据中的id
						builder.append(trId).append(",");
					}else{
						builder.append(trId);
					}
					issueDate = e.select("td:eq(10)").text();//获取一条链接中的时间

					String taxpayer = e.select("td:eq(9) li:eq(2) span:eq(1)").text();
					String contact = e.select("td:eq(9) li:eq(3) span:eq(1)").text();
					ZJT_SCJ_PriceDTO zjt_scj_priceDTO = new ZJT_SCJ_PriceDTO();
					zjt_scj_priceDTO.setContact(contact);
					zjt_scj_priceDTO.setTaxpayer(taxpayer);
					zjt_scj_priceDTO.setLink(abs_url);
					priceList.add(zjt_scj_priceDTO);
				}
			}
			String nowDateStr = String.valueOf(new Date().getTime());//当前时间戳
			String ids = builder.toString();
			if(ids.length() > 0){
				getPhoneList(nowDateStr,issueDate,ids,priceList,sessionId);
			}
			logger.info("造价通---市场价页面---爬取Spu链接数量第"+pageNo+"页");
			return priceList;
		}
		return null;
	}

	//获取当前页面的所有公司联系方式
	public static void getPhoneList(String nowDateStr,String issueDate,String ids,List<ZJT_SCJ_PriceDTO> listZJT,String sessionId){
		String getPhoneurl = "https://zj.zjtcn.com/materialfac/getPriceByIds.json?dataTime="+nowDateStr+"&issueDate"+issueDate+"&ids="+ids;
		Connection conn = Jsoup.connect(getPhoneurl);
		conn.cookie("jsid", "5a27b69a-8b65-45f0-88da-0082e528ca38");
		conn.userAgent(user_agent);
		conn.timeout(timeOut);

		//单页面内容提取
		try {
			conn.ignoreContentType(true);
			conn.get();
			Document doc = conn.get();
			Sleep.longSleep();
			String js_str = conn.response().body();
			ObjectMapper om = new ObjectMapper();
			JsonNode rootNode = om.readTree(js_str);
			JsonNode jsonNode = rootNode.get("results").get("matList");
			for(int i = 0; i<jsonNode.size(); i++){
				String jsonStr = jsonNode.get(i).get(2).toString();
				String contact = jsonStr.substring(jsonStr.indexOf(",",jsonStr.indexOf(",")+1)+1).replaceAll("\"","").replaceAll("]","");//截取第二个逗号后面的
				if(listZJT.size() >= i+1){
					listZJT.get(i).setContact(contact);
				}
			}
		} catch (IOException e) {
			logger.error("造价通---市场价页面---获取公司联系电话请求异常");
			e.printStackTrace();
		}
	}

	/**
	 * 提取详情页面数据
	 * @return
	 * @throws IOException 
	 */
	public static List<ZJT_SCJ_PriceDTO> fetchDetail_jcCrawler(String sessionId, ZJT_SCJ_PriceDTO zjt_scj_priceDTO) throws IOException{
		String link = "";
		if(zjt_scj_priceDTO != null){
			link = zjt_scj_priceDTO.getLink();
		}else{
			return null;
		}
		Connection conn = Jsoup.connect(link);
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		int statusCode = conn.response().statusCode();
		if(statusCode != 200){
			logger.info("request fail statusCode:{}", statusCode);
			return null;
		}
		//防止进入无法访问页面
		Elements valid_e = doc.select("#stdName");
		if(valid_e == null || valid_e.size() == 0){
			return null;
		}
		//获取产品公共信息
		String id = doc.select("#id").first().val();
		String code = doc.select("#code").first().val();
		String stdName = doc.select("#stdName").first().val();
		String name = doc.select("#name").first().val();
		String spec = doc.select("#spec").first().val();
		String keyFeatures = doc.select("#keyFeatures").first().val();
		String unit = doc.select("#unit").first().val();
		String brand = doc.select("#brand").first().val();
		
		String cityName = JSoupSelectU.owntext(doc, "div.cl-names > ul > li > span:eq(7)");
		String province = doc.select("#province").first().val();
		String ename = doc.select("#ename").first().val();
		
		//获取价格列表信息
		Elements es = doc.select("#infos > tr:gt(0)");
		if(es != null && es.size() > 0){
			List<ZJT_SCJ_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				String publishTime = JSoupSelectU.owntext(e, "td:eq(0)");
				String taxRate = JSoupSelectU.owntext(e, "td:eq(6)");
				String taxPrice = JSoupSelectU.owntext(e, "td:eq(7) font");
				
				ZJT_SCJ_PriceDTO p = new ZJT_SCJ_PriceDTO();
				p.setId(id);
				p.setCode(code);
				p.setStdName(stdName);
				p.setName(name);
				p.setSpec(spec);
				p.setKeyFeatures(keyFeatures);
				p.setUnit(unit);
				p.setBrand(brand);
				
				p.setProvince(province);
				p.setCityName(cityName);
				p.setCompanyName(ename);
				p.setPublishTime(publishTime);
				p.setTaxPrice(taxPrice);
				p.setTaxRate(taxRate);

				p.setTaxpayer(zjt_scj_priceDTO.getTaxpayer());//纳税人
				p.setContact(zjt_scj_priceDTO.getContact());//联系方式
				priceList.add(p);
			}
			return priceList;
		}
		return null;
	}













	/**
	 * jc-web项目用
	 */
	public static List<ZJT_SCJ_PriceDTO> fetchSearch(String sessionId, String year, String month, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		List<ZJT_SCJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<ZJT_SCJ_PriceDTO> subs = fetchSearchPage(sessionId, year, month, pageNo);
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
	public static List<ZJT_SCJ_PriceDTO> fetchSearchPage(String sessionId, String year, String month, int pageNo) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(root_url).append("facx/")
				.append("c0000_t_d")//全国c0000
				.append(year).append(month)
				.append("_p").append(pageNo).append("_k_qa_qi.html");

		Connection conn = Jsoup.connect(sb.toString());
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);

		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		Elements es = doc.select("#listbody > tbody > tr");
		if(es != null && es.size() > 0){
			List<ZJT_SCJ_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				Element a = e.select("td:eq(1) > a").first();
				if(a != null){
					String abs_url = a.absUrl("href");
					List<ZJT_SCJ_PriceDTO> innerPriceList = fetchDetail(sessionId, abs_url);
					if(innerPriceList != null && innerPriceList.size() > 0){
						priceList.addAll(innerPriceList);
					}
				}
			}
			logger.info("造价通---市场价页面---爬取全部产品数据第"+pageNo+"页");
			return priceList;
		}
		return null;
	}

	/**
	 * jc-web项目用
	 * 提取详情页面数据
	 * @return
	 * @throws IOException
	 */
	public static List<ZJT_SCJ_PriceDTO> fetchDetail(String sessionId, String pageUrl) throws IOException{
		Connection conn = Jsoup.connect(pageUrl);
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);

		//单页面内容提取
		Document doc = conn.get();
		Sleep.longSleep();
		int statusCode = conn.response().statusCode();
		if(statusCode != 200){
			logger.info("request fail statusCode:{}", statusCode);
			return null;
		}
		//防止进入无法访问页面
		Elements valid_e = doc.select("#stdName");
		if(valid_e == null || valid_e.size() == 0){
			return null;
		}
		//获取产品公共信息
		String id = doc.select("#id").first().val();
		String code = doc.select("#code").first().val();
		String stdName = doc.select("#stdName").first().val();
		String name = doc.select("#name").first().val();
		String spec = doc.select("#spec").first().val();
		String keyFeatures = doc.select("#keyFeatures").first().val();
		String unit = doc.select("#unit").first().val();
		String brand = doc.select("#brand").first().val();

		String cityName = JSoupSelectU.owntext(doc, "div.cl-names > ul > li > span:eq(7)");
		String province = doc.select("#province").first().val();
		String ename = doc.select("#ename").first().val();

		//获取价格列表信息
		Elements es = doc.select("#infos > tr:gt(0)");
		if(es != null && es.size() > 0){
			List<ZJT_SCJ_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				String publishTime = JSoupSelectU.owntext(e, "td:eq(0)");
				String taxRate = JSoupSelectU.owntext(e, "td:eq(6)");
				String taxPrice = JSoupSelectU.owntext(e, "td:eq(7) font");

				ZJT_SCJ_PriceDTO p = new ZJT_SCJ_PriceDTO();
				p.setId(id);
				p.setCode(code);
				p.setStdName(stdName);
				p.setName(name);
				p.setSpec(spec);
				p.setKeyFeatures(keyFeatures);
				p.setUnit(unit);
				p.setBrand(brand);

				p.setProvince(province);
				p.setCityName(cityName);
				p.setCompanyName(ename);
				p.setPublishTime(publishTime);
				p.setTaxPrice(taxPrice);
				p.setTaxRate(taxRate);
				priceList.add(p);
			}
			logger.info("造价通---市场价页面---爬取单个链接详情数据"+priceList.size()+"条");
			return priceList;
		}
		return null;
	}

}

