package com.itsum.jc.extract.zjtcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itsum.jc.Sleep;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.zjtcn.dto.ZJT_XXJ_PriceDTO;

/**
 * 造价通 - 信息价 - 根据Id获取历史报价信息
 * 
 * @author Jason Ma
 *
 */
public class Xxj_FetchByIdService {
	
	public static final Logger logger = LoggerFactory.getLogger(Xxj_FetchByIdService.class);
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	
	private static int timeOut = 5000;
	
	private static ObjectMapper om = new ObjectMapper();
	
//	public static void main(String[] args) throws IOException {
//		List<ZJT_XXJ_PriceDTO> pList = fetchDetail("4f99c7f5-ec00-4bf1-851f-9777caef6a26", "15278872", "广东", "韶关市");
//		if(pList != null && pList.size()> 0){
//			System.out.println(pList.size());
//			for(ZJT_XXJ_PriceDTO p : pList){
//				System.out.println(p);
//			}
//		}
//	}
	
	/**
	 * 提取详情页面数据
	 * @return
	 * @throws IOException 
	 */
	public static List<ZJT_XXJ_PriceDTO> fetchDetail(String sessionId, String id, String provinceCode, String province, String city) throws IOException{
		Connection conn = Jsoup.connect("https://" + provinceCode + ".zjtcn.com/gov/chartData");
		conn.cookie("jsid", sessionId);
		conn.userAgent(user_agent);
		conn.timeout(timeOut);
		
		//conn.data("name", "线材");
		//conn.data("spec", "Ф10以内");
		//conn.data("unit", "t");
		conn.data("province_Match", province);
		conn.data("echartCity", city);
		
		//conn.data("startDate", "2018-01");
		//conn.data("endDate", "2018-12");
		//conn.data("dateDefault", "2015");
		//conn.data("industryId", "建筑工程");
		conn.data("ids", id);
		//conn.data("priceType", "2");
		//conn.data("facs", "");
		
		conn.ignoreContentType(true);
		
		conn.post();
		
		//单页面内容提取
		int statusCode = conn.response().statusCode();
		if(statusCode != 200){
			return null;
		}
		String js_str = conn.response().body();
		JsonNode rootNode = om.readTree(js_str);
		Sleep.longSleep();
		if(rootNode != null){
			JsonNode citypage_node = rootNode.get("citypage");
			if(citypage_node != null && citypage_node.size() > 0){
				List<ZJT_XXJ_PriceDTO> priceList = new ArrayList<>();
				for(JsonNode node : citypage_node){
					ZJT_XXJ_PriceDTO p = new ZJT_XXJ_PriceDTO();
					p.setId(node.get("id").asText());
					p.setCode(node.get("code").asText());
					p.setName(node.get("name").asText());
					p.setStdName(node.get("stdName").asText());
					p.setSpec(node.get("spec").asText());
					p.setUnit(node.get("unit").asText());
					p.setProvince(node.get("province").asText());
					p.setCity(node.get("city").asText());
					p.setCounty(node.get("county").asText());
					p.setPublishTime(node.get("issueDate").asText());
					p.setUpdateOn(node.get("updateOn").asText());
					p.setPriceNtax(node.get("priceNtax").asText());
					p.setNotes(node.get("notes").asText());
					
					priceList.add(p);
				}
				return priceList;
			}
		}
		return null;
	}

}

