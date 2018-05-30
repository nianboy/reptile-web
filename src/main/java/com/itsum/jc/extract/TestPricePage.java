package com.itsum.jc.extract;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.itsum.jc.crawler.jsoup.JSoupSelectU;

import jmini.utils.Str;

public class TestPricePage {
	
	private static String session_id = "e7dc0563-f2f6-4b53-bc9e-923f5d9f3ab2";
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";

	public static void main(String[] args) throws IOException {
		//scjSearch();
		//scjDetail("http://www.gldjc.com/scj/SPU-702495_1.html?f=quotation_date&s=DESC");
		
		//xxjDetail();
		
		//rgxj();
		
		cables();
	}
	
	//从市场价格中提取链接
	public static void scjSearch() throws IOException{
		Connection conn = Jsoup.connect("http://www.gldjc.com/scj/so.html?l=1&q=Precise&is=0");
		conn.cookie("gldjc_sessionid", session_id);
		conn.userAgent(user_agent);
		
		Pattern p = Pattern.compile("openMarketPriceDetail\\('(.+-.+)',this\\);");
		//主页面内容
		Document doc = conn.get();
		org.jsoup.nodes.Document jdoc = Jsoup.parse(doc.html());
		org.jsoup.select.Elements es = jdoc.select("a[onclick^=openMarketPriceDetail]");
		if(es != null){
			for(Element e : es){
				System.out.println(e.attr("onclick"));
				Matcher matcher = p.matcher(e.attr("onclick"));
				if(matcher.matches()){
					String id = matcher.group(1);
					System.out.println("http://www.gldjc.com/scj/" + id + "_1.html?f=quotation_date&s=DESC");
				}
			}
		}
	}
	
	//从材料详情中提取价格
	public static void scjDetail(String url) throws IOException{
		Connection conn = Jsoup.connect(url);
		conn.cookie("gldjc_sessionid", session_id);
		conn.userAgent(user_agent);
		
		//主页面内容
		Document doc = conn.get();
		org.jsoup.nodes.Document jdoc = Jsoup.parse(doc.html());
		
		String info_str = JSoupSelectU.owntext(jdoc, ".productIntro .productContentDiv");
		String name =  JSoupSelectU.owntext(jdoc, ".productIntro .productContent h2");//名称
		String caizhi = "";//材质
		String guige = "";//规格
		String properties = info_str;//属性
		
		//提取材质
		Pattern caizhi_p = Pattern.compile(".*材质 : (.+?)(?: \\||$).*");
		Matcher caizhi_matcher = caizhi_p.matcher(info_str);
		if(caizhi_matcher.matches()){
			caizhi = caizhi_matcher.group(1);
		}
		//提取规格
		Pattern guige_p = Pattern.compile(".*规格 : (.+?)(?: \\||$).*");
		Matcher guige_matcher = guige_p.matcher(info_str);
		if(guige_matcher.matches()){
			guige = guige_matcher.group(1);
		}
		//处理列表
		org.jsoup.select.Elements es = jdoc.select(".spuShowListBox .contentLi");
		//城市名称提取规则
		Pattern chengshi_p = Pattern.compile(".*【(.*?)】.*");
		if(es != null){
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
				//抓取规格型号
				System.out.println("材料名称:" + name);
				System.out.println("材质 :" + caizhi);
				System.out.println("规格 :" + guige);
				//全部属性
				System.out.println("属性（包括多项）:" + properties);
				System.out.println("城市:" + cityName);
				System.out.println("公司名称:" + companyName);
				System.out.println("品牌:" + e.select(".brandDiv .conSpan").text());
				System.out.println("计量单位:" + e.select(".unitDiv .conSpan").text());
				System.out.println("发布时间:" + e.select(".priTimeDiv").text());
				System.out.println("含税市场价:" + e.select(".consprice").text());
				System.out.println("含税面价:" + e.select(".marPriDiv").text());
				System.out.println("税率:" + e.select(".taxRatesDiv").text());
				System.out.println("===========================================================================");
			}
		}
	}
	
	//提取信息查询数据
	public static void xxjDetail() throws IOException{
		Connection conn = Jsoup.connect("http://www.gldjc.com/xxj/so.html?locationId=&locName=全国&keyword=&year=2018&period=03&currentPage=1");
		conn.cookie("gldjc_sessionid", session_id);
		conn.userAgent(user_agent);
		conn.timeout(5000);
		
		//主页面内容
		Document doc = conn.get();
		org.jsoup.nodes.Document jdoc = Jsoup.parse(doc.html());
		Element e_root = jdoc.select(".wdsc > table").first();
		if(e_root != null){
			Elements trs = e_root.select("tr:gt(0)");
			if(trs != null){
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
					
					System.out.println("材料名称:" + name);
					System.out.println("规格型号:" + guige);
					System.out.println("单位:" + danwei);
					System.out.println("除税价:" + chushuijia);
					System.out.println("含税价:" + hanshuijia);
					System.out.println("税率:" + shuilv);
					System.out.println("备注:" + beizhu);
					System.out.println("专业:" + zhuanye);
					System.out.println("地区期数:" + diqu);
					System.out.println("============================================================");
				}
			}
		}
	}
	
	//提取人工信息查询数据
	public static void rgxj() throws IOException{
		Connection conn = Jsoup.connect("http://xunjia.gldjc.com/index");
		conn.cookie("gldjc_sessionid", session_id);
		conn.userAgent(user_agent);
		//表单数据
		conn.data("pageNo", "10");
		conn.data("areaCode", "全国");
		
		conn.timeout(5000);
		
		//主页面内容
		Document doc = conn.post();
		org.jsoup.nodes.Document jdoc = Jsoup.parse(doc.html());
		Element e_root = jdoc.select(".main > table").first();
		if(e_root != null){
			Elements trs = e_root.select("tr:gt(0)");
			if(trs != null){
				for(Element tr : trs){
					String name = JSoupSelectU.owntext(tr, "td:eq(0) p");
					String guige = JSoupSelectU.owntext(tr, "td:eq(1) p");
					String danwei = JSoupSelectU.owntext(tr, "td:eq(2) p");
					String jiage = JSoupSelectU.owntext(tr, "td:eq(3) p");
					String shijian = JSoupSelectU.owntext(tr, "td:eq(4) p");
					String diqu = JSoupSelectU.owntext(tr, "td:eq(5) p");
					String beizhu = JSoupSelectU.owntext(tr, "td:eq(6) p");
					String gongyishang = JSoupSelectU.owntext(tr, "td:eq(7) p");
					
					System.out.println("名称:" + name);
					System.out.println("规格型号:" + guige);
					System.out.println("单位:" + danwei);
					System.out.println("时间:" + shijian);
					System.out.println("价格:" + jiage);
					System.out.println("地区:" + diqu);
					System.out.println("备注:" + beizhu);
					System.out.println("供应商名称:" + gongyishang);
					
					
					System.out.println("============================================================");
				}
			}
		}
	}
	
	//提取电缆红本价
	public static void cables() throws IOException{
		Connection conn = Jsoup.connect("http://www.gldjc.com/scroll/more/cables");
		conn.cookie("gldjc_sessionid", session_id);
		conn.userAgent(user_agent);
		//表单数据
		conn.data("pageStart", "0");
		conn.data("socllNum", "0");
		
		conn.timeout(5000);
		
		conn.post();
		//主页面内容
		String resp_body = conn.response().body();
		org.jsoup.nodes.Document jdoc = Jsoup.parse("<table><tbody>" + resp_body + "</tbody></table>");
		Elements trs = jdoc.select("table > tbody > tr");
		if(trs != null){
			for(Element tr : trs){
				String guige = JSoupSelectU.owntext(tr, "td:eq(1) a");
				String danwei = JSoupSelectU.owntext(tr, "td:eq(2) div");
				String dianya = JSoupSelectU.owntext(tr, "td:eq(3) div");
				String hanshui = JSoupSelectU.owntext(tr, "td:eq(4) div");
				String chushui = JSoupSelectU.owntext(tr, "td:eq(5) div");
				String chengben = JSoupSelectU.owntext(tr, "td:eq(6) div");
				
				String shuilu = JSoupSelectU.owntext(tr, "td:eq(1) .popSpecModel p:eq(2) .popSpecModelRight");
				String shijian = JSoupSelectU.owntext(tr, "td:eq(1) .popSpecModel p:eq(3) .popSpecModelRight");
				
				System.out.println("规格型号:" + guige);
				System.out.println("单位:" + danwei);
				System.out.println("电压等级:" + dianya);
				System.out.println("含税价:" + hanshui);
				System.out.println("除税价:" + chushui);
				System.out.println("含税成本价:" + chengben);
				System.out.println("增值税税率:" + shuilu);
				System.out.println("报价时间:" + shijian);
				System.out.println("============================================================");
			}
		}
	}
	
	
}
