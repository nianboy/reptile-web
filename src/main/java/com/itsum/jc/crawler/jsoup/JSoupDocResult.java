package com.itsum.jc.crawler.jsoup;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import jmini.dto.IDataDTO;

/**
 * JSoup 返回信息
 * @author Jason Ma
 *
 */
public class JSoupDocResult implements IDataDTO{

	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_ERROR = -1;
	
	private Document doc = null;
	
	private Connection conn = null;
	
	/**
	 * 获取Http返回码
	 * @return
	 */
	public int statusCode(){
		if(conn != null && conn.response() != null){
			return conn.response().statusCode();
		}else{
			return STATUS_ERROR;
		}
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
