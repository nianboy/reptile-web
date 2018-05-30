package com.itsum.jc.crawler.jsoup;

import jmini.utils.Str;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSoupSelectU {
	
	public static Logger LOG = LoggerFactory.getLogger(JSoupSelectU.class);
	
	/**
	 * 获取cssSelector元素的属性值
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String attr(Element doc, String cssSelector, String attrName){
		if(Str.isEmpty(cssSelector) || Str.isEmpty(attrName)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements != null && elements.size() > 0){
				return elements.first().attr(attrName);
			}else{
				return null;
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
	}
	
	/**
	 * 获取cssSelector首个元素的html
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String outherhtml(Element doc,  String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.first().outerHtml();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
	}
	
	/**
	 * 获取cssSelector所有元素的属性值
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String outerhtmls(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.outerHtml();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
	}	
	
	/**
	 * 获取cssSelector首个元素的html
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String html(Element doc,  String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.first().html();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
	}
	
	/**
	 * 获取cssSelector所有元素的属性值
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String htmls(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.html();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
	}
	
	/**
	 * 获取cssSelector元素的href数据('abs:href')，绝对路径
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String abshref(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements != null && elements.size() > 0){
				//获取觉得路径
				return elements.first().attr("abs:href");
			}else{
				return null;
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
		
	}
	
	/**
	 * 获取满足cssCelector的首个元素文本
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String text(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.first().text();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
		
	}
	
	/**
	 * 获取满足cssCelector的所有元素的文本
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String texts(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		Elements elements = null;
		try{
			elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.text();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
		
	}
	
	
	/**
	 * 获取满足cssCelector的首个元素文本
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String owntext(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		try{
			Elements elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				return elements.first().ownText();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
		
	}
	
	/**
	 * 获取满足cssCelector的所有元素的文本
	 * @param page
	 * @param cssSelector
	 * @return
	 */
	public static String owntexts(Element doc, String cssSelector){
		if(Str.isEmpty(cssSelector)){
			return null;
		}
		Elements elements = null;
		try{
			elements = doc.select(cssSelector);
			if(elements == null || elements.size() == 0){
				return null;
			}else{
				StringBuilder sb = new StringBuilder();
				for(int i=0; i < elements.size(); i++){
					sb.append(elements.get(i).ownText());
				}
				return sb.toString();
			}
		}catch(IllegalArgumentException e){
			LOG.warn("Parse page error:", e);
			return null;
		}catch(RuntimeException e){
			LOG.warn("Parse doc error:", e);
			return null;
		}
		
	}

}
