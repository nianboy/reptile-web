package com.itsum.jc;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anapp.fw.cache.FWCacheManager;

import jmini.db.iservice.IMiniService;
import jmini.env.Props;
import jmini.env.SpringEnv;
import jmini.env.exception.ConfigException;

/**
 * 环境初始化
 * 
 * @author Jason Ma
 *
 */
public class ENV {
	
	private static Logger logger = LoggerFactory.getLogger(ENV.class);
	
	private static IMiniService db = null;
	
	public static void init(){
		logger.info("init start ...........................");
		logger.info("load jc.conf start.");
		File sysconfig = new File("app_conf/jc.conf");
		logger.info("load config from {}", sysconfig.getAbsolutePath());
		//初始化系统配置文件信息
		try {
			Props.load(sysconfig);
		} catch (IOException e) {
			logger.error("", e);
			throw new ConfigException("config init exception",e);
		}
		logger.info("load config ok.");
		//初始化jmini spring环境配置文件
		try {
			SpringEnv.init(new String[]{"classpath*:spring-jmini-fw.xml","classpath*:spring-app.xml"});
		} catch (IOException e) {
			logger.error("", e);
			throw new ConfigException("spring init exception", e);
		}
		logger.info("load spring env ok.");
		logger.info("load FWCacheManager.init start.");
		FWCacheManager.init();
		logger.info("load FWCacheManager.init end.");
	}
	
	/**
	 * 获取数据库服务
	 * 
	 * @return
	 */
	public static IMiniService db(){
		if(db == null){
			db = SpringEnv.getMiniService("DB");
		}
		return db;
	}
	
}
