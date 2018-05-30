package com.itsum.jc.service;

import java.util.List;

import com.itsum.jc.extract.zjtcn.ZJT_LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.gcw.GCW_LoginService;
import com.itsum.jc.moudles.TSession;

import jmini.utils.Str;

/**
 *  会话处理
 * 
 * @author Jason Ma
 *
 */
public class SessionService {
	
	private static Logger logger = LoggerFactory.getLogger(SessionService.class);
	
//	public static void main(String[] args) {
//		ENV.init();
//		String sessionId = getGCW();
//		System.out.println("sessionId=" + sessionId);
//	}
	
	/**
	 * 从数据库获取广材网的sessionId
	 * @return
	 */
	public static String getGCW(){
		return getFromDB("1");
	}
	
	/**
	 * 从数据库获取造价通的sessionId
	 * @return
	 */
	public static String getZJT(){
		return getFromDB("2");
	}

	/**
	 * 通过类型获取session Id
	 * @param type
	 * @return
	 */
	private static String getFromDB(String type){
		String typeName = null;
		if("1".equals(type)){
			typeName = "广材网";
		}
		if("2".equals(type)){
			typeName = "造价通";
		}
		String sId = null;
		List<TSession> sList = ENV.db().queryList("TYPE=?", "CREATE_DATE ASC", TSession.class, type);
		if(sList != null && sList.size() > 0){
			for(TSession s : sList){
				 String tsId = s.getSessionId();
				 if(!Str.isEmpty(tsId)){
					 if(GCW_LoginService.isValidSession(tsId) || ZJT_LoginService.isValidSession(tsId)){
						 sId = tsId;
						 break;
					 }else{
						 //id不可用就从数据库删除
						 ENV.db().delete(s);
					 }
				 }
			}
		}
		//如果没有一个可用的sessionId 就写入警告日志
		if(sId == null){
			logger.warn("No available session ID for {}", typeName);
		}
		return sId;
	}
	
}
