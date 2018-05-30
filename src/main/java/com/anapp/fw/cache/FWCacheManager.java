package com.anapp.fw.cache;

import java.io.Serializable;
import java.util.List;

import jmini.cache.EhCacheConfUtils;
import jmini.dto.IDataDTO;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

/**
 * 第三方缓存管理器
 * @author Jason Ma
 *
 */
public class FWCacheManager {
	
	public static CacheManager cacheManager = null;
	
	public static void init(){
		Configuration configuration = new Configuration();
		cacheManager = CacheManager.newInstance(configuration);
		if(FWCacheManager.getCache(FWCacheName.UPLOAD_FILE.getCacheName())==null){
			//读取文件缓存
			FWCacheManager.addDefaultCache(FWCacheName.UPLOAD_FILE.getCacheName(), 1000);
		}
		if(FWCacheManager.getCache(FWCacheName.PUBLISH_FILE.getCacheName())==null){
			//读取文件缓存
			FWCacheManager.addDefaultCache(FWCacheName.PUBLISH_FILE.getCacheName(), 1000);
		}
	}
	
	/**
	 * 使用默认参数创建一个缓存
	 * @param cacheName 缓存名称
	 */
	public static void addDefaultCache(String cacheName, int maxEntriesLocalHeap){
		CacheConfiguration config = EhCacheConfUtils.defaultConfig(cacheName, maxEntriesLocalHeap);
		cacheManager.addCache(new Cache(config));
	}
	
	/**
	 * 使用缓存配置信息创建一个缓存对象
	 * @param config
	 */
	public static void addCache(CacheConfiguration config){
		cacheManager.addCache(new Cache(config));
	}
	
	/**
	 * 获取缓存对象
	 * @param cacheName
	 * @return
	 */
	public static Cache getCache(String cacheName){
		return cacheManager.getCache(cacheName);
	}
	
	/**
	 * 从缓存中取出对象
	 * @param cacheName
	 * @param key
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getCache(String cacheName, Serializable key, Class<T> requiredType){
		 Element e = cacheManager.getCache(cacheName).get(key);
		 return e==null?null:(T)(e.getObjectValue());
	}
	
	/**
	 * 从缓存中取出列表对象
	 * @param cacheName
	 * @param key
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getCacheList(String cacheName, Serializable key, Class<T> requiredType){
		 Element e = cacheManager.getCache(cacheName).get(key);
		 return (List<T>) (e==null?null:e.getObjectValue());
	}
	
	/**
	 * 向缓存中放入对象
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void putCache(String cacheName, Serializable key, IDataDTO value){
		cacheManager.getCache(cacheName).put(new Element(key, value));
	}
	
	/**
	 * 向缓存中放入对象
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void putCache(String cacheName, Serializable key, List<? extends IDataDTO> value){
		cacheManager.getCache(cacheName).put(new Element(key, value));
	}

}
