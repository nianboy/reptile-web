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
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.gcw.dto.Gchq_AreaCodeDTO;

import jmini.utils.Str;

/**
 * 钢材行情区域提取
 * 
 * @author Jason Ma
 */
public class GchqAreaCodeService {
	
	public static final Logger logger = LoggerFactory.getLogger(GchqAreaCodeService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static Pattern ptn_areaCode = Pattern.compile("/quotation/price_quotation/(\\d+);$");
	
	/**
	 * 查询所有钢材价格区域
	 */
	public static List<Gchq_AreaCodeDTO> fetchAreaCodes(String sessionId) throws IOException{
		Connection conn = Jsoup.connect("http://hangqing.gldjc.com/quotation/to_quotation");
		conn.cookie("gldjc_sessionid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		Document doc = conn.get();
		Sleep.longSleep();
		Elements es = doc.select(".jghq_dq dd[name=cityAreaCode] a");
		if(es != null && es.size() > 0){
			List<Gchq_AreaCodeDTO> areaCodeList = new ArrayList<>();
			for(Element e: es){
				String href = e.attr("href");
				if(!Str.isEmpty(href)){
					Gchq_AreaCodeDTO ac = new Gchq_AreaCodeDTO();
					ac.setName(e.ownText());
					//提取code
					Matcher matcher_areaCode = ptn_areaCode.matcher(href);
					if(matcher_areaCode.matches()){
						ac.setCode(matcher_areaCode.group(1));
					}
					areaCodeList.add(ac);
				}
			}
			return areaCodeList; 
		}
		return null;
	}
	
}
