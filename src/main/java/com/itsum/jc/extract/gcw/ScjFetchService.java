package com.itsum.jc.extract.gcw;

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

import com.itsum.jc.crawler.jsoup.JSoupSelectU;
import com.itsum.jc.extract.gcw.dto.SCJ_PriceDTO;
import com.itsum.jc.extract.gcw.dto.SpuDTO;

import jmini.utils.Str;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

/**
 * 市场价查询工具类
 * @author Jason Ma
 *
 */
public class ScjFetchService {
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	//提取材质
	private static Pattern caizhi_p = Pattern.compile(".*材质 : (.+?)(?: \\||$).*");
	//提取规格
	private static Pattern guige_p = Pattern.compile(".* 规格.* : (.+?)(?: \\||$).*");

	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScjFetchService.class);
	
	
	//从市场价格中提取链接
	public static List<SpuDTO> fetchSearchLink_jcCrawler(String sessionId, int pageNum, int pageAction){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		
		List<SpuDTO> links = new ArrayList<>();
		for(int pageNo=pageAction; pageNo <= pageNum; pageNo++){
			try {
				List<SpuDTO> subs = fetchSearchListSinglePage(sessionId, pageNo);//云南省编号
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
	
	//提取单页面信息
	public static List<SpuDTO> fetchSearchListSinglePage(String sessionId, int pageNo) throws IOException{
		String page_url = "http://www.gldjc.com/scj/so.html?l=530000&q=Precise&is=0&p=" + pageNo;
//		String page_url = "http://www.gldjc.com/scj/so.html?l=1&q=Precise&is=0&p=" + pageNo;
		Connection conn = Jsoup.connect(page_url);
		
		conn.cookie("gldjc_sessionid", sessionId)
		.userAgent(user_agent)
		.timeout(timeOut);
		
		Pattern p = Pattern.compile("openMarketPriceDetail\\('(.+-.+)',this\\);");
		//主页面内容
		Document doc = conn.get();
		Sleep.longSleep();
		Document jdoc = Jsoup.parse(doc.html());
		Elements es = jdoc.select(".productName > a[onclick^=openMarketPriceDetail]");
		if(es != null && es.size() > 0){
			List<SpuDTO> links = new ArrayList<SpuDTO>();
//			for(Element e : es){
			for(int i = 0 ; i < es.size() ; i++){
				Matcher matcher = p.matcher(es.get(i).attr("onclick"));
				if(matcher.matches()){
					String id = matcher.group(1);
					SpuDTO spu = new SpuDTO();
					spu.setLink("http://www.gldjc.com/scj/" + id + "_1.html?f=quotation_date&s=DESC");
					spu.setName(id);
					if(i == es.size()-1){
						spu.setPageNo(pageNo);
					}
					links.add(spu);
				}
			}
			logger.info("广材网---市场价页面---爬取Spu链接第"+pageNo+"页成功");
			return links;
		}
		return null;
	}


	//获取多页的材料市场价
	public static  List<SCJ_PriceDTO> fetchDeteil(String sessionId, String spuName, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;
		List<SCJ_PriceDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<SCJ_PriceDTO> subs = fetchDeteilSinglePage(sessionId, spuName, pageNo);
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
	
	//获取单个材料的市场价
	public static List<SCJ_PriceDTO> fetchDeteilSinglePage(String sessionId, String spuName, int pageNo) throws IOException{
		//生成链接
		String page_url = "http://www.gldjc.com/scj/" + spuName + "_1_" + pageNo + ".html?f=quotation_date&s=DESC";
		
		Connection conn = Jsoup.connect(page_url);
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		//主页面内容
		Document doc = conn.get();
		Sleep.longSleep();
		Document jdoc = Jsoup.parse(doc.html());
		
		String info_str = JSoupSelectU.owntext(jdoc, ".productIntro .productContentDiv");
		String name =  JSoupSelectU.owntext(jdoc, ".productIntro .productContent h2");//名称
		String category1Id = jdoc.select(".productIntroBox input[name=\"catalogId\"]").val();//分类1id
		String category2Id = jdoc.select(".productIntroBox input[name=\"catalog2Id\"]").val();//分类2id
		String categoryName = jdoc.select(".productIntroBox input[name=\"catalog2\"]").val();//分类名称
		String caizhi = "";//材质
		String guige = "";//规格
		String properties = info_str;//属性
		
		//提取材质
		Matcher caizhi_matcher = caizhi_p.matcher(info_str);
		if(caizhi_matcher.matches()){
			caizhi = caizhi_matcher.group(1);
		}
		//提取规格
		Matcher guige_matcher = guige_p.matcher(info_str);
		if(guige_matcher.matches()){
			guige = guige_matcher.group(1);
		}
		//处理列表
		Elements es = jdoc.select(".spuShowListBox .contentLi");
		//城市名称提取规则
		Pattern chengshi_p = Pattern.compile(".*【(.*?)】.*");
		if(es != null && es.size() > 0){
			List<SCJ_PriceDTO> priceList = new ArrayList<>();
			for(Element e : es){
				String companyName = "";
				String cityName = "";
				String a_companyName = e.select("a[name=a_companyName]").text();
				if(!Str.isEmpty(a_companyName)){
					Matcher chengshi_matcher = chengshi_p.matcher(a_companyName);
					if(chengshi_matcher.matches()){
						cityName = chengshi_matcher.group(1);
						companyName = a_companyName.replace("【" + cityName + "】", "");
					}else{
						companyName = a_companyName;
					}
				}
				String linkMan = e.select("#supplier_msg .supplierSpanDs > p:eq(1) > font").text();
				String linkPhone = e.select("#supplier_msg .supplierSpanDs > p:eq(2) > font").text();
				String companyAddress = e.select("#supplier_msg .supplierSpanDs > p:eq(3) > font").text();
				String taxpayer = e.select("#supplier_msg .supplierSpanDs > p:eq(4) > font").text();
				String province = "";
				if(companyAddress.contains("-")){
					province = companyAddress.split("-")[0];
				}
				SCJ_PriceDTO price = new SCJ_PriceDTO();
				price.setLinkMan(linkMan);
				price.setLinkPhone(linkPhone);
				price.setCompanyAddress(companyAddress);
				price.setTaxpayer(taxpayer);
				price.setProvince(province);

				price.setName(name);//材料名称
				price.setTexture(caizhi);//材质
				price.setSpec(guige);//规格
				price.setProperties(properties);//属性（包括多项）
				price.setCityName(cityName);//城
				price.setCompanyName(companyName);//公司名称
				price.setBrand(JSoupSelectU.owntext(e, ".brandDiv .conSpan"));//品牌
				price.setUnit(JSoupSelectU.owntext(e, ".unitDiv .conSpan"));//计量单位
				price.setPublishTime(JSoupSelectU.text(e, ".priTimeDiv"));//含税市场价
				price.setTaxPriceMarket(JSoupSelectU.text(e, ".consprice"));//含税市场价
				price.setTaxPrice(JSoupSelectU.text(e, ".marPriDiv"));//含税面价
				price.setTaxRate(JSoupSelectU.text(e, ".taxRatesDiv"));//税率

				price.setCategory1Id(category1Id);
				price.setCategory2Id(category2Id);
				price.setCategoryName(categoryName);
				
				priceList.add(price);
			}
			logger.info("广材网---市场价页面---当前链接爬取数据第"+pageNo+"页成功");
			return priceList;
		}
		return null;
	}








	//jc-web项目用
	//从市场价格中提取链接
	public static List<SpuDTO> fetchSearchLink(String sessionId, int pageNum){
		//网站最多显示100页查询结果
		if(pageNum > 100)
			pageNum = 100;
		if(pageNum < 1)
			pageNum = 1;

		List<SpuDTO> links = new ArrayList<>();
		for(int pageNo=1; pageNo <= pageNum; pageNo++){
			try {
				List<SpuDTO> subs = fetchSearchListSinglePage(sessionId, pageNo);//云南省编号
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

