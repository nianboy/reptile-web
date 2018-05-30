package com.anapp.fw.conf;

import jmini.env.Props;

public class Conf {
	
	public static final String SERVER_URL = "server.url";
	
	public static final String FILE_ROOT_DIR = "file.root.dir";
	
	/**
	 * 读取单个系统属性
	 * @param key
	 * @return
	 */
	public static String p(String key){
		return Props.p(key);
	}

}
