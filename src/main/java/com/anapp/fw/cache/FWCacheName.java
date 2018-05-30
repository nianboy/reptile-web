package com.anapp.fw.cache;

/**
 * 框架缓存key Enum
 * @author Jason Ma
 *
 */
public enum FWCacheName {
	/** 产品分类缓存 */
	UPLOAD_FILE("uploadFileRead"),
	PUBLISH_FILE("publishFileRead");
	
	private String cacheName = null;
	
	private FWCacheName(String cacheName){
		this.cacheName = cacheName;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	
}
