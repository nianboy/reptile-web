package com.anapp.fw;

import jmini.db.iservice.IMiniService;
import jmini.env.SpringEnv;

/**
 * 数据库帮助类
 * @author Jason Ma
 */
public class DB {
	
	public static final String dbName = "DB";
	
	public static final String dbYqName = "DBYQ";
	
	public static IMiniService getDefault(){
		return SpringEnv.getMiniService(dbName);
	}
	
	public static IMiniService getYqDB(){
		return SpringEnv.getMiniService(dbYqName);
	}
	
}
